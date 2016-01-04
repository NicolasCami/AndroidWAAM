package fr.univpau.rpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import fr.univpau.waam.NewMessageActivity;

public class SendMessageRPC extends AsyncTask<String, Void, Void> {
    
    private static final String 		BASE_URL = "http://www.iut-adouretud.univ-pau.fr/~olegoaer/waam/newMessage.php";
    
    private volatile NewMessageActivity _activity;
    private HttpClient 					_client;
    private ProgressDialog 				_progress;

    public SendMessageRPC(NewMessageActivity s) {
    	_activity = s;    
        _client = new DefaultHttpClient();
        _progress = new ProgressDialog(_activity);
    }
    
    @Override
    protected void onPreExecute() {
        _progress.setTitle("Veuillez patienter");
        _progress.setMessage("Envoie du message en cours...");
        _progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);      
        _progress.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {   
        	HttpPost post = new HttpPost(BASE_URL);
        	List<NameValuePair> postParams = new ArrayList<NameValuePair>(4);
        	postParams.add(new BasicNameValuePair("my_latitude", params[0]));
        	postParams.add(new BasicNameValuePair("my_longitude", params[1]));
        	postParams.add(new BasicNameValuePair("my_message", params[2]));
        	postParams.add(new BasicNameValuePair("my_gender", params[3]));
        	post.setEntity(new UrlEncodedFormEntity(postParams));
            HttpResponse response = _client.execute(post);
        } catch (Exception e) {
        	Log.e("RPC","Exception levée", e);
        }
        
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(_progress.isShowing()) _progress.dismiss();
        //Toast.makeText(this.screen, "FINI", Toast.LENGTH_SHORT).show();
        _activity.messageSent();
    }
    
}