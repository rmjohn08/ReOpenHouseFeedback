package net.rmj.android.ohfeedback.dataaccess;

import java.util.ArrayList;
import java.util.List;

import net.rmj.android.ohfeedback.OhConstants;
import net.rmj.android.ohfeedback.model.Feedback;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.content.Context;
import android.util.Log;

public class TestDaoUtil {

	public static void testQuestionDao(Context context)
	{
		QuestionsFeedbackDao dao = new QuestionsFeedbackDao(context);
	
		try {
	
		dao.openDatabase();
		//dao.testQuestionDao();
		dao.testQuestionLocation();
		
		}  catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error testing dao");
			ex.printStackTrace();
		} finally {
			dao.closeDatabase();
		}
		
	}
	
	public static void testLocationDao(Context context)
	{
		LocationDao dao = new LocationDao(context);
	
		try {
	
		dao.openDatabase();
		//dao.testQuestionDao();
		dao.testLocationDao();
		
		}  catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error testing location dao");
			ex.printStackTrace();
		} finally {
			dao.closeDatabase();
		}
		
	}

	public static void testFeedbackDao(Context context) {
		QuestionsFeedbackDao qdao = new QuestionsFeedbackDao(context);
		qdao.openDatabase();
		List<Questionaire> qf =  qdao.getLocationQuestions(1);
		qdao.closeDatabase();
		long locId = 1;
		
		List<Feedback> flist = new ArrayList<Feedback>();
		for (Questionaire q : qf) {
			Feedback f = new Feedback();
			if (q.getType().equals(OhConstants.TEXT_TYPE))
				f.setResponseText(q.getText());
			else if(q.getType().equals(OhConstants.NUMBER_TYPE));
				f.setResponseNo(q.getRating());
			
			f.setLocationId(locId);
			f.setQuestionId(q.getQuestionId());
			flist.add(f);
			
		}
		
		FeedbackDao fdao = new FeedbackDao(context);
		fdao.openDatabase();
		try {
			fdao.beginTransaction();
			for (Feedback fe : flist) {
				fdao.addFeedback(fe);
				
			}
			fdao.setTransactionComplete();
			
		} catch(Exception ex) {
			ex.printStackTrace();
			
		} finally {
			fdao.endTransaction();
		}
		
	}
	
}
