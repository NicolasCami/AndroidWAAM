package fr.univpau.waam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import fr.univpau.listener.NewMessageListener;
import fr.univpau.listener.TextCountListener;

public class NewMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newmessage);
		
		EditText editText = (EditText) findViewById(R.id.editText);
		TextView text = (TextView) findViewById(R.id.textCount);
		TextCountListener textCountListener = new TextCountListener(text);
		editText.addTextChangedListener(textCountListener);
		
        Button btnNewMessage = (Button) findViewById(R.id.buttonSend);
        NewMessageListener newMessageListener = new NewMessageListener(this, (EditText) findViewById(R.id.editText));
        btnNewMessage.setOnClickListener(newMessageListener);
	}
	
	public void messageSent() {
		Toast.makeText(this, "Message envoyé !", Toast.LENGTH_SHORT).show();
		finish();
	}
	
}
