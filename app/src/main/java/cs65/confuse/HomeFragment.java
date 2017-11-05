package cs65.confuse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by juliettepouchol on 05/11/2017.
 */

public class HomeFragment extends Fragment {
    private Button playButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //   Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

}
