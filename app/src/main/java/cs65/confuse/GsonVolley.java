package cs65.confuse;

/**
 * Created by juliettepouchol on 09/10/2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Checkable;
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
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GsonVolley extends AppCompatActivity {

    public Account account;
    private String req;

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

    public void CheckUserName(final Activity activity, String name) throws MalformedURLException {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        URL url = new URL("http://cs65.cs.dartmouth.edu/nametest.pl?name="+name);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // parse the string, based on provided class object as template
                            Gson gson = new GsonBuilder().create();
                            account = gson.fromJson(response, Account.class);
                            if(account.avail) {
                                Checkable checkBox = activity.findViewById(R.id.availableButton);
                                checkBox.setChecked(true);
                            }
                            else{
                                Checkable checkBox = activity.findViewById(R.id.availableButton);
                                checkBox.setChecked(false);
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

    public void doPost(JSONObject o){
        req = o.toString();

        new Thread(new Runnable() {

            String res = null; // closed over by the post()-ed run().

            @Override
            public void run() {
                try {
                    URL url = new URL("http://cs65.cs.dartmouth.edu/profile.pl");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // For info on configurable headers of HTTP:
                    // https://developer.android.com/reference/java/net/HttpURLConnection.html

                    try {
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");

                        // we want to see strings going back and forth, don't compress them
                        conn.setRequestProperty("Accept-Encoding", "identity");

                        // This doesn't work with my server: CGI.pm ignores chunked requests
                        //conn.setRequestProperty("Transfer-Encoding", "chunked");
                        //conn.setChunkedStreamingMode(0); // we don't know how much data we'll be sending in the body

                        // we must know exactly what the request body is
                        conn.setFixedLengthStreamingMode(req.length());

                        OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                        out.write(req.getBytes());
                        out.flush();
                        out.close();

                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        res = readStream(in);
                    }
                    catch(Exception e){
                        Log.d("THREAD", e.toString());
                    } finally {
                        conn.disconnect();
                    }
                }
                catch( Exception e){
                    Log.d("THREAD", e.toString());
                }

                if( res!= null ) {
                    Log.d("NET POST", res);
                }
                else{
                    Log.d("NET ERR", "empty result");
                }
            }
        }).start();
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

    public void doGet(final Activity activity, String name, String password) throws MalformedURLException {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        URL url = new URL("http://cs65.cs.dartmouth.edu/profile?name="+name+"&password="+password);

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