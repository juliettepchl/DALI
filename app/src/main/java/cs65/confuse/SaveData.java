package cs65.confuse;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LukeHudspeth on 9/25/17.
 * Code used to save image to local storage based off of stackoverflow example
 * found here: "https://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage"
 * class implemented to save data to local storage
 */

public class SaveData {
    private Activity activity;
    private SharedPreferences appPrefs;
    private Bitmap b;

    public SaveData(Activity _activity) {
        activity = _activity;

    }

    public void initialize() {
        appPrefs = activity.getSharedPreferences("cs65.confuse.LoginActivity_preferences", Context.MODE_PRIVATE);
    }

    //method called to save all the data to local storage including text edits and image
    public void save(Account account) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString("password", account.password);
        prefsEditor.commit();
        prefsEditor.putString("userName", account.name);
        prefsEditor.commit();
        prefsEditor.putString("fullName", account.fullName);
        prefsEditor.commit();
    }

    //function called to load data from previously saved shared preferences.
    public Account load() {
        Account account = new Account();
        account.name = appPrefs.getString("userName", "");
        account.fullName = appPrefs.getString("fullName", "");
        account.password = appPrefs.getString("password", "");
        account.prof = loadImage(appPrefs.getString("path", ""));
        return account;
    }

    public Bitmap getImage() {
        return b;

    }

    //function called to load image from storarage via image path as a string
    public Bitmap loadImage(String path) {
        try {
            //Toast.makeText(activity.getApplicationContext(),appPrefs.getString("path", "") ,Toast.LENGTH_SHORT).show();
            File f = new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    //function called to save the image path as a string to shared preferences.
    public File saveImage(Bitmap bitmap) {

        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Toast.makeText(activity.getApplicationContext(),appPrefs.getString("path", "") ,Toast.LENGTH_SHORT).show();
        return mypath;
    }

    public void saveDiff(String diff) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString("diff", diff);
        prefsEditor.commit();
    }

    public String loadDiff() {
        String diff = appPrefs.getString("diff", "");
        return diff;
    }


}


