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

/**
 * A placeholder fragment containing a simple view.
 */
public class MealFragment extends Fragment {

    private static final String ARGS_MEAL = "dining_hall_meal";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MealFragment newInstance(int meal) {
        MealFragment fragment = new MealFragment();
        Bundle args = new Bundle();
        Meal mealEnum = Meal.values()[meal];
        args.putSerializable(ARGS_MEAL, mealEnum);
        fragment.setArguments(args);
        return fragment;
    }

    public MealFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meal, container, false);
        TextView meal = (TextView)v.findViewById(R.id.mealHallTextView);

        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        switch((Meal)getArguments().getSerializable(ARGS_MEAL)) {
            case Breakfast:
                meal.setText(Html.fromHtml(settings.getString(Helpers.CURRENT_BREAKFAST,Helpers.MENU_UNAVAILABLE_MESSAGE)));
                break;
            case Lunch:
                meal.setText(Html.fromHtml(settings.getString(Helpers.CURRENT_LUNCH,Helpers.MENU_UNAVAILABLE_MESSAGE)));
                break;
            case Dinner:
                meal.setText(Html.fromHtml(settings.getString(Helpers.CURRENT_DINNER,Helpers.MENU_UNAVAILABLE_MESSAGE)));
                break;
        }

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

    public enum Meal {
        Breakfast, Lunch, Dinner
    }
}