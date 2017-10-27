package cs65.confuse;

/**
 * Created by LukeHudspeth on 10/9/17.
 */


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SettingsFragment extends Fragment {

    private Button signout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settingsfragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SaveData sd = new SaveData(getActivity());
        sd.initialize();
        Account account = sd.load();
        ((EditText)view.findViewById(R.id.name)).setText(account.name);
        ((ImageView)view.findViewById(R.id.profile_pic)).setImageBitmap(account.prof);
        signout = view.findViewById(R.id.sign_out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            //reset data on sign_out click to default.
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SignIn.class);
                getActivity().startActivity(intent);
            }
        });
    }




}
