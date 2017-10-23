package cs65.confuse;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

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
                GsonVolley gv = new GsonVolley();
                try {
                    gv.AttemptLogin(SignIn.this, ((EditText)findViewById(R.id.characterName)).getText().toString(),
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
}

