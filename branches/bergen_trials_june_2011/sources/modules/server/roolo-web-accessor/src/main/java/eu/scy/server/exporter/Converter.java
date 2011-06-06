package eu.scy.server.exporter;

import info.collide.sqlspaces.client.TupleIterator;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IContext;

public class Converter {

	private TupleSpace ts;

	private List<String> users;

	private boolean connected;

	public Converter(int port) {
		this(port, new String[0]);
	}

	public Converter(int port, String[] users) {
		connected = false;
		try {
			if (users == null) {
				this.users = new ArrayList<String>();
			} else {
				this.users = new ArrayList<String>(Arrays.asList(users));
			}
			ts = new TupleSpace("127.0.0.1", port, "actions");
			connected = true;
		} catch (TupleSpaceException tse) {
			tse.printStackTrace();
		}
	}

	public void convertToFile(File f) throws TupleSpaceException, IOException {
		TupleIterator allActions = ts.readAll(new Tuple(),1000);
		Document doc = convertToXML(allActions);
		writeFile(doc, f);
	}

	public String convertToString() throws TupleSpaceException, IOException {
		TupleIterator allActions = ts.readAll(new Tuple(),1000);
		Document doc = convertToXML(allActions);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		String result = outputter.outputString(doc);
		return result;
	}

	public byte[] exportZip() throws IOException, TupleSpaceException,
			URISyntaxException {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		ZipOutputStream zipOS = new ZipOutputStream(boas);
		Set<String> entries = new HashSet<String>();
		RetrieveElos e = new RetrieveElos();

		TupleIterator allActions = ts.readAll(new Tuple(),1000);
		for (Tuple tuple : allActions) {
			String eloUri = tuple.getField(8).getValue().toString();
			if (entries.contains(eloUri)) {
				continue;
			}
			String xmlELO = e.getXmlELO(new URI(eloUri));
			entries.add(eloUri);
			if (xmlELO == null) {
				continue;
			}
			eloUri = eloUri.replace("/", "_");
			ZipEntry entry = new ZipEntry(eloUri + ".eloXML");
			zipOS.putNextEntry(entry);
			zipOS.write(xmlELO.getBytes());
			zipOS.closeEntry();

		}
		zipOS.close();
		return boas.toByteArray();

	}

	public void convertToHTMLTable(Writer out) throws TupleSpaceException, IOException {
		TupleIterator allActions = ts.readAll(new Tuple(),1000);
		int maxFields = Integer.MIN_VALUE;
		for (Tuple t : allActions) {
			maxFields = Math.max(maxFields, t.getNumberOfFields());
		}
		//sortTupleArray(allActions);
		out.write("<table id=\"rounded-corner\" summary=\"Overview of all Action Logs\" >");
		out.write("<thead>");
		out.write("<th />Constant<th>UniqueID</th><th>Timestamp</th><th>Type</th><th>User</th><th>Tool</th><th>Mission</th><th>Session</th><th>ELO</th>");
		for (int z = 10; z <= maxFields; z++) {
			out.write("<th>Attribute No." + (z - 9) + "</th>");
			out.flush();
		}
		out.write("</thead>");
		out.write("<tbody>");
		allActions = ts.readAll(new Tuple(),1000);
		for (Tuple t : allActions) {
			if (tupleShouldBeDropped(t)) {
				continue;
			}
			out.write("<tr>");
			for (int i = 0; i < maxFields; i++) {
				out.write("<td>");
				if (i < t.getNumberOfFields()) {
					String longValue = "";
					longValue = (t.getField(i).getValue().toString());
					if (i == 2) {
						Date d = new Date(Long.parseLong(longValue));
						DateFormat formatter = new SimpleDateFormat();
						longValue = formatter.format(d);
						out.write(longValue);
					} else {
						if (longValue.length() <= 10) {
							out.write(longValue);
						} else {
							String shortValue = longValue.substring(0, 5)
									+ "[...]";
							String uid = (t.getTupleID().toString() + i);
							out.write("<span id=\""
									+ uid
									+ "\" title=\""
									+ longValue
									+ "\" style=\"cursor:pointer\" onClick=\"toggleContent(\'"
									+ uid + "\', \'" + shortValue + "\', \'"
									+ longValue + "\');\">" + shortValue
									+ "</span>");
						}
						if (i == 8) {
							out.write("&nbsp;<a href=\"downloadElo.jsp?eloUri="
									+ Base64.encodeToString(
											longValue.getBytes(), false)
									+ "\"><img border=\"0\" src=\"table-images/save.gif\" height=\"16\" width=\"16\" title=\"Click to save\" /></a>");

						}
					}
				} else {
					out.write("&nbsp;");
				}
				out.write("</td>");
			}
			out.write("</tr>");
			out.flush();
		}
		out.write("</tbody>");
		out.write("</table>");

	}

	public String[] getUsers() throws TupleSpaceException {
		if (!connected) {
			return new String[0];
		}
		Set<String> users = new HashSet<String>();
		TupleIterator readAll = ts.readAll(new Tuple(), 1000);
		for (Tuple tuple : readAll) {
			String user = tuple.getField(4).getValue().toString();
			int indexOfUserSeparator = user.indexOf("@");
			if (indexOfUserSeparator == -1) {
				continue;
			}
			user = user.substring(0, indexOfUserSeparator);
			if (users.contains(user)) {
				continue;
			}
			users.add(user);
		}
		return users.toArray(new String[users.size()]);
	}

	private Document convertToXML(final TupleIterator tuples) throws IOException {
		//sortTupleArray(tuples);
		Document doc = new Document();
		Element rootElement = new Element("actionLogs");
		rootElement.setAttribute("server", ts.toString());
		doc.setRootElement(rootElement);
		Element spaceElement = new Element("actions");
		for (Tuple tuple : tuples) {
			if (tupleShouldBeDropped(tuple)) {
				continue;
			}
			Element actionElement = new Element("action");
			IAction actionFromTuple = ActionTupleTransformer
					.getActionFromTuple(tuple);

			Element idElement = new Element("id");
			idElement.addContent(actionFromTuple.getId());
			actionElement.addContent(idElement);
			Element userElement = new Element("user");

			userElement.addContent(actionFromTuple.getUser());
			actionElement.addContent(userElement);
			Element timeElement = new Element("time");
			timeElement.addContent(String.valueOf(actionFromTuple
					.getTimeInMillis()));
			actionElement.addContent(timeElement);
			Element typeElement = new Element("type");
			typeElement.addContent(actionFromTuple.getType());
			actionElement.addContent(typeElement);
			Element contextElement = new Element("context");
			IContext context = actionFromTuple.getContext();
			String mission = context.get(ContextConstants.mission);
			String session = context.get(ContextConstants.session);
			String tool = context.get(ContextConstants.tool);
			Element missionElement = new Element("mission");
			missionElement.addContent(mission);
			contextElement.addContent(missionElement);
			Element sessionElement = new Element("session");
			sessionElement.addContent(session);
			contextElement.addContent(sessionElement);
			Element toolElement = new Element("tool");
			toolElement.addContent(tool);
			contextElement.addContent(toolElement);
			actionElement.addContent(contextElement);
			Map<String, String> attributes = actionFromTuple.getAttributes();
			Iterator<Entry<String, String>> it = attributes.entrySet()
					.iterator();
			Element propElement = new Element("properties");
			while (it.hasNext()) {
				Map.Entry<String, String> pair = (Map.Entry<String, String>) it
						.next();
				Element attElement = new Element(pair.getKey());
				attElement.addContent(pair.getValue());
				propElement.addContent(attElement);

			}

			actionElement.addContent(propElement);
			spaceElement.addContent(actionElement);
		}
		rootElement.addContent(spaceElement);
		return doc;
	}

	private boolean tupleShouldBeDropped(Tuple tuple) {
		String user = tuple.getField(4).getValue().toString();
		int indexOfUserSeparator = user.indexOf("@");
		if (indexOfUserSeparator != -1) {
			user = user.substring(0, indexOfUserSeparator);
			if (users == null || users.isEmpty()) {
				return false;
			} else {
				for (int i = 0; i < users.size(); i++) {
					if (user.toLowerCase()
							.trim()
							.equals(((String) users.get(i)).toLowerCase()
									.trim())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void sortTupleArray(Tuple[] allActionTuples) {
		Arrays.sort(allActionTuples, new Comparator<Tuple>() {

			@Override
			public int compare(Tuple o1, Tuple o2) {
				long ts1 = Long.parseLong(o1.getField(2).getValue().toString());
				long ts2 = Long.parseLong(o2.getField(2).getValue().toString());
				if (ts1 < ts2) {
					return -1;
				} else if (ts1 > ts2) {
					return +1;
				}
				return 0;
			}
		});
	}

	public void writeFile(Document doc, File f) throws IOException {
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		outputter.toString();
		FileOutputStream output = new FileOutputStream(f);
		outputter.output(doc, output);
		// System.out.println("File written to " + f.getAbsolutePath());

	}

	public boolean isConnected() {
		return connected;
	}

}
