package cs65.confuse;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    private Account account;
    private Button create_account;
    private SaveData sd;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sd = new SaveData(this);
        sd.initialize();
        account = sd.load();
        if(account !=null){
            ((EditText)findViewById(R.id.characterName)).setText(account.name);
            ((EditText)findViewById(R.id.password)).setText(account.password);
        }

        create_account = findViewById(R.id.createAccount);

        create_account.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignIn.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

        login = findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AttemptLogin(SignIn.this, ((EditText)findViewById(R.id.characterName)).getText().toString(),
                            ((EditText)findViewById(R.id.password)).getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Log In Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Log.d("STATE", "onSaveState");
        sd.save(refreshAccountInfo());

    }

    //load data from savedInstanceState bundle
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("STATE", "onRestoreState");
        account = sd.load();
        if(account !=null){
            ((EditText)findViewById(R.id.characterName)).setText(account.name);
            ((EditText)findViewById(R.id.password)).setText(account.password);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public Account refreshAccountInfo(){
        account.name=((EditText)findViewById(R.id.characterName)).getText().toString();
        account.password=((EditText)findViewById(R.id.password)).getText().toString();
        return account;
    }


    public void AttemptLogin(final Activity activity, String name, String password) throws MalformedURLException {
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

                            if(account.error == null) {
                                Intent i = new Intent(activity.getApplicationContext(), MainApp.class);
                                activity.startActivity(i);
                            }
                            else{
                                ((EditText) activity.findViewById(R.id.password)).setText(null);
                                ((EditText) activity.findViewById(R.id.characterName)).setText(null);
                                ((EditText)activity.findViewById(R.id.characterName)).setError("This account does not exist");
                                activity.findViewById(R.id.characterName).requestFocus();


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

