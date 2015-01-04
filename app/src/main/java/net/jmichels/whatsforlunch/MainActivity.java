package net.jmichels.whatsforlunch;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.DatePicker;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title.
     */
    private CharSequence mTitle;
    private DiningHallFragment mDiningHallFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,drawer);
        drawer.setDrawerListener(new MainDrawerListener());

        // Reset the dates for the picker
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        int diningHallPosition = settings.getInt(Helpers.DINING_HALL_POSITION, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().commit();
        editor.putInt(Helpers.DINING_HALL_POSITION, diningHallPosition);
        editor.putBoolean(Helpers.FIRST_RUN,true);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set the subtitle
        Calendar rightNow = Calendar.getInstance();
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        final int year = settings.getInt(Helpers.MENU_YEAR, rightNow.get(Calendar.YEAR));
        final int month = settings.getInt(Helpers.MENU_MONTH, rightNow.get(Calendar.MONTH));
        final int day = settings.getInt(Helpers.MENU_DAY, rightNow.get(Calendar.DAY_OF_MONTH));
        getSupportActionBar().setSubtitle((month+1) + "/" + day + "/" + year);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(!Helpers.diningHalls[position].getName().equals(mTitle)) {
            mDiningHallFragment = DiningHallFragment.newInstance(Helpers.diningHalls[position]);
            fragmentManager.beginTransaction().replace(R.id.container, mDiningHallFragment).commit();
            getPreferences(MODE_PRIVATE).edit().putInt(Helpers.DINING_HALL_POSITION, position).commit();
        }
    }

    public void onSectionAttached(DiningHall diningHall) {
        mTitle = diningHall.getName();
        setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_pick_date) {
            showDatePicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DiningHallFragment getDiningHallFragment() { return mDiningHallFragment; }

    public CharSequence getDiningHallTitle() {
        return mTitle;
    }

    private void showDatePicker() {
        // Get the last input date or use today
        Calendar rightNow = Calendar.getInstance();
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        final int year = settings.getInt(Helpers.MENU_YEAR,rightNow.get(Calendar.YEAR));
        final int month = settings.getInt(Helpers.MENU_MONTH, rightNow.get(Calendar.MONTH));
        final int day = settings.getInt(Helpers.MENU_DAY, rightNow.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // save the input date in shared prefs
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt(Helpers.MENU_YEAR, year);
                        editor.putInt(Helpers.MENU_MONTH, monthOfYear);
                        editor.putInt(Helpers.MENU_DAY, dayOfMonth);

                        // Commit the edits!
                        editor.commit();

                        getSupportActionBar().setSubtitle((monthOfYear+1) + "/" + dayOfMonth + "/" + year);

                        getDiningHallFragment().fetchMenu();
                    }
                }, year, month, day);
        dpd.show();
    }
}
