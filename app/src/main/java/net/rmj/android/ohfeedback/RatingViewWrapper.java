package net.rmj.android.ohfeedback;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatingViewWrapper {

	View base;
	RatingBar rateBar=null;
	TextView text = null;
	
	public RatingViewWrapper(View base) {
		this.base=base;
	}
	
	public RatingBar getRatingBar() {
		if (rateBar==null) {
			rateBar = (RatingBar)base.findViewById(R.id.ratingBar1);
		}
		
		return rateBar;
	}
	
	public TextView getTextView() {
		if (text==null) {
			text = (TextView)base.findViewById(R.id.question);
		}
		return text;
	}
	
}
