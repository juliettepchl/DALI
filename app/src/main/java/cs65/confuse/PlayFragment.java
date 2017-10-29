package cs65.confuse;

/**
 * Created by LukeHudspeth on 10/9/17.
 * interactive tab of the sliding tab layout.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayFragment extends Fragment {
    private Button playButton;
    private Account account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         //   Inflate the layout for this fragment
        return inflater.inflate(R.layout.playfragment, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playButton = view.findViewById(R.id.playButton);

        SaveData sd = new SaveData(getActivity());
        sd.initialize();

        account = sd.load();
        try {
            getAccount(getActivity(), account.name, account.password);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        ((EditText)view.findViewById(R.id.name)).setText(String.format("Hello, %s!", account.name));
       // ((EditText)view.findViewById(R.id.Score)).setText("Your current score is: " +account.score.toString());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), MapActivity.class);
                    startActivity(myIntent);
            }

        });
    }

    public void getAccount(final Activity activity, String name, String password) throws MalformedURLException {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        URL url = new URL("http://cs65.cs.dartmouth.edu/profile.pl?name="+name+"&password="+password);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the string, based on provided class object as template
                            Gson gson = new GsonBuilder().create();
                            account = gson.fromJson(response, Account.class);
                            ((EditText)getActivity().findViewById(R.id.name)).setText(String.format("Hello, %s!", account.name));
                            ((EditText)getActivity().findViewById(R.id.Score)).setText("Your current score is: " +account.score.toString());
                        }
                        catch( Exception e){
                            Log.d("JSON", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            // This to set custom headers:
            // https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json"); // or else HTTP code 500
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("STATE", "onRestoreState");
        try {
            getAccount(getActivity(), account.name, account.password);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
    }

}