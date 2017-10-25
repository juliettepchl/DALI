package cs65.confuse;

/**
 * Created by LukeHudspeth on 10/9/17.
 * interactive tab of the sliding tab layout.
 */

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PlayFragment extends Fragment {
    private Button playButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         //   Inflate the layout for this fragment
        return inflater.inflate(R.layout.playfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playButton = view.findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), MapActivity.class);
                    startActivity(myIntent);
            }

        });
    }
}