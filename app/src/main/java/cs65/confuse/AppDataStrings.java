package cs65.confuse;

/**
 *  Luke Hudspeth and Juliette Pouchol "A TEAM HAS NO NAME"
 *  created on 29/09/2017
 *
 *  class to get and set login activity strings
 *
 *
 */

public class AppDataStrings{

    public AppDataViews adv;
    public String passwordVerification = null;
    

    public AppDataStrings(AppDataViews _adv){
        adv = _adv;
    }

    public String getPasswordVerification() {
        return passwordVerification;
    }

    public void setPasswordVerification(String input) {
        passwordVerification = input;
    }

    public String getCharacterName() {
        return adv.getCharacterView().getText().toString();
    }

    public void setCharacterName(String characterName) { adv.getCharacterView().setText(characterName);}

    public String getFullName() {
        return adv.getFullNameView().getText().toString();
    }

    public void setFullName(String fullName) {
        adv.getFullNameView().setText(fullName);
    }

    public String getPassword() {
        return adv.getPasswordView().getText().toString();
    }

    public void setPassword(String password){
        adv.getPasswordView().setText(password);
    }
}