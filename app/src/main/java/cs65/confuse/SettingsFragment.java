package cs65.confuse;

/**
 * Created by LukeHudspeth on 10/9/17.
 */


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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

public class SettingsFragment extends Fragment {

    private Button signout;
    private Account account;
    private Button reset;
    private Switch difficulty;
    public String mode = "easy";
    private SaveData sd;
    private ImageView profile_imageview;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.settingsfragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sd = new SaveData(getActivity());
        sd.initialize();
        account = sd.load();
        sd.saveDiff(mode);
        ((EditText)view.findViewById(R.id.name)).setText(account.name);
        ((EditText)view.findViewById(R.id.password)).setText(account.password);
        profile_imageview = view.findViewById(R.id.profile_pic);
        //profile_imageview.setImageBitmap(account.prof);
        signout = view.findViewById(R.id.sign_out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            //reset data on sign_out click to default.
            public void onClick(View view) {
                account.fullName = "";
                account.name  = "";
                account.avail = null;
                account.password = "";
                account.score = 0;
                account.prof = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                sd.save(account);
                Intent intent = new Intent(getActivity(),SignIn.class);
                getActivity().startActivity(intent);
            }
        });
        reset = view.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            //reset data on sign_out click to default.
            public void onClick(View view) {
                try {
                    doReset(getActivity(), account.name, account.password);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        difficulty = view.findViewById(R.id.difficulty);
        difficulty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked){
                    mode = "hard";
                    sd.saveDiff(mode);
                    Toast.makeText(getActivity(), "ahh we are in hard mode now!", Toast.LENGTH_SHORT).show();

                } else{
                    mode = "easy";
                    sd.saveDiff(mode);
                }
            }
        });

    }


    public void doReset(final Activity activity, String name, String password) throws MalformedURLException {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());

        URL url = new URL(String.format("http://cs65.cs.dartmouth.edu/resetlist.pl?name="+name+"&password="+password));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the string, based on provided class object as template
                            Gson gson = new GsonBuilder().create();
                            Reset_Status reset = gson.fromJson(response, Reset_Status.class);

                            if (reset.status.equals("OK")){



                                Toast.makeText(getActivity(), "Cats unpetted!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), reset.status, Toast.LENGTH_SHORT).show();
                            }
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
}
