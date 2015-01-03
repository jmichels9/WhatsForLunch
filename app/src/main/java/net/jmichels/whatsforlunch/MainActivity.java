package net.jmichels.whatsforlunch;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

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
    private DiningHall mDiningHall;
    private TabbedFragment mTabbedFragment;
    private boolean firstRun;

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

        firstRun = true;

        // Reset the dates for the picker
        getPreferences(MODE_PRIVATE).edit().remove("menuYear").remove("menuMonth").remove("menuDay").commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*if(firstRun) {
            setTitle(mTitle);
            mDiningHallFragment.fetchMenu();
            firstRun = false;
        }*/
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(!Helpers.diningHalls[position].getName().equals(mTitle)) {
            mDiningHall = Helpers.diningHalls[position];
            mTabbedFragment = TabbedFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.container, mTabbedFragment).commit();
        }
    }

    public void onSectionAttached(DiningHall diningHall) {
        mTitle = diningHall.getName();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DiningHall getDiningHall() {
        return mDiningHall;
    }

    public TabbedFragment getTabbedFragment() { return mTabbedFragment; }

    public CharSequence getDiningHallTitle() {
        return mTitle;
    }
}
