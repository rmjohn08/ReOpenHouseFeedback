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
						if (!fromUser) {
							//
							//Questionaire qn = findRowModel(((Long)ratingBar.getTag()).longValue());
							//if (qn!=null) {
							//	ratingBar.setRating(qn.getRating());
							//}
						} else {
							//
							Questionaire qn = findRowModel(((Long)ratingBar.getTag()).longValue());
							if (qn!=null) qn.setRating(rating);
						}
					} catch (Exception ex) {
						Log.e(OhConstants.EXCEPTION, "Exception occuring");
						ex.printStackTrace();
						
					}
				}
            	
            });
            
            return row;
        }
        
        if(row == null)
        {   // inflate our custom layout. resLayout == R.layout.row_team_layout.xml
        	Log.i(OhConstants.OH_TAG, "Row is null");
        	LayoutInflater ll = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = ll.inflate(resLayout, parent, false);
            
            // from here handle click on item
            row.setClickable(true);
            row.setFocusable(true);
            ////sets the wrapper with the current row
            wrapper= new RatingViewWrapper(row);
            
            Questionaire model = getRowModel(position);
            wrapper.getTextView().setText(model.getQuestion());
            wrapper.getRatingBar().setRating(model.getRating());
            
            row.setTag(wrapper);
            rb = wrapper.getRatingBar();
            rb.setTag(new Integer(position));
            
            rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					Log.i(OhConstants.OH_TAG, "Rating   changed::" + ratingBar.getRating() + " rating::" +  rating);
					// TODO Auto-generated method stub
					/* Integer myPosition = (Integer)ratingBar.getTag();
					if (myPosition!=null) {
						Questionaire model = getRowModel(myPosition);
						model.setRating(rating);
						LinearLayout parent = (LinearLayout)ratingBar.getParent();
						TextView label = (TextView)parent.findViewById(R.id.question);
						label.setText(model.getText());
						
						Log.i(OhConstants.OH_TAG, myPosition +" ChangeListener for "+model.getQuestion() + " mr::" + model.getRating() + " rb::" +ratingBar.getRating() + " mQ:" +model.getQuestion());
					} else {
						Log.i(OhConstants.OH_TAG,"myPosition has resulted in null ");
					}*/
				}
            	
            });
            
            //Questionaire item = myQuestions.get(position); // Produce a row for each Question.
            //if(item != null)
            //{  
            	//wrapper.getTextView().setText(item.getQuestion());
            	//wrapper.getRatingBar().setRating(item.getRating());
            	
            	
            //}
            
        } else {
        	//RatingBar rq = (RatingBar)row.findViewById(R.id.ratingBar1);
        	//TextView tv = (TextView)row.findViewById(R.id.question);
        	//Log.i(OhConstants.OH_TAG, "RatingBar else r::"+rq.getRating() + " q:" +tv.getText() );
        	//Questionaire model = findRowModel((Long)rq.getTag()); //getRowModel(position);
        	wrapper = (RatingViewWrapper)row.getTag();
        	if(wrapper!=null){
	        	rb = wrapper.getRatingBar();
	        	
	        	//model.setRating(rq.getRating());
	        	try {
	        		Log.i(OhConstants.OH_TAG, "RowModel else r::"+wrapper.getRatingBar().getRating() + " q::" + wrapper.getTextView().getText());
	        	} catch(Exception ex) {	Log.i(OhConstants.OH_TAG,"Something went wrong printing wrapper... "); }
	        	
        	}
        	
        }

        //rb.setTag(position,item);
        
        /* Questionaire model = getRowModel(position);
        wrapper.getTextView().setText(model.getQuestion());
        wrapper.getRatingBar().setRating(model.getRating());
        rb.setTag(new Integer(position)); */
        return row;
    }
    
    
    public View getViewBAD(int position, View convertView, ViewGroup parent)
    { View row;
        row = convertView;
        Log.i(OhConstants.OH_TAG,"Curr position "+ position);
        //the ViewWrapper to access the views in the row
        RatingViewWrapper wrapper;
        RatingBar rb;
        
        if(row == null)
        {   // inflate our custom layout. resLayout == R.layout.row_team_layout.xml
        	Log.i(OhConstants.OH_TAG, "Row is null");
        	LayoutInflater ll = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = ll.inflate(resLayout, parent, false);
            
            // from here handle click on item
            row.setClickable(true);
            row.setFocusable(true);
            //sets the wrapper with the current row
            wrapper= new RatingViewWrapper(row);
            row.setTag(wrapper);
            rb = wrapper.getRatingBar();
            rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					// TODO Auto-generated method stub
					//Integer myPosition = (Integer)ratingBar.getTag();
					//Questionaire model = getRowModel(myPosition);
					//model.setRating(rating);
					
					Questionaire model = findRowModel((Long)ratingBar.getTag());
					model.setRating(rating);
					
					Log.i(OhConstants.OH_TAG,model.getQuestion() + " mr::" + model.getRating() + " rb::" +ratingBar.getRating() + " mQ:" +model.getQuestion());
					
				}
            	
            });
            
            Questionaire item = myQuestions.get(position); // Produce a row for each Question.
            if(item != null)
            {   // Find our widgets and populate them with the Team data.
                /* TextView myQuestion = (TextView) row.findViewById(R.id.question);
                final RatingBar myRating = (RatingBar)row.findViewById(R.id.ratingBar1);
                if(myQuestion != null)
                    myQuestion.setText(item.getQuestion());
                if(myRating != null) {
                    myRating.setRating(item.getRating());
                    myRating.setTag(new Long(item.getQuestionId()));
                }
              	*/
            	//wrapper.getTextView().setText(item.getQuestion());
            	//wrapper.getRatingBar().setRating(item.getRating());
            	
            	
            }
                   
            /*row.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				// TODO Auto-generated method stub
    				//TestDaoUtil.testQuestionDao(v.getContext());
    				//TestDaoUtil.testLocationDao(v.getContext());
    				TestDaoUtil.testFeedbackDao(v.getContext());
    			}
            	
            	
            	
            }); */
            // end click on item
            Questionaire model = findRowModel(item.getQuestionId()); //getRowModel(position);
            
            wrapper.getTextView().setText(model.getQuestion());
            wrapper.getRatingBar().setRating(model.getRating());
            rb.setTag(model.getQuestionId());  //new Integer(position));
            
            
        } else {
        	RatingBar rq = (RatingBar)row.findViewById(R.id.ratingBar1);
        	TextView tv = (TextView)row.findViewById(R.id.question);
        	Log.i(OhConstants.OH_TAG, "RatingBar else r::"+rq.getRating() + " q:" +tv.getText() );
        	Questionaire model = findRowModel((Long)rq.getTag()); //getRowModel(position);
        	//wrapper = (RatingViewWrapper)row.getTag();
        	//rb = rq;
        	
        	//model.setRating(rq.getRating());
        	
        	Log.i(OhConstants.OH_TAG, "RowModel else r::"+model.getRating() + " q::" + model.getQuestion());
        	
        }

        //rb.setTag(position,item);
        
        return row;
    }
    
}

