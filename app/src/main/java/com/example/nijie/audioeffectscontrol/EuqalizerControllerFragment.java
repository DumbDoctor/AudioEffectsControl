package com.example.nijie.audioeffectscontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nijie on 8/27/15.
 */
public class EuqalizerControllerFragment extends ItemDetailFragment {
    private static final String TAG = "NJTEST";
    private EqualizerController mController;

    public static EuqalizerControllerFragment Instantiate(){
        return new EuqalizerControllerFragment();
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EuqalizerControllerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.equalizer_control_detail,container,false);
        mController = new EqualizerController(rootView,getActivity().getApplicationContext());

        return rootView;

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mController != null) {
            mController.onPause();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mController != null){
            mController.onDetach();

        }
    }
}
