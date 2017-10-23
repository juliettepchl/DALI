package cs65.confuse;


import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;


import java.util.ArrayList;

/**
 * Created by Fanglin Chen on 12/18/14.
 * Used by Luke Hudspeth and Julliete Pouchol for CS65 Lab2 Tab Implementation
 *
 */

public class TabsViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public static final int PLAY = 0;
    public static final int HISTORY = 1;
    public static final int RANKING = 2;
    public static final int SETTINGS = 3;
    public static final String UI_TAB_PLAY = "PLAY";
    public static final String UI_TAB_HISTORY = "HISTORY";
    public static final String UI_TAB_RANKING = "RANKING";
    public static final String UI_TAB_SETTINGS = "SETTINGS";

    public TabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public android.support.v4.app.Fragment getItem(int pos){
        return fragments.get(pos);
    }

    public int getCount(){
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case PLAY:
                return UI_TAB_PLAY;
            case HISTORY:
                return UI_TAB_HISTORY;
            case RANKING:
                return UI_TAB_RANKING;
            case SETTINGS:
                return UI_TAB_SETTINGS;
            default:
                break;
        }
        return null;
    }
}