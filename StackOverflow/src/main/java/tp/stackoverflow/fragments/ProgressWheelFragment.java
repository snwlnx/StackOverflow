package tp.stackoverflow.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tp.stackoverflow.views.ProgressWheel;
import tp.stackoverflow.R;

/**
 * Created by korolkov on 12/2/13.
 */
public class ProgressWheelFragment extends Fragment {
    boolean running;
    ProgressWheel pw_two;

    int progress = 0;

    @Override
    public void onResume() {
        super.onResume();
        pw_two = (ProgressWheel) getActivity().findViewById(R.id.progressBarTwo);
        pw_two.spin();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.progress_wheel_fragment, container, false);
        return rootView;
    }


    @Override
    public void onPause() {
        super.onPause();
        progress = 361;
        pw_two.stopSpinning();
        pw_two.resetCount();
    }
}
