package info.collide.android.scydatacollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public class WebServicesController {

    private final String saveElo = "saveELO/";

    private final String queryElos = "query/";

    private final String getElo = "getELO/";

    private final DataCollectorConfiguration config;

    private final Activity activity;

    protected static final int GUIUPDATEIDENTIFIER = 99822832;

    ProgressDialog mypd;

    public WebServicesController(Activity activity) {
        this.activity = activity;
        config = new DataCollectorConfiguration(activity);
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine() method. We iterate until the BufferedReader return null which means there's no more data to read. Each line will appended to a StringBuilder and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

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

        return sb.toString();
    }

    public void postFormJSON(final DataCollectorFormModel form) throws Exception {
        JSONObject jsonobj = new JSONObject();
        // TODO ENCODING
        jsonobj.put("content", form.toXML());
        jsonobj.put("username", config.getUserName());
        jsonobj.put("password", config.getPassword());
        jsonobj.put("language", "en");
        jsonobj.put("country", "en");
        jsonobj.put("title", form.getTitle());
        jsonobj.put("description", form.getDescription());
        jsonobj.put("type", "scy/formauthor");

        String mUrl = config.getServerUrl() + saveElo;
        makeRequestTextResult(mUrl, jsonobj);
    }

    public ArrayList<DataFormPreviewModel> getEloForms(String ownForms) throws Exception {
        JSONObject request = new JSONObject();
        request.put("username", config.getUserName());
        request.put("password", config.getPassword());
        JSONObject query = new JSONObject();
        query.put("metadatakey", "technicalFormat");
        query.put("metadatavalue", "scy/formauthor");

        JSONArray metadata = new JSONArray();
        metadata.put("title");
        metadata.put("description");
        metadata.put("author");
        metadata.put("identifier");

        request.put("query", query);
        request.put("metadata", metadata);

        String mUrl = config.getServerUrl() + queryElos;

        JSONArray jsonELOArray = doJSONRequestWithJSONArrayResult(mUrl, request);

        ArrayList<DataFormPreviewModel> alDfpm = new ArrayList<DataFormPreviewModel>();
        for (int i = 0; i < jsonELOArray.length(); i++) {
            JSONObject jsonForm = jsonELOArray.getJSONObject(i);

            String title = jsonForm.getString("title");
            String description = jsonForm.getString("description");
            // JSONArray authors = jsonForm.getJSONArray("author");
            // we are only interested in the first author
            // String author = authors.getJSONObject(0).getString("vcard");
            String author = jsonForm.getString("author");
            String uri = jsonForm.getString("identifier");

            DataFormPreviewModel dfpm = new DataFormPreviewModel(title, description, author, uri);
            alDfpm.add(dfpm);
        }

        return alDfpm;
    }

    private String makeRequestTextResult(String path, JSONObject params) throws Exception {
        return makeJSONRequest(path, params, "text/plain");
    }

    private String makeJSONRequest(String path, JSONObject params, String acceptHeader) throws Exception {
        StringEntity se = new StringEntity(params.toString(), "UTF-8");
        HttpEntity entity = doRequest(path, se, acceptHeader, "application/json");
        if (entity != null) {
            InputStream is = entity.getContent();
            return convertStreamToString(is);
        }
        return null;
    }

    private String makeStringRequest(String path, String param, String acceptHeader) throws Exception {
        StringEntity se = new StringEntity(param, "UTF-8");
        HttpEntity entity = doRequest(path, se, acceptHeader, "text/plain");
        if (entity != null) {
            InputStream is = entity.getContent();
            return convertStreamToString(is);
        }
        return null;
    }

    private HttpEntity doRequest(String path, HttpEntity requestEntity, String acceptHeader, String contentHeader) throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpPost httpost = new HttpPost(path);
        httpost.setEntity(requestEntity);
        httpost.setHeader("Accept", acceptHeader);
        httpost.setHeader("Content-type", contentHeader);
        HttpResponse response = httpclient.execute(httpost);
        return response.getEntity();
    }

    private JSONArray doJSONRequestWithJSONArrayResult(String path, JSONObject params) throws Exception {
        JSONArray resultJSON = null;
        String result = makeJSONRequest(path, params, "application/json");
        resultJSON = new JSONArray(result);
        return resultJSON;
    }

    private JSONObject doStringRequestWithJSONObjectResult(String path, String param) throws Exception {
        String result = makeStringRequest(path, param, "application/json");
        return new JSONObject(result);
    }

    public DataCollectorFormModel fetchDCFMFromServer(String uri) {
        try {
            String mUrl = config.getServerUrl() + getElo;
            JSONObject jsonResult = doStringRequestWithJSONObjectResult(mUrl, uri);
            String strFormXML = (String) jsonResult.get("content");
            return DataCollectorFormModel.fromString(strFormXML);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, R.string.connectionError, Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}
