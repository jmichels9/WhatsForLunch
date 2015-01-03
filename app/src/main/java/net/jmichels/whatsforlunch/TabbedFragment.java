package net.jmichels.whatsforlunch;

import java.util.Locale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabbedFragment extends Fragment {

    public static final String TAG = TabbedFragment.class.getSimpleName();
    MealsPagerAdapter mMealsPagerAdapter;
    ViewPager mViewPager;

    public static TabbedFragment newInstance() {
        return new TabbedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tabbed, container, false);

        mMealsPagerAdapter = new MealsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mMealsPagerAdapter);

        return v;
    }

    public DiningHallFragment getActiveTab() {
        return (DiningHallFragment) mMealsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
    }

    public class MealsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public MealsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MainActivity activity = (MainActivity)getActivity();
            return DiningHallFragment.newInstance(activity.getDiningHall(), DiningHallFragment.Meal.values()[position]);
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
