package fr.univpau.listener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.univpau.entity.MessageEntity;

public class ClickMessageListener implements OnItemClickListener {

	ListView _list;
	Context _context;
	
	public ClickMessageListener(Context context, ListView list) {
		_list = list;
		_context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MessageEntity msg =  (MessageEntity) _list.getItemAtPosition(position);
		double latitude = msg.getLocation()[0];
		double longitude = msg.getLocation()[1];
		String uriBegin = "geo:" + latitude + "," + longitude;
		String query = latitude + "," + longitude;
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		_context.startActivity(intent);
	}

}
