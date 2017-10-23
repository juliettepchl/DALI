package cs65.confuse;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 *  Luke Hudspeth and Juliette Pouchol "A TEAM HAS NO NAME"
 *  created on 29/09/2017
 *
 *  class to get login activity views
 *
 *
 */

public class AppDataViews{

    public Activity activity;

    public AppDataViews(Activity _activity){
        activity = _activity;
    }

    public Button getSignInButtonView(){ return (Button)activity.findViewById(R.id.sign_in_button);}

    public EditText getCharacterView() {return (EditText)activity.findViewById(R.id.characterName);}

    public EditText getPasswordView() {
        return (EditText)activity.findViewById(R.id.password);
    }

    public EditText getFullNameView() {
        return (EditText)activity.findViewById(R.id.fullName);
    }

    public ImageView getCameraView() { return (ImageView)activity.findViewById(R.id.cameraView);}
}