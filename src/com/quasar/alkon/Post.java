package com.quasar.alkon;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Post {

    private InputStream is = null;
    private String respuesta = "";

    private void conectaPost(ArrayList parametros, String URL) {
        ArrayList nameValuePairs;
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(URL);
            nameValuePairs = new ArrayList();
            if (parametros != null) {
                for (int i = 0; i < parametros.size() - 1; i += 2) {
                    nameValuePairs.add(new BasicNameValuePair((String)parametros.get(i), (String)parametros.get(i + 1)));
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            }
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString(), e);
        } finally {

        }
    }

    private void getRespuestaPost() {
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(is, "latin1"));
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                System.out.println("DD = "+line);
                sb.append(line + "\n");
            }
            is.close();
            respuesta = sb.toString();
            Log.e("log_tag", "Cadena JSon " + respuesta);
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
    }

    @SuppressWarnings("finally")
    private JSONArray getJsonArray() {
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(respuesta);
        } catch (Exception e) {

        } finally {
            return jArray;
        }
    }

    public JSONArray getServerData(ArrayList parametros, String URL) {
        conectaPost(parametros, URL);
        if (is != null) {
            getRespuestaPost();
        }
        if (respuesta != null && respuesta.trim() != "") {
            return getJsonArray();
        } else {
            return null;
        }
    }
}
