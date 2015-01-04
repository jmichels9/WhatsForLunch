package net.jmichels.whatsforlunch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 12/29/2014.
 */
public class FetchMenuTask extends AsyncTask<String,Integer,String[]> {

    boolean showDialog;
    ProgressDialog dialog;
    Activity activity;
    View view;
    int meal;

    public FetchMenuTask(Activity activity, View view, int meal) {
        this.view = view;
        this.meal = meal;
        this.activity = activity;
        if(activity == null) {
            showDialog = false;
            dialog = null;
        } else {
            showDialog = true;
            dialog = new ProgressDialog(activity);
        }
    }

    @Override
    protected void onPreExecute() {
        if(showDialog) {
            dialog.setMessage(Helpers.FETCHING_MENU);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected String[] doInBackground(String... params) {
        final String menuUrl = Helpers.CLIENT_URL;
        String[] combinedMenu = new String[3];

        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(menuUrl);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair(Helpers.CLIENT_PARAM_MONTH, params[0]));
        pairs.add(new BasicNameValuePair(Helpers.CLIENT_PARAM_DAY, params[1]));
        pairs.add(new BasicNameValuePair(Helpers.CLIENT_PARAM_COMPLEX, params[2]));

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

            Whitelist whitelist = new Whitelist();
            whitelist.addAttributes("div","id");
            whitelist.addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup",
                    "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "li",
                    "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub", "sup",
                    "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul");

            Cleaner cleaner = new Cleaner(whitelist);

            // Get just the menu from the cleaned up response
            Document doc = cleaner.clean(Jsoup.parse(total.toString()));
            Element breakfastMenuSection = doc.getElementById(Helpers.CLIENT_RESULT_BREAKFAST);
            Element lunchMenuSection = doc.getElementById(Helpers.CLIENT_RESULT_LUNCH);
            Element dinnerMenuSection = doc.getElementById(Helpers.CLIENT_RESULT_DINNER);

            if(breakfastMenuSection != null) {
                if(!breakfastMenuSection.toString().contains(Helpers.CLIENT_RESULT_UNAVAILABLE)) {
                    combinedMenu[0] = breakfastMenuSection.toString();
                } else {
                    combinedMenu[0] = Helpers.MENU_UNAVAILABLE_MESSAGE;
                }
            }

            if(lunchMenuSection != null) {
                if(!lunchMenuSection.toString().contains(Helpers.CLIENT_RESULT_UNAVAILABLE)) {
                    combinedMenu[1] = lunchMenuSection.toString();
                } else {
                    combinedMenu[1] = Helpers.MENU_UNAVAILABLE_MESSAGE;
                }
            }

            if(dinnerMenuSection != null) {
                if(!dinnerMenuSection.toString().contains(Helpers.CLIENT_RESULT_UNAVAILABLE)) {
                    combinedMenu[2] = dinnerMenuSection.toString();
                } else {
                    combinedMenu[2] = Helpers.MENU_UNAVAILABLE_MESSAGE;
                }
            }
        } catch(ClientProtocolException e) {
            e.printStackTrace();
            combinedMenu[0] = combinedMenu[1] = combinedMenu[2] = Helpers.MENU_CLIENT_ERROR;
        } catch(IOException e) {
            e.printStackTrace();
            combinedMenu[0] = combinedMenu[1] = combinedMenu[2] = Helpers.MENU_POST_ERROR;
        }

        return combinedMenu;
    }

    @Override
    protected void onPostExecute(final String[] success) {
        String[] content = new String[3];
        if(success != null) {
            if(success[0] != null && success[0].toString().length() > 0) {
                content[0] = success[0];
            } else {
                content[0] = Helpers.MENU_UNAVAILABLE_MESSAGE;
            }

            if(success[1] != null && success[1].toString().length() > 0) {
                content[1] = success[1];
            } else {
                content[1] = Helpers.MENU_UNAVAILABLE_MESSAGE;
            }

            if(success[2] != null && success[2].toString().length() > 0) {
                content[2] = success[2];
            } else {
                content[2] = Helpers.MENU_UNAVAILABLE_MESSAGE;
            }
        } else {
            content[0] = content[1] = content[2] = Helpers.MENU_UNAVAILABLE_MESSAGE;
        }

        SharedPreferences settings = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Helpers.CURRENT_BREAKFAST, content[0]);
        editor.putString(Helpers.CURRENT_LUNCH, content[1]);
        editor.putString(Helpers.CURRENT_DINNER, content[2]);
        editor.commit();

        TextView diningHallTextView = (TextView)view.findViewById(R.id.mealTextView);
        diningHallTextView.setText(Html.fromHtml(content[meal]));

        if (showDialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}