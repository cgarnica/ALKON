package com.quasar.alkon;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends Activity {
	
	private String usuario;
	
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Button login = (Button)findViewById(R.id.login);
        final EditText usuario = (EditText)findViewById(R.id.usuario);
        //final EditText password = (EditText)findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            //String urlBase = "http://10.0.2.2/alkon/login.php";
            String urlBase = "http://www.quasarbi.com/alkon/login.php";
            String user;
            @Override
            @SuppressLint("ShowToast")
            public void onClick(View view) {
                ArrayList parametros = new ArrayList();

                parametros.add("usuario");
                parametros.add(usuario.getText().toString());
                //parametros.add("password");
                //parametros.add(password.getText().toString());

                try{
                	Usuario usuUsuario = new Usuario();
                    Post post = new Post();
                    JSONArray logger = post.getServerData(parametros,urlBase);

                    if(logger != null && logger.length()>0){
                        JSONObject json_logger = logger.getJSONObject(0);
                        String numRegistros = json_logger.getString("usuario");
                        if(!numRegistros.equalsIgnoreCase("")){
                            user = logger.getJSONObject(0).getString("usuario");
                            Toast.makeText(getApplicationContext(), "Inicia sesión "+user, 1000).show();
                            MainActivityClass();
                            usuUsuario.setUsuario(user.toString());
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error, verifique usuario y password", 1000).show();
                        usuario.setText("");
                        //password.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error al Conectar con el Servidor.", 1000).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String httpGetData(String mURL){
        String response = "";
        mURL = mURL.replace(" ", "%20");
        Log.i("LocAndroid Response HTTP Threas", "Ejecutando get 0: " + mURL);
        HttpClient httpClient = new DefaultHttpClient();

        Log.i("LocAndroid Response HTTP Thread", "Ejecutando get 1");
        HttpGet httpPost = new HttpGet(mURL);
        Log.i("LocAndroid Response HTTP Thread", "Ejecutando get 2");

        try{
            Log.i("LocAndroid Response HTTP", "Ejecutando get Final");
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            Log.i("LocAndroid Response HTTP", response);
        } catch (ClientProtocolException e){
            Log.i("LocAndroid Response HTTP ERROR 1", e.getMessage());
        } catch (IOException e){
            Log.i("LocAndroid Response HTTP ERROR 2", e.getMessage());
        }
        return response;
    }
    public void MainActivityClass(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
