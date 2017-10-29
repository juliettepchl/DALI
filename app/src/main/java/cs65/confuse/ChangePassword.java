package cs65.confuse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    private EditText mEditText;
    private Button confirm;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mEditText = findViewById(R.id.txt_change_password2);
        mEditText.requestFocus();
        confirm = findViewById(R.id.confirm_but);
        SaveData sd = new SaveData(ChangePassword.this);
        sd.initialize();
        account = sd.load();
        try {
            getAccount(ChangePassword.this, account.name, account.password);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            //reset data on sign_out click to default.
            public void onClick(View view) {
                if (account.password.equals(mEditText.getText().toString())){
                    Toast.makeText(ChangePassword.this, "Password is same as original!", Toast.LENGTH_SHORT).show();
                }
                DoGet(account, mEditText.getText().toString());
            }
        });



    }

    public void DoGet(Account account, String newPassword){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);

        URL url = null;
        try {
            url = new URL(String.format("http://cs65.cs.dartmouth.edu/changepass.pl?name="+account.name+"&password="+account.password+"&newpass="+newPassword));
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
                            Reset_Status reset = gson.fromJson(response, Reset_Status.class);

                            if (reset.status.equals("OK")){
                                Toast.makeText(ChangePassword.this, "Password has been changed!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangePassword.this, MainApp.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(ChangePassword.this, reset.status, Toast.LENGTH_SHORT).show();
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

