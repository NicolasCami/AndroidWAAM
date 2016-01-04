package fr.univpau.listener;

import fr.univpau.waam.MainActivity;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class MessageScrollListener implements OnScrollListener {
	
	private int 				_previousLastItem = 0;
	private MainActivity		_activity;

	public MessageScrollListener(MainActivity activity) {
		this._activity = activity;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int lastItem = firstVisibleItem + visibleItemCount;
		if(lastItem == totalItemCount) {
			if(_previousLastItem != lastItem){
				//Toast.makeText(view.getContext(), "Fin de liste", Toast.LENGTH_SHORT).show();
				_activity.messageLazyLoad();
				_previousLastItem = lastItem;
			}
		}
	}
	
	public void setPreviousLastItem(int i) {
		_previousLastItem = i;
	}

}
