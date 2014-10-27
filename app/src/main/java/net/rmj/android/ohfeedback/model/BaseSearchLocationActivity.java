package net.rmj.android.ohfeedback.model;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
//import android.support.v4.app.FragmentActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import net.rmj.android.ohfeedback.LocationDetailActivity;
import net.rmj.android.ohfeedback.OhConstants;
import net.rmj.android.ohfeedback.R;


/**
 * Created by Ronaldo on 10/7/2014.
 */
public abstract class BaseSearchLocationActivity extends ActionBarActivity implements PopupMenu.OnMenuItemClickListener {

    protected long locationId;

    public void showPopup(View v) {

        PopupMenu pop = new PopupMenu(this, v);
        pop.setOnMenuItemClickListener(this);

        MenuInflater inflater = pop.getMenuInflater();
        inflater.inflate(R.menu.actions_menu,pop.getMenu());

        // since the parent of the menu view is a layout
        // we cast it to its respective type which is linear layout
        // that way we can get access to the tag and to the locationId
        LinearLayout ly = (LinearLayout)v.getParent();
        TextView text = (TextView)ly.findViewById(R.id.resultText);
        locationId = (Long)text.getTag();
        pop.show();

    }

    // this is for the pop menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view_detail:
                //Toast.makeText(this, "Current location " + locationId, Toast.LENGTH_SHORT).show();
                // open up the new intent to see the detail about the location
                Intent locIntent = new Intent(this,LocationDetailActivity.class);
                locIntent.putExtra(OhConstants.LOCATION_ID_NAME, locationId);

                this.startActivity(locIntent);
                return true;

            default:
                return false;
        }

    }


}
