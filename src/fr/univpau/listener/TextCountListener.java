package fr.univpau.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class TextCountListener implements TextWatcher {
	
	TextView _text;
	
	public static final int TEXT_LIMIT = 140;
	
	public TextCountListener(TextView text) {
		_text = text;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		_text.setText("Caractères restants : " + String.valueOf(TEXT_LIMIT - s.length()));
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}
