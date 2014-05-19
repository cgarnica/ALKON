package com.quasar.alkon;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import com.quasar.alkon.Post;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class MainActivity extends Activity implements LocationListener {
	//private String urlBase = "http://10.0.2.2/alkon/";
	private String urlBase = "http://www.quasarbi.com/alkon/";
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Context context;
	private TextView txtLat;
	private TextView txtLon;
	private TextView txtAlarma;
	String lat;
	String provider;
	private String latitude, longitude;
	private boolean gpsEnable, networkEnable;
	private Spinner spiUsuario;
	private String[] strUsuario;
	private String strAlarma;
	private String strDistancia;
	private Toast mToast;
	private Usuario usuUsuarioActivo;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		txtLat = (TextView) findViewById(R.id.Latitud);
		txtLon = (TextView) findViewById(R.id.Longitud);

		txtAlarma = (TextView) findViewById(R.id.Alarma);

		/*
		 * 
		 * SPINNER USUARIO
		 */
		try {
			int cont;
			Post post = new Post();
			usuUsuarioActivo = new Usuario();
			String strUsuarioActivo = usuUsuarioActivo.getUsuario();
			String urlBaseUsuario = this.urlBase + "usuario.php";
			ArrayList parametros = new ArrayList();
			parametros.add("usuario");
			parametros.add(strUsuarioActivo);

			JSONArray usuario = post.getServerData(parametros, urlBaseUsuario);

			if (usuario != null && usuario.length() > 0) {
				int numRegistros = usuario.length();
				numRegistros = numRegistros + 1;
				strUsuario = new String[numRegistros];
				if (numRegistros > 0) {
					strUsuario[0] = "";
					for (cont = 1; cont < numRegistros; cont++) {
						strUsuario[cont] = usuario.getJSONObject(cont-1).getString("usuario");
					}
				}
			}
		} catch (Exception e) {
			Log.e("log_tag", "Error inesperado Spinner Usuario", e);
		}
		spiUsuario = (Spinner) findViewById(R.id.usuario_spinner);
		ArrayAdapter<String> dataAdapterDeparatamento = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strUsuario);
		dataAdapterDeparatamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spiUsuario.setAdapter(dataAdapterDeparatamento);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				threadObj();
			}
		}, 0, 10000);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0,	0, this);
	}

	public void onLocationChanged(Location location) {
		Usuario usuUsuario = new Usuario();
		double douLatitud = 0.0;
		double douLongitud = 0.0;
		String strUsuario = null;

		//String urlBaseR = "http://10.0.2.2/alkon/registro.php";
		String urlBaseR = "http://www.quasarbi.com/alkon/registro.php";
		Post post = new Post();

		txtLat = (TextView) findViewById(R.id.Latitud);
		txtLat.setText("Latitud = " + location.getLatitude());

		txtLon = (TextView) findViewById(R.id.Longitud);
		txtLon.setText("Longitud = " + location.getLongitude());

		douLatitud = location.getLatitude();
		douLongitud = location.getLongitude();
		strUsuario = usuUsuario.getUsuario();
		ArrayList parametros = new ArrayList();
		parametros.add("usuario");
		parametros.add(strUsuario.toString());
		parametros.add("latitud");
		parametros.add(String.valueOf(douLatitud).toString());
		parametros.add("longitud");
		parametros.add(String.valueOf(douLongitud).toString());

		try {
			JSONArray respuesta = post.getServerData(parametros, urlBaseR);
			if (respuesta.getJSONObject(0).getString("resp").equalsIgnoreCase("Error! datos no enviados")) {
				//Toast.makeText(getApplicationContext(),	respuesta.getJSONObject(0).getString("resp"), 1000).show();
				Log.i("log_tag", respuesta.getJSONObject(0).getString("resp"));
			} else if (respuesta.getJSONObject(0).getString("resp").equalsIgnoreCase("Datos enviados")) {
				//Toast.makeText(getApplicationContext(),respuesta.getJSONObject(0).getString("resp"), 1000).show();
				Log.i("log_tag", respuesta.getJSONObject(0).getString("resp"));
			}
		} catch (Exception e) {
			Log.e("log_tag", "Error inesperado", e);
			Toast.makeText(getApplicationContext(), "Error, Datos no Enviados",1000).show();
		}
		Hthread thread = new Hthread();
        thread.start();
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

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Latitud", "enable");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Latitud", "disable");
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitud", "status");
	}
	
	private void threadObj() {
		this.runOnUiThread(localizacionObj);
	}
	
	private Runnable localizacionObj = new Runnable() {
		public void run() {
			spiUsuario.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
					position = position + 1;
					try {
						txtAlarma = (TextView) findViewById(R.id.Alarma);
						Post post = new Post();
						String urlBaseLocalizacion = urlBase + "localizacionUsuario.php";
						String urlBaseDistancia = urlBase + "distancia.php";
						usuUsuarioActivo = new Usuario();
						String strUsuarioActivo = usuUsuarioActivo.getUsuario();
						ArrayList parametros = new ArrayList();
						parametros.add("usuarioObj");
						parametros.add(String.valueOf(arg0.getSelectedItem()).toString());
						parametros.add("usuarioPri");
						parametros.add(strUsuarioActivo.toString());
											
						ArrayList parametros1 = new ArrayList();
						parametros1.add("usuarioObj");
						parametros1.add(String.valueOf(arg0.getSelectedItem()).toString());
						
						JSONArray localizacionUsuario = post.getServerData(parametros, urlBaseLocalizacion);
						JSONArray distanciaUsuario = post.getServerData(parametros1, urlBaseDistancia);
						
						if (localizacionUsuario != null && localizacionUsuario.length() > 0) {
							int numRegistros = localizacionUsuario.length();
							if (numRegistros > 0) {
								strAlarma = localizacionUsuario.getJSONObject(0).getString("activacion");
								strDistancia = distanciaUsuario.getJSONObject(0).getString("distancia");
								if(strAlarma.equalsIgnoreCase("1")){
									txtAlarma.setText("Alarma ON >> " + strDistancia + " m");
					                try {
					                    Timer endSound = new Timer();
					                    endSound.schedule(new TimerTask() {
					                        public void run() { 
					                        	Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
							                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
							                    if (mp != null) {
					                            	mp.start();
					                            } 
					                        } 
					                    }, 2000); 
					                 } catch (Exception e) {
					                     e.printStackTrace();
					                 }
								} else if (strAlarma.equalsIgnoreCase("0")){
									txtAlarma.setText("Alarma OFF >> " + strDistancia + " m");
								}
							}
						} else if (localizacionUsuario == null) {
							txtAlarma.setText("Sin Alarma!!!");
						}
					} catch (Exception e) {
						Log.e("log_tag","Error inesperado TextView Usuario Latitud y Longitud",e);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
			//Toast.makeText(getApplicationContext(), "Tiempo", Toast.LENGTH_LONG).show();
		}
	};
	
	class Hthread extends Thread {
		
		public void run() {
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}