package eu.scy.client.tools.formauthor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class OldWebServicesController {
	private final String saveElo = "saveELO/";
	private final String getElos = "getELOListAndroid";
	private final String getElo = "getELOAndroid";
	private final String updateElo = "updateELOAndroid";
	private final Configuration config;
	private static final String SCY_FORM = "scy/form";
	private static final String SCY_FORMTEMPLATE = "scy/formtemplate";
	private static final String SCY_FORM_EXTEMSION = ".form";
	private static final String SCY_FORMTEMPLATE_EXTENSTION = ".formtmp";

	public OldWebServicesController() {
		config = new Configuration();
	}

	public void getFormJSON() {

		HttpClient httpClient = new DefaultHttpClient();
		// HttpContext localContext = new BasicHttpContext();
		// HttpGet httpGet = new HttpGet(dcc.getServerUrl() + methodName);
		// try {
		// HttpResponse response = httpClient.execute(httpGet, localContext);
		// response.getStatusLine();
		// InputStream is = response.getEntity().getContent();
		// String result = convertStreamToString(is);
		// Log.i("Server result", result);
		// Toast.makeText(application, result, 10).show();

		// } catch (ClientProtocolException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return sb.toString();
	}

	public void postFormJSON(DataFormModel form) {
		JSONObject jsonobj = new JSONObject();
		try {
			// required parameters
			jsonobj.put("content", form.toXML().toString());
			jsonobj.put("username", config.getGroupname());
			jsonobj.put("password", config.getPassword());
			jsonobj.put("language", "de");
			jsonobj.put("title", form.getTitle());
			jsonobj.put("type", SCY_FORMTEMPLATE);
			// optional parameters
			jsonobj.put("country", "de");
			jsonobj.put("description", form.getDescription());
//			jsonobj.put("formtypeextension", SCY_FORMTEMPLATE_EXTENSTION);
			// jsonobj.put("author", config.getUserName());
			// jsonobj.put("test", "ä");
			// TODO Groupname als Author in Client
			String mUrl = config.getServerUrl() + saveElo;
			String message = makeRequestTextResult(mUrl, jsonobj);
			if (message == null || message.equals("")) {
				JOptionPane.showMessageDialog(null, Localizer
						.getString("MESSAGE"), message,
						JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, message, message,
						JOptionPane.ERROR_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Localizer
					.getString("ERROR_URL_INCORRECT"), Localizer
					.getString("ERROR"), JOptionPane.OK_CANCEL_OPTION);
			e.printStackTrace();
		}
	}

	ArrayList<DataFormPreviewModel> getEloForms(String formtype) {
		JSONObject jsonobj = new JSONObject();
		try {

			// jsonobj.put("content", form.toXML().toString());
			jsonobj.put("username", config.getUserName());
			jsonobj.put("password", config.getPassword());
			// TODO AUTHOR einfügen
			jsonobj.put("author", config.getGroupname());

			jsonobj.put("language", "de");
			jsonobj.put("country", "de");
			jsonobj.put("formtype", formtype);
			jsonobj.put("ownformulars", "");
			// jsonobj.put("title", form.getTitle());
			// jsonobj.put("description", form.getDescription());

			String mUrl = config.getServerUrl() + getElos;

			ArrayList<DataFormPreviewModel> alDfpm = new ArrayList<DataFormPreviewModel>();
			try {
				JSONObject jsonResult = makeRequestJSONResult(mUrl, jsonobj);
				JSONArray jsonELOArray = jsonResult.getJSONArray("elos");

				for (int i = 0; i < jsonELOArray.length(); i++) {
					JSONObject jsonForm = jsonELOArray.getJSONObject(i);
					DataFormPreviewModel dfpm = new DataFormPreviewModel(
							jsonForm.getString("title"), jsonForm
									.getString("description"), jsonForm
									.getString("author"), jsonForm
									.getString("uri"));
					alDfpm.add(dfpm);
				}
			} catch (HttpHostConnectException ex) {
				JOptionPane.showMessageDialog(null, Localizer
						.getString("ERROR_URL_INCORRECT"), Localizer
						.getString("ERROR"), JOptionPane.OK_CANCEL_OPTION);
				ex.printStackTrace();
			}

			return alDfpm;
			// JOptionPane.showMessageDialog(null, jsonResult.toString(),
			// "Result", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String makeRequestTextResult(String path, JSONObject params)
			throws Exception {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(path);

		// UTF-8 must be set HERE!
		StringEntity se = new StringEntity(params.toString(), "UTF-8");
		// se.setContentEncoding("UTF-8");
		httpost.setEntity(se);
		httpost.setHeader("Accept", "text/plain");
		httpost.setHeader("Content-type", "application/json");

		HttpResponse response = httpclient.execute(httpost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream is = response.getEntity().getContent();
			String result = convertStreamToString(is);
			return result;
		} else {
			throw new HttpException("Did not get meaningful response from server.");
		}
	}

	private static JSONObject makeRequestJSONResult(String path,
			JSONObject params) throws Exception {
		JSONObject resultJSON = null;

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(path);

		StringEntity se = new StringEntity(params.toString(), "UTF-8");

		httpost.setEntity(se);
		httpost.setHeader("Accept", "application/json");
		httpost.setHeader("Content-type", "application/json");

		HttpResponse response = httpclient.execute(httpost);

		HttpEntity entity = response.getEntity();
		// If the response does not enclose an entity, there is no need
		// to worry about connection release

		if (entity != null) {

			// A Simple JSON Response Read
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);

			// A Simple JSONObject Creation
			resultJSON = new JSONObject(result);

		}
		return resultJSON;
	}

	public void getElo(String uri, DataFormModel dfm) {
		JSONObject jsonobj = new JSONObject();
		dfm.clear();
		// DataFormModel dfm = null;
		try {

			jsonobj.put("uri", uri);

			String mUrl = config.getServerUrl() + getElo;

			JSONObject jsonResult = makeRequestJSONResult(mUrl, jsonobj);
			String strFormXML = (String) jsonResult.get("content");
			// dfm = new DataFormModel();
			// dfm.addObserver(dfv);
			dfm.fromString(strFormXML);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return dfm;

	}

}
