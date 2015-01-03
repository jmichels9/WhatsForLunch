package net.jmichels.whatsforlunch;

/**
 * Created by Jay on 12/28/2014.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MealFragment extends Fragment {
    private View mView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MealFragment newInstance() {
        return new MealFragment();
    }

    public MealFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_meal, container, false);
    }
}