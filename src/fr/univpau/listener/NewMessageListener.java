package fr.univpau.listener;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import fr.univpau.rpc.SendMessageRPC;
import fr.univpau.util.MyPreferenceManager;
import fr.univpau.waam.NewMessageActivity;
import fr.univpau.waam.R;

public class NewMessageListener implements OnClickListener {

	EditText 				_editTitle;
	NewMessageActivity 		_context;
	MyPreferenceManager		_preferenceManager;
	
	public NewMessageListener() {
		
	}
	
	public NewMessageListener(NewMessageActivity context, EditText editTitle) {
		_editTitle = editTitle;
		_context = context;
		_preferenceManager = new MyPreferenceManager((Activity) _context);
	}

	@Override
	public void onClick(View v) {
		String my_latitude = Double.toString(_preferenceManager.getLat());
		String my_longitude = Double.toString(_preferenceManager.getLng());
		String my_message = ((EditText) ((NewMessageActivity) _context).findViewById(R.id.editText)).getText().toString();
		String my_gender = Double.toString(_preferenceManager.getGender());
		new SendMessageRPC((NewMessageActivity) _context).execute(new String[]{my_latitude, my_longitude, my_message, my_gender});
	}

}
