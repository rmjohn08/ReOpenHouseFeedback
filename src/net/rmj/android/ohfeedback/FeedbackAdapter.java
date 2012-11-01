package net.rmj.android.ohfeedback;

import java.util.ArrayList;

import net.rmj.android.ohfeedback.model.Questionaire;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class FeedbackAdapter extends ArrayAdapter<Questionaire>{
	//ArrayAdapter<Questionaire>
   
    ArrayList<Questionaire> myQuestions;
    int resLayout;
    Context context;

    public FeedbackAdapter(Context context, int textViewResourceId, ArrayList<Questionaire> myQuestions) {
        super(context, textViewResourceId, myQuestions);
        this.myQuestions = myQuestions;
        resLayout = textViewResourceId;
        this.context = context;
    }
    
    protected Questionaire getRowModel(int indx) {
    	OpenHouseLocationFeedback ac = (OpenHouseLocationFeedback)context;
    	 ListAdapter la = ac.activityList().getAdapter();
    	 Log.i(OhConstants.OH_TAG,"ListAdapter count::" + la.getCount());
    	 return (Questionaire)la.getItem(indx);
    	 
    	//return (RatingRowModel)).getItem(indx);
    			//(ac.activityList()).getAdapter().getItem(indx);
    }
    
    /**
     * finds the questionaire by the _id
     * @param qid
     * @return
     */
    protected Questionaire findRowModel(long qid) {
    	
    	for (Questionaire q : myQuestions) {
    		if (q.getQuestionId() == qid) return q;
    		
    	}
    	return null;
    	
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {    View row;
        row = convertView;
        Log.i(OhConstants.OH_TAG,"Curr position "+ position);
        //the ViewWrapper to access the views in the row
        if (row!=null) {
        	Questionaire q = myQuestions.get(position);
        	Log.i(OhConstants.OH_TAG,"Row is not null... should check the contents");
        	RatingViewWrapper wrap = (RatingViewWrapper)row.getTag();
        	
        	if (wrap!=null) {
        		Log.i(OhConstants.OH_TAG,"Rating in wrapper::" + wrap.getRatingBar().getRating() + " text::" + wrap.getTextView().getText());
        	}
        	//RatingBar rb2 = (RatingBar)row.findViewById(R.id.ratingBar1);
        	//wrap.getRatingBar().setRating(rb2.getRating());
        	//Log.i(OhConstants.OH_TAG,"After swapping ::" + wrap.getRatingBar().getRating() + " text::" + wrap.getTextView().getText());
        	
        	// shouldnt continue
        	
        	wrap.getRatingBar().setRating(q.getRating());
        	wrap.getRatingBar().setTag(new Long(q.getQuestionId()));
        	wrap.getTextView().setText(q.getQuestion());
        	Log.i(OhConstants.OH_TAG,"Rating in Questionaire::" + q.getRating() + " question::" + q.getQuestion());
        	return row;
        }
        RatingViewWrapper wrapper;
        RatingBar rb;
        Questionaire q = myQuestions.get(position);
        if (q!=null) {
        	Log.i(OhConstants.OH_TAG,"Question::"+q.getQuestion());
        	LayoutInflater ll = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = ll.inflate(resLayout, parent, false);
            wrapper = new RatingViewWrapper(row);
            wrapper.getTextView().setText(q.getQuestion());
            
            rb = wrapper.getRatingBar();
            row.setClickable(true);
            row.setFocusable(true);
            
            row.setTag(wrapper);  // <=== saving the actual wrapper. 
            
            rb.setTag(new Long(q.getQuestionId()));
            
            rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					
					Log.i(OhConstants.OH_TAG, "Rating changed::" + ratingBar.getRating() + " rating::" +  rating);
					try {
						if (fromUser) {
							
							Questionaire qn = findRowModel(((Long)ratingBar.getTag()).longValue());
							if (qn!=null) qn.setRating(rating);
						}
					} catch (Exception ex) {
						Log.e(OhConstants.EXCEPTION, "Exception occuring");
						ex.printStackTrace();
						
					}
				}
            	
            });
            
           
        }
        return row;
       
    }
    
    
   
    
}

