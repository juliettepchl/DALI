package cs65.confuse;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity {

    //Changes
    private SlidingTabLayout slidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private TabsViewPagerAdapter mViewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment());
        fragments.add(new PeopleFragment());
        fragments.add(new ProjectsFragment());


        mViewPagerAdapter = new TabsViewPagerAdapter(getSupportFragmentManager(),fragments);

        mViewPager.setAdapter(mViewPagerAdapter);

        slidingTabLayout.createDefaultTabView(this);
        slidingTabLayout.setViewPager(mViewPager);

        Bundle extras = getIntent().getExtras();
    }

}

