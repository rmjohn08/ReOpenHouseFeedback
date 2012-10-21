package net.rmj.android.ohfeedback;

import java.util.ArrayList;

import net.rmj.android.ohfeedback.model.Location;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SearchLocationAdapter extends ArrayAdapter<Location>{

	
    View row;
    ArrayList<Location> locations;
    // the id of the layout that will be used for each row
    int resLayout;
    Context context;

    public SearchLocationAdapter(Context context, int textViewResourceId, ArrayList<Location> myQuestions) {
        super(context, textViewResourceId, myQuestions);
        this.locations = myQuestions;
        resLayout = textViewResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        row = convertView;
        if(row == null)
        {   // inflate our custom layout. resLayout == R.layout.row_team_layout.xml
            LayoutInflater ll = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = ll.inflate(resLayout, parent, false);
            
            // from here handle click on item
            row.setClickable(true);
            row.setFocusable(true);
            
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
            
            
        }

        Location item = locations.get(position); // Produce a row for each Question.
        if(item != null)
        {   // Find our widgets and populate them with the Team data.
            TextView myQuestion = (TextView) row.findViewById(R.id.resultText);
            //RatingBar myRating = (RatingBar)row.findViewById(R.id.ratingBar1);
            if(myQuestion != null)
                myQuestion.setText(item.getAddress());
            
            //rating).setText("Wins: " + String.valueOf(item.getTeamWins()));
            Button btnView = (Button) row.findViewById(R.id.btnLocDetail);
            final long locId=item.getLocationId();
            btnView.setOnClickListener(new OnClickListener() {
            		@Override
            		public void onClick(View view) {
            			// open up the new intent to see the detail about the location
            			Intent locIntent = new Intent(context,LocationDetailActivity.class);
            			locIntent.putExtra(OhConstants.LOCATION_ID_NAME, locId);
            			
            			context.startActivity(locIntent);
            			
            		}
            });
            
        }
        return row;
    }
}

