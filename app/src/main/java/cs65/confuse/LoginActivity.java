package cs65.confuse;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Luke Hudspeth and Juliette Pouchol "A TEAM HAS NO NAME"
 * created on 25/09/2017
 * <p>
 * A login screen that offers login via userName/password.
 *
 * Camera Handling Code adapted from class provided example
 * GitHub Repository Here: "https://github.com/mishravarun/CS65-Samples/tree/master/Camera"
 */

public  class LoginActivity extends AppCompatActivity implements ListenerInterface {


    private final int REQUEST_IMAGE_CAPTURE = 0;
    private Button clearButton;
    private Bitmap bitmap;
    private boolean errors = false;
    private AppDataStrings ads;
    private AppDataViews adv;
    private SaveData sd;
    private File f;
    private boolean isTakenFromCamera;
    private String prevPassword;
    private String newPassword;
    private boolean isDifferent;
    private GsonVolley gv;
    public Account account;
    private String req;




    //use Shared Preferences to load data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_login);
        } else {
            setContentView(R.layout.activity_login_flipped);
        }

        //classes that hold data
        adv = new AppDataViews(this);
        ads = new AppDataStrings(adv);
        sd = new SaveData(this);
        sd.initialize();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        adv.getCameraView().setImageBitmap(bitmap);

        //set up clear button
        clearButton = (Button) findViewById(R.id.button);
        clearButton.setText("I ALREADY HAVE AN ACCOUNT");
        setListeners();
    }
    // set view listeners
    private void setListeners() {

        adv.getCharacterView().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clearButton.setText("Clear");

            }

            @Override
            public void afterTextChanged(Editable editable) {gv = new GsonVolley();
                try {
                    gv.CheckUserName(LoginActivity.this, ads.getCharacterName());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }}

        });

        adv.getPasswordView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevPassword = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newPassword = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if(prevPassword != newPassword){
                        isDifferent=true;
                    }
                    else{
                        isDifferent=false;
                    }
            }
        });

        adv.getPasswordView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!view.isFocused()&& ads.getPassword()!=""&&isDifferent){
                    VerifyPassword();
                    isDifferent = false;
                }

            }
        });

        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clearButton.getText().toString().equals("I ALREADY HAVE AN ACCOUNT")){
                    Intent myIntent = new Intent(LoginActivity.this, SignIn.class);
                    startActivity(myIntent);
                } else {
                    ads.setPassword("");
                    ads.setFullName("");
                    ads.setCharacterName("");
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    adv.getCameraView().setImageBitmap(bitmap);
                    clearButton.setText("I ALREADY HAVE AN ACCOUNT");
                    sd.save(buildAccount());
                }
            }
        });

        //start the camera taking and cropping activity.
        adv.getCameraView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    isTakenFromCamera = true;
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        //sign in button handling
        adv.getSignInButtonView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (attemptLogin()) {
                    doPost(buildJSONRequest());
                    Intent myIntent = new Intent(LoginActivity.this, MainApp.class);
                    //myIntent.putExtra("userInfo", (Parcelable) ads);
                    startActivity(myIntent);
                }
            }
        });


        adv.getFullNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clearButton.setText("Clear");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // attempts to login
    private boolean attemptLogin() {
        // Reset errors.
        if(adv.getPasswordView().isFocused()&&isDifferent){
            VerifyPassword();
            isDifferent=false;
            return false;
        }
        adv.getCharacterView().setError(null);
        adv.getPasswordView().setError(null);
        adv.getCharacterView().setError(null);
        assessErrors();
        return !errors;
    }

    // method that verifies that all the fields have been filled, and that the password was verified
    private void assessErrors() {
        // Check for valid characters
         if (TextUtils.isEmpty(ads.getCharacterName())) {
            adv.getCharacterView().setError(getString(R.string.error_field_required));
            errors = true;
        } else if (TextUtils.isEmpty(ads.getFullName())) {
            adv.getFullNameView().setError(getString(R.string.error_field_required));
            errors = true;
        } // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(ads.getPassword())) {
            adv.getPasswordView().setError(getString(R.string.error_field_required));
            errors = true;
        } else if (!isPasswordValid(ads.getPassword())) {
            adv.getPasswordView().setError(getString(R.string.error_invalid_password));
            ads.setPasswordVerification(null);
            errors = true;
        }else if(!((CheckBox)findViewById(R.id.availableButton)).isChecked()){
            adv.getCharacterView().setError("This username is already taken");
            adv.getCharacterView().requestFocus();
            ads.setCharacterName(null);
            errors = true;
        }
        else {
            errors = false;
        }
    }

    //password length check
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    // save data to shared preferences
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("STATE", "onSaveState");
        outState.putParcelable("IMG", bitmap);
        sd.save(buildAccount());
    }

    //load data from savedInstanceState bundle
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("STATE", "onRestoreState");
        bitmap = savedInstanceState.getParcelable("IMG");
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void VerifyPassword() {
        FragmentManager fm = getFragmentManager();
        PasswordCheckFragment passwordCheckFragment = PasswordCheckFragment.newInstance("Password Check");
        passwordCheckFragment.show(fm, "fragment_password_check");
    }


    // Handle data after activity returns.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                // Send image taken from camera for cropping
                Bundle extras = data.getExtras();
                bitmap = (Bitmap)extras.get("data");
                f = sd.saveImage(bitmap);
                Uri source = Uri.fromFile(f);
                beginCrop(source);
                break;

            case Crop.REQUEST_CROP: //We changed the RequestCode to the one being used by the library.
                // Update image view after image crop
                handleCrop(resultCode, data);
                break;
        }
    }

    //use 3rd party library to crop based off of class example
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            adv.getCameraView().setImageURI(Crop.getOutput(result));
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Crop.getOutput(result));
                sd.save(buildAccount());
            }
            catch(IOException e){
                e.printStackTrace();
            }

            //error handling
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private JSONObject buildJSONRequest(){
        JSONObject o = new JSONObject();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        try {
            o.put("name", ads.getCharacterName());
            o.put( "password", ads.getPassword());
            o.put("fullName", ads.getFullName());
            o.put("profile_pic", imageEncoded);
            Toast.makeText(getApplicationContext(), imageEncoded, Toast.LENGTH_LONG).show();


        }
        catch( JSONException e){
            Log.d("JSON", e.toString());
        }

        return o;
    }


    @Override
    public void onFinishEditDialog(String inputText) {
        if(inputText.equalsIgnoreCase(ads.getPassword())){
            Toast.makeText(getApplicationContext(), "Passwords match", Toast.LENGTH_SHORT).show();
        }
        else{
            adv.getPasswordView().setError("Passwords do not match");
            ads.setPassword(null);
            adv.getPasswordView().requestFocus();
        }
    }


    public Account buildAccount(){
        account = new Account();
        account.password = ads.getPassword();
        account.fullName = ads.getFullName();
        account.name = ads.getCharacterName();
        account.prof = bitmap;
        return account;
    }

    //function that posts the account information to the server from a JSON object
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

    //helper function for doPost
    private String readStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

}