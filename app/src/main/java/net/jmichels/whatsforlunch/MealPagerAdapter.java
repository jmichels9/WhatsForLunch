package net.jmichels.whatsforlunch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by Jay on 1/3/2015.
 */
public class MealPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private MealFragment[] mMealFragments;

    public MealPagerAdapter(FragmentManager fm, MealFragment[] mealFragments) {
        super(fm);
        mFragmentManager = fm;
        mMealFragments = mealFragments;
    }

    public void setPagerItems(MealFragment[] pagerItems) {
        if (mMealFragments != null) {
            for (int i = 0; i < mMealFragments.length; i++) {
                mFragmentManager.beginTransaction().remove(mMealFragments[i]).commit();
            }
        }
        mMealFragments = pagerItems;
    }

    @Override
    public Fragment getItem(int position) {
        return mMealFragments[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
                return Helpers.BREAKFAST.toUpperCase(l);
            case 1:
                return Helpers.LUNCH.toUpperCase(l);
            case 2:
                return Helpers.DINNER.toUpperCase(l);
        }
        return null;
    }
}
