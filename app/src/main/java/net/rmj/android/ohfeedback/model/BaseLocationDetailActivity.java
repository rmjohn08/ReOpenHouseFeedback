package net.rmj.android.ohfeedback.model;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.rmj.android.ohfeedback.R;

/**
 * Created by Ronaldo on 10/8/2014.
 */
public abstract class BaseLocationDetailActivity extends ActionBarActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setLogo(R.drawable.ic_launcher);

        }
    }

    protected Toolbar getToolBar() { return toolbar;}

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
