package net.jmichels.whatsforlunch;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DiningHallFragment extends Fragment {

    public static final String TAG = DiningHallFragment.class.getSimpleName();

    private static final String ARG_DINING_HALL = "dining_hall";

    MealsPagerAdapter mMealsPagerAdapter;
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

        mMealsPagerAdapter = new MealsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mMealsPagerAdapter);

        return v;
    }

    public void fetchMenu() {
        DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);

        // Get the last input date or use today
        Calendar rightNow = Calendar.getInstance();
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        int month = settings.getInt(Helpers.MENU_MONTH, rightNow.get(Calendar.MONTH));
        int day = settings.getInt(Helpers.MENU_DAY, rightNow.get(Calendar.DAY_OF_MONTH));

        // Clear the current menu items
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Helpers.CURRENT_BREAKFAST,Helpers.FETCHING_MENU);
        editor.putString(Helpers.CURRENT_LUNCH,Helpers.FETCHING_MENU);
        editor.putString(Helpers.CURRENT_DINNER,Helpers.FETCHING_MENU);
        editor.commit();


        FetchMenuTask fetchMenu = new FetchMenuTask(getActivity(), getView(), mViewPager.getCurrentItem());
        fetchMenu.execute(String.valueOf(month+1),String.valueOf(day),diningHall.getId().toString());

        TextView diningHallTextView = (TextView)getActiveTab().getView().findViewById(R.id.mealHallTextView);
        diningHallTextView.setText(Helpers.MENU_LOADING_DATA);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);
        ((MainActivity) activity).onSectionAttached(diningHall);
    }

    public MealFragment getActiveTab() {
        return (MealFragment) mMealsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
    }

    public class MealsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public MealsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MealFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_breakfast).toUpperCase(l);
                case 1:
                    return getString(R.string.title_lunch).toUpperCase(l);
                case 2:
                    return getString(R.string.title_dinner).toUpperCase(l);
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
