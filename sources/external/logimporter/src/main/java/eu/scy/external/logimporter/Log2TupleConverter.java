package eu.scy.external.logimporter;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.ActionTupleTransformer;

/**
 * This class can transform openfire logs into actions and tuples in order to reconstruct missing
 * action logs. It will not overwrite already existing log tuples and merge the openfire log into
 * the action space.
 * 
 * @author weinbrenner
 * 
 */
public class Log2TupleConverter {

    private static final int FRACTION = 100;

    private String tsHost;

    private int tsPort;

    private ActionPacketTransformer transformer;

    private boolean output2Console;

    public Log2TupleConverter(String tsHost, int tsPort, boolean output2Console) {
        this.tsHost = tsHost;
        this.tsPort = tsPort;
        this.output2Console = output2Console;
        transformer = new ActionPacketTransformer();
    }

    public static void main(String[] args) throws XmlPullParserException, FileNotFoundException, TupleSpaceException {
        if (args != null && args.length != 0) {
            Log2TupleConverter c = new Log2TupleConverter("localhost", 2525, true);
            ArrayList<Action> allActions = new ArrayList<Action>();
            for (String s : args) {
                List<Action> actions = c.parseFile(new File(s));
                allActions.addAll(actions);
            }
            List<Tuple> tuples = c.transformToTuples(allActions);
            c.mergeTuplesInActionSpace(tuples);
        } else {
            System.err.println("Please pass at least one logfile as an argument!");
            System.exit(1);
        }
    }

    private void mergeTuplesInActionSpace(List<Tuple> tuples) throws TupleSpaceException {
        if (output2Console) {
            System.out.println("Merging " + tuples.size() + " tuples into the actions on '" + tsHost + "' ... ");
        }
        int written = 0;
        int part = tuples.size() / FRACTION;
        TupleSpace ts = new TupleSpace(tsHost, tsPort, "actions");
        for (int i = 0; i < tuples.size(); i++) {
            Tuple t = tuples.get(i);
            if (output2Console && i % part == 0) {
                System.out.println((i * (100 / FRACTION) / part) + " %");
            }
            int c = ts.count(t);
            if (c == 0) {
                ts.write(t);
                written++;
            }
        }
        ts.disconnect();
        if (output2Console) {
            System.out.println("Merging finished! (" + written + " written, " + (tuples.size() - written) + " already there)");
        }
    }

    private List<Tuple> transformToTuples(List<Action> actions) {
        if (output2Console) {
            System.out.println("Converting " + actions.size() + " actions to tuples ...");
        }
        ArrayList<Tuple> result = new ArrayList<Tuple>();
        for (Action a : actions) {
            Tuple t;
            try {
                t = ActionTupleTransformer.getActionAsTuple(a);
                result.add(t);
            } catch (NullPointerException e) {
                System.err.println("### NPE while transforming an action to tuple! Action was:\n" + a.toString());
            }
        }
        if (output2Console) {
            System.out.println("Conversion finished!");
        }
        return result;
    }

    public List<Action> parseFile(File f) throws XmlPullParserException, FileNotFoundException {
        if (output2Console) {
            System.out.println("Parsing file " + f.getName() + " ...");
        }
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new FileReader(f));

        ArrayList<Action> result = parse(parser);

        if (output2Console) {
            System.out.println("Parsed " + result.size() + " actions!");
        }
        return result;
    }

    public List<Action> parseString(String s) throws XmlPullParserException, FileNotFoundException {
        if (output2Console) {
            System.out.println("Parsing string with " + s.length() + " chars ...");
        }
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(s));

        ArrayList<Action> result = parse(parser);

        if (output2Console) {
            System.out.println("Parsed " + result.size() + " actions!");
        }
        return result;
    }

    private ArrayList<Action> parse(XmlPullParser parser) {
        HashSet<String> knownIds = new HashSet<String>();
        ArrayList<Action> result = new ArrayList<Action>();

        transformer.resetParser();

        try {
            boolean done = false;
            boolean started = false;
            String path = "";
            LinkedList<String> lastElementName = new LinkedList<String>();

            try {
                while (!done) {
                    int eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equals(transformer.getName())) {
                            started = true;
                        }
                        if (!started) {
                            continue;
                        }
                        if (parser.getName() != null) {
                            path += "/" + parser.getName();
                            lastElementName.add("/" + parser.getName());
                            transformer.startNode(path);
                            if (parser.getAttributeCount() > 0) {
                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    String attribKey = parser.getAttributeName(i);
                                    String attribValue = parser.getAttributeValue(i);
                                    transformer.mapXMLNodeToObject(path + "@" + attribKey, attribValue);
                                }
                            }
                        }
                    } else if (started && eventType == XmlPullParser.TEXT) {
                        transformer.mapXMLNodeToObject(path, parser.getText());
                    } else if (started && eventType == XmlPullParser.END_TAG) {
                        transformer.endNode(path);
                        path = path.substring(0, path.lastIndexOf(lastElementName.getLast()));
                        lastElementName.removeLast();
                        if (parser.getName().equals(transformer.getName())) {
                            Action a = (Action) transformer.getObject();
                            if (!knownIds.contains(a.getId())) {
                                result.add(a);
                                knownIds.add(a.getId());
                            }
                            transformer.resetParser();
                            started = false;
                        }
                    } else if (eventType == XmlPullParser.END_DOCUMENT) {
                        done = true;
                    }
                }
            } catch (EOFException e) {
                Action a = (Action) transformer.getObject();
                if (!knownIds.contains(a.getId())) {
                    result.add(a);
                    knownIds.add(a.getId());
                }
                System.err.println("### Unexpected end of logfile! If this is the last log file of a running openfire server, that's okay ...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
