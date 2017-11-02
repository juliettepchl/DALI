package cs65.confuse;

/**
 * Created by LukeHudspeth on 10/9/17.
 */


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {
    private SaveData sd;
    private CatAdapter adapter;
    private RecyclerView mCatRecyclerView;
    private List<Cat> catList;
    private Account account;
    private Bitmap myBitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.historyfragment, container, false);
        mCatRecyclerView = (RecyclerView)view.findViewById(R.id.cat_recycler_view);
        mCatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        SaveData sd = new SaveData(getActivity());
        sd.initialize();
        String diff = sd.loadDiff();
        account = sd.load();

        URL url = null;
        try {
            url = new URL("http://cs65.cs.dartmouth.edu/catlist.pl?name=" + account.name + "&password=" + account.password + "&mode=" + diff);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the string, based on provided class object as template
                            Gson gson = new GsonBuilder().create();
                            Cat[] list = gson.fromJson(response, Cat[].class);
                            catList = new ArrayList<Cat>(Arrays.asList(list));
                            adapter = new CatAdapter(catList);
                            mCatRecyclerView.setAdapter(adapter);

                        } catch (Exception e) {
                            Log.d("JSON", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(stringRequest);

    }


    private class CatHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        private TextView mNameTextView;
        private TextView mLatView;
        private TextView mLongView;
        private Cat mCat;
        private ImageView mImageView;
        private ImageButton mLoveButton;


        public CatHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_cat, parent, false));
            mNameTextView = itemView.findViewById(R.id.cat_name);
            mLatView = itemView.findViewById(R.id.cat_lat);
            mLongView = itemView.findViewById(R.id.cat_long);
            mImageView = (ImageView)itemView.findViewById(R.id.cat_pic);
            mLoveButton = (ImageButton)itemView.findViewById(R.id.loveButton);
            mLoveButton.setOnClickListener(this);



        }
        public void Bind(final Cat cat){
            mCat = cat;
            mNameTextView.setText(mCat.name);
            mLatView.setText("Lat: " + Float.toString(mCat.lat));
            mLongView.setText("Lng: " +Float.toString(mCat.lng));
            
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL(mCat.picUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        myBitmap = BitmapFactory.decodeStream(input);
                        if (myBitmap != null) {
                            mImageView.setImageBitmap(myBitmap);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            if (myBitmap != null) {
                    mImageView.setImageBitmap(myBitmap);
            }

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mCat.name +" clicked!", Toast.LENGTH_SHORT).show();
            mLoveButton = view.findViewById(R.id.loveButton);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mLoveButton.setImageDrawable(getActivity().getDrawable(R.drawable.filled_heart));
            }
        }
    }

    private class CatAdapter extends RecyclerView.Adapter<CatHolder>{

        private List<Cat> cats;
        public CatAdapter(List<Cat> _cats ){
            cats = _cats;

        }

        @Override
        public CatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
           return new CatHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CatHolder holder, int position) {
            Cat cat = cats.get(position);
            holder.Bind(cat);
        }

        @Override
        public int getItemCount() {
            return cats.size();
        }

    }
}







