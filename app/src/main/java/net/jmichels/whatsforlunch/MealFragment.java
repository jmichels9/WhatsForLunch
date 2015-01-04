package net.jmichels.whatsforlunch;

/**
 * Created by Jay on 12/28/2014.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MealFragment extends Fragment {

    public static MealFragment newInstance(String mealText) {
        MealFragment fragment = new MealFragment();
        Bundle args = new Bundle();
        args.putSerializable(Helpers.MEAL_FRAGMENT_STRING, mealText);
        fragment.setArguments(args);
        return fragment;
    }

    public MealFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meal, container, false);
        updateMealTextView(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(settings.getBoolean(Helpers.FIRST_RUN,true)) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getDiningHallFragment().fetchMenu();
            settings.edit().putBoolean(Helpers.FIRST_RUN,false).commit();
        }
    }

    public void updateMealTextView(View v) {
        TextView meal = (TextView)v.findViewById(R.id.mealTextView);

        String mealText = getArguments().getString(Helpers.MEAL_FRAGMENT_STRING);
        if(mealText == null || mealText.length() <= 0) {
            mealText = Helpers.MENU_UNAVAILABLE_MESSAGE;
        }

        meal.setText(Html.fromHtml(mealText));
    }
}