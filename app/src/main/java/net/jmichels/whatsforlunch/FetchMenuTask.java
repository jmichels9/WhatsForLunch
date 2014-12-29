package net.jmichels.whatsforlunch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 12/29/2014.
 */
public class FetchMenuTask extends AsyncTask<String,Integer,String> {

    boolean showDialog;
    ProgressDialog dialog;
    Context context;
    View rootView;

    public FetchMenuTask(Context context, View view) {
        this.rootView = view;
        this.context = context;
        if(context == null) {
            showDialog = false;
            dialog = null;
        } else {
            showDialog = true;
            dialog = new ProgressDialog(context);
        }
    }

    @Override
    protected void onPreExecute() {
        if(showDialog) {
            dialog.setMessage("Fetching Menu...");
            dialog.setCancelable(false);
            dialog.show();
        }
    }

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
            Elements combinedMenu = new Elements();
            Document doc = Jsoup.parse(total.toString());
            Element breakfastMenuSection = doc.getElementById("cphMain_pnlBreakfastItems");
            Element lunchMenuSection = doc.getElementById("cphMain_pnlLunchItems");
            Element dinnerMenuSection = doc.getElementById("cphMain_pnlDinnerItems");

            if(breakfastMenuSection != null) {
                if(!breakfastMenuSection.toString().contains("no menu is available")) {
                    combinedMenu.add(breakfastMenuSection);
                }
            }

            if(lunchMenuSection != null) {
                if(!lunchMenuSection.toString().contains("no menu is available")) {
                    combinedMenu.add(lunchMenuSection);
                }
            }

            if(dinnerMenuSection != null) {
                if(!dinnerMenuSection.toString().contains("no menu is available")) {
                    combinedMenu.add(dinnerMenuSection);
                }
            }

            if(combinedMenu == null || combinedMenu.size() <= 0 || combinedMenu.toString().length() <= 0) {
                return "Sorry, no menu is available at this time. Please try again!";
            } else {
                return combinedMenu.toString();
            }
        } catch(ClientProtocolException e) {
            e.printStackTrace();
            return "There was a problem with the client protocol.";
        } catch(IOException e) {
            e.printStackTrace();
            return "There was an issue with the POST request";
        }
    }

    @Override
    protected void onPostExecute(final String success) {
        if (showDialog && dialog.isShowing()) {
            dialog.dismiss();
        }

        String content = "";
        if(success != null && success.toString().length() > 0) {
            content = success.toString();
        } else {
            content = "Sorry, but there was a problem fetching the menu data.";
        }

        TextView diningHallTextView = (TextView)rootView.findViewById(R.id.diningHallTextView);
        diningHallTextView.setText(Html.fromHtml(content));
    }
}