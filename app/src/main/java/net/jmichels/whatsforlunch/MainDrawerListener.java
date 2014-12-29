package net.jmichels.whatsforlunch;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by Jay on 12/29/2014.
 */
public class MainDrawerListener implements DrawerLayout.DrawerListener {

    private CharSequence title;

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        // nothing for now
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        MainActivity activity = (MainActivity)drawerView.getContext();
        CharSequence currentTitle = activity.getDiningHallTitle();
        activity.setTitle(R.string.app_name);

        if(title == null) {
            title = currentTitle;
        }
    }

    @Override
    public void onDrawerClosed(View view) {
        // update the menu
        MainActivity activity = (MainActivity)view.getContext();
        CharSequence currentTitle = activity.getDiningHallTitle();
        activity.setTitle(currentTitle);

        if(title == null) {
            title = currentTitle;
        } else if(!currentTitle.equals(title)) {
            DiningHallFragment current = activity.getDiningHallFragment();
            current.fetchMenu();
            title = currentTitle;
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        // nothing for now
    }
}
