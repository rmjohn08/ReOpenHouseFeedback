package net.rmj.android.ohfeedback;

import java.util.ArrayList;

import net.rmj.android.ohfeedback.model.Questionaire;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


public class QuestionAdapter extends ArrayAdapter<Questionaire>{

	
    View row;
    ArrayList<Questionaire> myQuestions;
    int resLayout;
    Context context;

    public QuestionAdapter(Context context, int textViewResourceId, ArrayList<Questionaire> myQuestions) {
        super(context, textViewResourceId, myQuestions);
        this.myQuestions = myQuestions;
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

        Questionaire item = myQuestions.get(position); // Produce a row for each Question.
        if(item != null)
        {   // Find our widgets and populate them with the Team data.
            TextView myQuestion = (TextView) row.findViewById(R.id.questionText);
            CheckBox cb = (CheckBox)row.findViewById(R.id.chkQuestion);
            cb.setTag(new Long(item.getQuestionId()));
            cb.setChecked(item.isSelected());

            if(myQuestion != null)
                myQuestion.setText(item.getQuestion());
           
        }
        return row;
    }
}

