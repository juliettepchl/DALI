package cs65.confuse;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


/*
*Created by Luke Hudspeth and Juliette Pouchol for lab2 implementation
* the main app interacts with the various fragments of our sliding tabs layout.
*
 */

import java.util.ArrayList;

public class MainApp extends AppCompatActivity {
    //Changes
    private SlidingTabLayout slidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private TabsViewPagerAdapter mViewPagerAdapter;
    private Bitmap bitmap;
    private Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        Toast.makeText(getApplicationContext(), "Log In Successful", Toast.LENGTH_LONG).show();
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new PlayFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new RankingFragment());
        fragments.add(new SettingsFragment());

        mViewPagerAdapter = new TabsViewPagerAdapter(getSupportFragmentManager(),fragments);

        mViewPager.setAdapter(mViewPagerAdapter);

        slidingTabLayout.createDefaultTabView(this);
        slidingTabLayout.setViewPager(mViewPager);



    }

}