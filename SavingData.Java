package cs65.confuse;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.SharedPreferences;

/**
 * Created by LukeHudspeth on 9/25/17.
 */



public class SavingData extends AppCompatActivity {

    public String SHARED_PREF_CHAR_NAME = "charname";
    public String SHARED_PREF_FULL_NAME = "fullname";
    public String SHARED_PREF_PSWD = "pswd";
    public String SHARED_PREF_PSWD_VERIFY = "pswd_verify";
    public String SHARED_PREF_PROF_PIC = "prof_pic";


    public EditText character_name;
    public EditText full_name;
    public EditText password;
    public EditText password_verification;
    public Image profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        character_name = (EditText) findViewById(R.id.characterName);
        full_name = (EditText) findViewById(R.id.fullName);
        password = (EditText) findViewById(R.id.password);
        password_verification = (EditText) findViewById(R.id.passwordVerification);
    }

    public void saveData(View v){
        SharedPreferences sp_char_name = getSharedPreferences(SHARED_PREF_CHAR_NAME, 0);
        SharedPreferences sp_full_name = getSharedPreferences(SHARED_PREF_FULL_NAME, 0);
        SharedPreferences sp_pswd = getSharedPreferences(SHARED_PREF_PSWD, 0);
        SharedPreferences sp_pswd_verify = getSharedPreferences(SHARED_PREF_PSWD_VERIFY, 0);


        SharedPreferences.Editor editor_char_name = sp_char_name.edit();
        editor_char_name.putString( "text", character_name.getText().toString());

        SharedPreferences.Editor editor_full_name = sp_full_name.edit();
        editor_full_name.putString( "text", full_name.getText().toString());

        SharedPreferences.Editor editor_password = sp_pswd.edit();
        editor_password.putString( "text", password.getText().toString());

        SharedPreferences.Editor editor_password_verification = sp_pswd_verify.edit();
        editor_password_verification.putString( "text", password.getText().toString());



    }

    public void loadData(View v){
        SharedPreferences sp_char_name = getSharedPreferences(SHARED_PREF_CHAR_NAME, 0);
        SharedPreferences sp_full_name = getSharedPreferences(SHARED_PREF_FULL_NAME, 0);
        SharedPreferences sp_pswd = getSharedPreferences(SHARED_PREF_PSWD, 0);
        SharedPreferences sp_pswd_verify = getSharedPreferences(SHARED_PREF_PSWD_VERIFY, 0);

        character_name.setText(sp_char_name.getString("text", ""));
        full_name.setText(sp_full_name.getString("text", ""));
        password.setText(sp_pswd.getString("text", ""));
        password_verification.setText(sp_pswd_verify.getString("text", ""));



    }


    public void saveImage(View v){
        SharedPreferences sp_char_prof_pic = getSharedPreferences(SHARED_PREF_PROF_PIC, 0);
    }



}


