package net.jmichels.whatsforlunch;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, DiningHallFragment.newInstance(Helpers.diningHalls[position]))
                .commit();
    }

    public void onSectionAttached(DiningHall diningHall) {
        mTitle = diningHall.getName();
        // Get menu and update the display of it?
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DiningHallFragment extends Fragment {
        /**
         * The fragment argument representing the dining hall for this
         * fragment.
         */
        private static final String ARG_DINING_HALL = "dining_hall";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DiningHallFragment newInstance(DiningHall diningHall) {
            DiningHallFragment fragment = new DiningHallFragment();
            Bundle args = new Bundle();
            args.putSerializable(ARG_DINING_HALL, diningHall);
            fragment.setArguments(args);
            return fragment;
        }

        public DiningHallFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            String response = null;
            String content = "";

            DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);

            try {
                response = new FetchMenuTask().execute("1","15",diningHall.getId().toString()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                if(response != null && response.toString().length() > 0) {
                    content = response.toString();
                } else {
                    content = "Sorry, but there was a problem fetching the menu data.";
                }
            }

            TextView diningHallTextView = (TextView)rootView.findViewById(R.id.diningHallTextView);
            diningHallTextView.setText(Html.fromHtml(content));

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);
            ((MainActivity) activity).onSectionAttached(diningHall);
        }

        private class FetchMenuTask extends AsyncTask<String,Integer,String> {

            @Override
            protected String doInBackground(String... params) {
                final String menuUrl = "http://menu.unl.edu/";

                HttpResponse response = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(menuUrl);

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("Month", params[0]));
                pairs.add(new BasicNameValuePair("Day", params[1]));
                pairs.add(new BasicNameValuePair("gComplex",params[2]));

                try {
                    // execute the POST request
                    post.setEntity(new UrlEncodedFormEntity(pairs));
                    response = client.execute(post);

                    // transform the response into a string
                    BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line);
                    }

                    // Get just the menu from the response
                    Document doc = Jsoup.parse(total.toString());
                    Element menuSection = doc.select("div#cphMain_pnlBreakfastItems").first().parent();

                    return menuSection.toString();
                } catch(ClientProtocolException e) {
                    e.printStackTrace();
                    return "There was a problem with the client protocol.";
                } catch(IOException e) {
                    e.printStackTrace();
                    return "There was an issue with the POST request";
                }
            }
        }
    }
}
