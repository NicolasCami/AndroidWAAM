package fr.univpau.rpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.univpau.entity.MessageEntity;
import fr.univpau.util.MyPreferenceManager;
import fr.univpau.waam.MainActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class MessageRPC extends AsyncTask<Void, Void, List<MessageEntity>> {
    
    private static final String 	BASE_URL = "http://www.iut-adouretud.univ-pau.fr/~olegoaer/waam/wallMessages.php?my_latitude={1}&my_longitude={2}&my_radius={3}";
    
    private volatile MainActivity 	_screen;
    private HttpClient 				_client;
    private ProgressDialog 			_progress;
    private MyPreferenceManager		_preferenceManager;
    private final boolean			_showMessage;

    public MessageRPC(MainActivity s, boolean showMessage) {
        _screen = s;
        _showMessage = showMessage;
        _client = new DefaultHttpClient();
        if(_showMessage) {
        	_progress = new ProgressDialog(_screen);
        }
        _preferenceManager = new MyPreferenceManager(_screen);
    }
    
    @Override
    protected void onPreExecute() {
    	if(_showMessage) {
	        _progress.setTitle("Veuillez patienter");
	        _progress.setMessage("Récupération des données en cours...");
	        _progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);      
	        _progress.show();
    	}
    }

    @Override
    protected List<MessageEntity> doInBackground(Void... params) {
    	String url = prepareURL();
    	List<MessageEntity> responseObjects = new ArrayList<MessageEntity>();
        //EditText editText = (EditText) this.screen.findViewById(R.id.text);
        
        try {
            HttpResponse response = _client.execute(new HttpGet(url));
            String reponseText = EntityUtils.toString(response.getEntity());
            JSONObject obj = new JSONObject(reponseText);
            Log.i("WAAM", reponseText);
            JSONArray arr = obj.getJSONArray("items");
            for(int i = 0; i < arr.length(); i++) {
            	JSONObject msg = arr.getJSONObject(i);
                String date = msg.getString("time");
                byte[] utf8 = msg.getString("msg").getBytes("UTF-8");
                String content =  new String(utf8, "UTF-8");
                int gender = msg.getInt("gender");
                double distance = msg.getDouble("meters");
                JSONArray locationArray = msg.getJSONArray("geo");
                double[] location = new double[2];
                location[0] = locationArray.getDouble(0);
                location[1] = locationArray.getDouble(1);
                responseObjects.add(new MessageEntity(date, content, gender, distance, location));
            }
        } catch (Exception e) {
        	Log.e("RPC","Exception levée", e);
        }
        
        return responseObjects;
    }

    @Override
    protected void onPostExecute(List<MessageEntity> result) {
        if(_showMessage && _progress.isShowing()) _progress.dismiss();
        //Toast.makeText(this.screen, "FINI", Toast.LENGTH_SHORT).show();
        _screen.populateMessages(result);
    }
    
    protected String prepareURL() {
        String url = BASE_URL;
        url = url.replace("{1}", Double.toString(_preferenceManager.getLat()));
        url = url.replace("{2}", Double.toString(_preferenceManager.getLng()));
        url = url.replace("{3}", Double.toString(_preferenceManager.getDistance()));
        //Log.i("LOLOLOL", url);
        return url;
    }
    
}