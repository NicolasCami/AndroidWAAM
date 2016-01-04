package fr.univpau.waam;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.univpau.adapter.MessageAdapter;
import fr.univpau.entity.MessageEntity;
import fr.univpau.listener.ClickMessageListener;
import fr.univpau.listener.MessageScrollListener;
import fr.univpau.listener.MyLocationListener;
import fr.univpau.rpc.MessageRPC;
import fr.univpau.util.MyPreferenceManager;

public class MainActivity extends Activity {
	
	private MessageAdapter 			_messageAdapter;
	private List<MessageEntity> 	_messageList;
	private List<MessageEntity> 	_messageListToShow;
	private LocationManager 		_locationManager;
	private MyLocationListener 		_locationListener;
	private MyPreferenceManager 	_preferenceManager;
	private ProgressDialog 			_progress;
	private MessageScrollListener	_scrollListener;
	private TextView				_bottomText;
	private TextView				_notificationText;
	private ListView 				_messageListView;
	
	// set to true when receive the first event of onLocationChanged from the LocationListener
	private boolean 				_locationSet = false;
	private int						_lazyLoadEnd = 0;
	private boolean					_refreshList = true;
	
	private static final long		LOCATION_UPDATE_TIME = 1000;
	private static final float		LOCATION_UPDATE_DISTANCE = 1;
	private static int				MESSAGE_LAZYLOAD_NUMBER = 15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("");
		getActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_main);
		
		_messageList = new ArrayList<MessageEntity>();
		_messageListToShow = new ArrayList<MessageEntity>();
		_preferenceManager = new MyPreferenceManager(this);
		_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		_locationListener = new MyLocationListener(this);
        
		_messageListView = (ListView) findViewById(R.id.listMessage);
        _scrollListener = new MessageScrollListener(this);
        _messageAdapter = new MessageAdapter(this, R.layout.itemlist_message, _messageListToShow);
        _messageListView.setAdapter(_messageAdapter);
        _messageListView.setOnScrollListener(_scrollListener);
        
        _messageListView.setClickable(true);
        ClickMessageListener clickMessageListener = new ClickMessageListener(this, _messageListView);
        _messageListView.setOnItemClickListener(clickMessageListener);
        
		_progress = new ProgressDialog(this);
        _progress.setTitle("Veuillez patienter");
        _progress.setMessage("Récupération de votre position...");
        _progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);      
        _progress.show();
        
        _bottomText = (TextView) findViewById(R.id.bottomText);
        
		if(!isLocationAvailable()) {
			AlertDialog.Builder errorBuilder = new AlertDialog.Builder(this);
			errorBuilder.setTitle("Erreur de localisation");
			errorBuilder.setMessage("Veuillez activer la localisation et redémarrer l'application.");
			errorBuilder.setCancelable(true);
			errorBuilder.setPositiveButton("J'ai compris",
                new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    finish();
                    System.exit(0);
                }
            });

            AlertDialog errorDialog = errorBuilder.create();
            errorDialog.show();
		}
		else {
			onLocationChanged(getLocation());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
	    final View menu_hotlist = menu.findItem(R.id.action_reload).getActionView();
	    _notificationText = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
	    _notificationText.setOnClickListener(new TextView.OnClickListener() {  
	    	public void onClick(View v) {
	    		_refreshList = true;
	    		loadMessage(true);
            }
        });
	    ((ImageView) menu_hotlist.findViewById(R.id.hotlist_bell)).setOnClickListener(new ImageView.OnClickListener() {  
	    	public void onClick(View v) {
	    		_refreshList = true;
	    		loadMessage(true);
            }
        });
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		//Log.i("WAAM", "CLICK OPTION " + id);
		switch(id) {
			case android.R.id.home:
				_messageListView.smoothScrollToPosition(0);
				break;
			case R.id.action_settings:
				Intent intent = new Intent();
	        	intent.setClass(this, PreferencesActivity.class);
	        	startActivity(intent);
				break;
			case R.id.action_reload:
				_refreshList = true;
				loadMessage(true);
				break;
			case R.id.action_new:
				Intent intentNew = new Intent();
				intentNew.setClass(this, NewMessageActivity.class);
				startActivity(intentNew);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void populateMessages(List<MessageEntity> result) {
		int previousSize = _messageList.size();
		_messageList.clear();
		_messageList.addAll(result);
		//Log.i("WAAM", "nouveaux messages");
		if(_refreshList || _preferenceManager.isAutoUpdate()) {
			_messageListToShow.clear();
			_lazyLoadEnd = 0;
			_scrollListener.setPreviousLastItem(0);
			//Log.i("WAAM", "taille liste " + _messageList.size());
			if(_preferenceManager.isLazyLoad()) {
				messageLazyLoad();
			}
			else {
				messageBrutLoad();
			}
			if(_messageListToShow.size()==0) {
				_bottomText.setText("aucun message");
			}
			else if(_messageListToShow.size()==1) {
				_bottomText.setText((_messageListToShow.size()) + " message affiché");
			}
			else {
				_bottomText.setText((_messageListToShow.size()) + " messages affichés");
			}
			if(!_preferenceManager.isAutoUpdate()) {
				_refreshList = false;
			}
			updateHotCount(0);
		}
		else {
			updateHotCount(_messageList.size()-previousSize);
		}
	}

	public void messageLazyLoad() {
		int previousEnd = _lazyLoadEnd + 1;
		_lazyLoadEnd += MESSAGE_LAZYLOAD_NUMBER;
		//Log.i("WAAM", previousEnd + " => " + _lazyLoadEnd);
		
		if(previousEnd >= _messageList.size()) {
			return;
		}
		
		if(previousEnd == 1) {
			previousEnd--;
		}
		
		if(_lazyLoadEnd >= _messageList.size()) {
			_lazyLoadEnd = _messageList.size() - 1;
		}
		if(previousEnd >= _messageList.size()) {
			previousEnd = _messageList.size() - 1;
		}
		
		if(previousEnd <= _lazyLoadEnd) {
			int begin = previousEnd;
			int end = _lazyLoadEnd;
			//Log.i("WAAM", begin + " => " + end);
			for(int i=begin; i<=end; i++) {
				//Log.i("WAAM", " => " + _messageList.get(i).getContent());
				_messageListToShow.add(_messageList.get(i));
			}
			//_messageListToShow.addAll(_messageList.subList(begin, end));
		}

		_messageAdapter.notifyDataSetChanged();
	}
	
	public void messageBrutLoad() {
		_lazyLoadEnd = _messageList.size() - 1;
		_messageListToShow.clear();
		_messageListToShow.addAll(_messageList);
		_messageAdapter.notifyDataSetChanged();
	}
	
	private boolean isNetworkAvailable() {
		boolean networkStatus = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return networkStatus;
	}
	
	private boolean isGPSAvailable() {
		boolean gpsStatus = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return gpsStatus;
	}
	
	private boolean isLocationAvailable() {
		return isGPSAvailable() || isNetworkAvailable();
	}
	
	public void onLocationChanged(Location loc) {
		if(loc != null) {
			if(_progress.isShowing()) _progress.dismiss();
			_preferenceManager.setLat((float) loc.getLatitude());
			_preferenceManager.setLng((float) loc.getLongitude());
	        if(_locationSet == false) {
	        	// first time we get an update
	        	loadMessage(true);
		        _locationSet = true;
	        }
	        else {
	        	loadMessage(false);
	        }
		}
		else {
			//Log.i("LOLOLOL", "location nul");
		}
	}
	
	public void loadMessage(boolean showMessage) {
		new MessageRPC(this, showMessage).execute();
	}
	
	public Location getLocation() {
		Location location = null;
		
	    try {
	        if(_locationManager!=null && _locationListener!=null && isLocationAvailable()) {
	            if (isNetworkAvailable()) {
	                _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, _locationListener);
                    location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	            }
	            else if(isGPSAvailable()) {
                    _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, _locationListener);
                    location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return location;
	}
	
	public void updateHotCount(final int new_hot_number) {
	    if(_notificationText == null) return;
        if(new_hot_number == 0)
        	_notificationText.setVisibility(View.INVISIBLE);
        else {
        	_notificationText.setVisibility(View.VISIBLE);
        	_notificationText.setText(Integer.toString(new_hot_number));
        }
	}
	
	
}
