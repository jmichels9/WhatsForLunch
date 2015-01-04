package net.jmichels.whatsforlunch;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DiningHallFragment extends Fragment {

    private static final String ARG_DINING_HALL = "dining_hall";

    MealPagerAdapter mMealPagerAdapter;
    ViewPager mViewPager;

    public static DiningHallFragment newInstance(DiningHall diningHall) {
        DiningHallFragment fragment = new DiningHallFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DINING_HALL, diningHall);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dining_hall, container, false);

        MealFragment breakfast = MealFragment.newInstance(Helpers.MENU_NOT_FETCHED);
        MealFragment lunch = MealFragment.newInstance(Helpers.MENU_NOT_FETCHED);
        MealFragment dinner = MealFragment.newInstance(Helpers.MENU_NOT_FETCHED);

        mMealPagerAdapter = new MealPagerAdapter(getChildFragmentManager(), new MealFragment[] { breakfast, lunch, dinner });

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mMealPagerAdapter);

        return v;
    }

    public void fetchMenu() {
        DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);

        // Get the last input date or use today
        Calendar rightNow = Calendar.getInstance();
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        int month = settings.getInt(Helpers.MENU_MONTH, rightNow.get(Calendar.MONTH));
        int day = settings.getInt(Helpers.MENU_DAY, rightNow.get(Calendar.DAY_OF_MONTH));

        FetchMenuTask fetchMenu = new FetchMenuTask(this);
        fetchMenu.execute(String.valueOf(month + 1), String.valueOf(day), diningHall.getId().toString());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);
        ((MainActivity) activity).onSectionAttached(diningHall);
    }

    public MealPagerAdapter getPagerAdapter() {
        return mMealPagerAdapter;
    }
}
