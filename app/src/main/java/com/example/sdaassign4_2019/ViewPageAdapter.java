package com.example.sdaassign4_2019;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * <pre>viewPager adapter adapted from the assignment 4 zip folder.
 * Adapts the view so that the three fragments are shown in tabbed
 * layout with tab navigation features as well.
 *
 * references:
 *  *        - https://developer.android.com/docs
 *
 * Original @author Chris Coughlan 2019
 *
 * Reapplied to this project without any changes by:
 *                          - Colin Fleck - colin.fleck3@mail.dcu.ie
 *                          </pre>
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

    private Context context;

    ViewPageAdapter(FragmentManager fm, int behavior, Context nContext) {
        super(fm, behavior);
        context = nContext;
    }

    /**
     * <pre>Fragment class helps user navigate between fragments in the tabbed view.
     * @param position gets the int position associated with the fragment currently in view.
     * @return the fragment to be shown.</pre>
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new Fragment();

        //finds the tab position (note array starts at 0)
        position = position+1;

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                fragment = new Welcome();
                break;
            case 2:
                //code
                fragment = new BookList();
                break;
            case 3:
                //code
                fragment = new Settings();
                break;
        }

        return fragment;
    }

    /**
     * <pre>returns the amount of fragments and tabs shown
     * @return the amount of tabs</pre>
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * <pre>Method gets the title of the fragment based off the position and sets its name in the tabs bar
     * @param position gets fragment positions
     * @return returns name of frag at that position</pre>
     */
    @Override
    public CharSequence getPageTitle(int position) {
        position = position+1;

        CharSequence tabTitle = "";

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                tabTitle = "HOME";
                break;
            case 2:
                //code
                tabTitle = "BOOKS";
                break;
            case 3:
                //code
                tabTitle = "SETTINGS";
                break;
        }

        return tabTitle;
    }
}
