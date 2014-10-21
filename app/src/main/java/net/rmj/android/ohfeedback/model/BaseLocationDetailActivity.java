package net.rmj.android.ohfeedback.model;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by Ronaldo on 10/8/2014.
 */
public abstract class BaseLocationDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
