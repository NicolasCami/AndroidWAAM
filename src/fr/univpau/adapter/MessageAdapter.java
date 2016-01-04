package fr.univpau.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.univpau.entity.MessageEntity;
import fr.univpau.util.Gender;
import fr.univpau.util.MyPreferenceManager;
import fr.univpau.waam.R;

public class MessageAdapter extends ArrayAdapter<MessageEntity> {
	
	List<Boolean> 			itemAnimated;
	MyPreferenceManager		_preferenceManager;
	
	private final int 		COLOR_DISTANCE_R = 41;
	private final int 		COLOR_DISTANCE_G = 144;
	private final int 		COLOR_DISTANCE_B = 234;
	private final int		COLOR_ROW_EVEN = Color.argb(0, 0, 0, 0);
	private final int		COLOR_ROW_ODD = Color.argb(10, 0, 0, 0);

    public MessageAdapter(Context context, int textViewResourceId, List<MessageEntity> objects) {
    	super(context, textViewResourceId, objects);
    	itemAnimated = new ArrayList<Boolean>();
    	_preferenceManager = new MyPreferenceManager((Activity) context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View rowView = null;
    	
    	if(convertView == null) {
    		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.itemlist_message, parent, false);
    	}
    	else {
    		rowView = convertView;
    	}
    	
    	MessageEntity message = getItem(position);
        
        TextView textContent = (TextView) rowView.findViewById(R.id.textContent);
        textContent.setText(message.getContent());
        
        TextView textDate = (TextView) rowView.findViewById(R.id.textDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm");
        String dateString = sdf.format(message.getDate().getTime());
        textDate.setText(dateString);
        
        TextView textDistance = (TextView) rowView.findViewById(R.id.textDistance);
        textDistance.setText(message.getDistance() + " m");
        
        ImageView imageGender = (ImageView) rowView.findViewById(R.id.imageGender);
        if(message.getGender() == Gender.MALE) {
        	imageGender.setImageResource(R.drawable.male);
        }
        else if(message.getGender() == Gender.FEMALE) {
        	imageGender.setImageResource(R.drawable.female);
        }
        
        // distance color gradient
        int colorWeightR = COLOR_DISTANCE_R - ((((int) message.getDistance()) * COLOR_DISTANCE_R) / (_preferenceManager.getDistance()));
        int colorWeightG = COLOR_DISTANCE_G - ((((int) message.getDistance()) * COLOR_DISTANCE_G) / (_preferenceManager.getDistance()));
        int colorWeightB = COLOR_DISTANCE_B - ((((int) message.getDistance()) * COLOR_DISTANCE_B) / (_preferenceManager.getDistance()));
        Log.i("WAAM", colorWeightR + " / " + colorWeightG + " / " + colorWeightB);
        textDistance.setTextColor(Color.rgb(colorWeightR, colorWeightG, colorWeightB));
        
        // row background (odd or even)
        if(position%2 == 0) {
        	rowView.setBackgroundColor(COLOR_ROW_EVEN);
        }
        else {
        	rowView.setBackgroundColor(COLOR_ROW_ODD);
        }
        
        //if(!itemAnimated.get(position)) {
            AnimationSet set = new AnimationSet(true);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(400);
            set.addAnimation(animation);

            rowView.setAnimation(set);
            //itemAnimated.set(position, true);
        //}

        return rowView;
    }
    
    public void resetAnimation() {
    	itemAnimated.clear();
    	for(int i=0; i<getCount(); i++) {
    		itemAnimated.add(false);
    	}
    }
	
}
