package cs65.confuse;

/**
 * Created by LukeHudspeth on 10/9/17.
 * interactive tab of the sliding tab layout.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PlayFragment extends Fragment {
    private Button playButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        playButton = container.findViewById(R.id.playButton);
//
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playButton.getText().toString().equals("I ALREADY HAVE AN ACCOUNT")){
//                    Intent myIntent = new Intent(getActivity(), MapActivity.class);
//                    startActivity(myIntent);
//                }
//
//            }
//        });
        return inflater.inflate(R.layout.playfragment, container, false);
    }
}