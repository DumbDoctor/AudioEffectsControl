package com.example.nijie.audioeffectscontrol;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.media.MediaPlayer;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by nijie on 8/27/15.
 */
public class EqualizerController {
    private final String TAG = "NJTEST";

    private View mView;
    private Equalizer mEqualizer;
    private int numBands = 0;

    private Context mContext;

    private MediaPlayer mPlayer;
    private Visualizer mVisualizer;

    VisualizerView mVisualizerView;

    private short mNumFrequencyBands = 0;
    private short mLowerBandLevel = 0;
    private short mUpperBandLevel = 0;

    private LinearLayout mLinearLayoutView;




    public EqualizerController(View rootView, Context context) {
        mView = rootView;
        mContext = context;


        mPlayer = MediaPlayer.create(context, R.raw.testmp3);

        setupVisualizerUI();

        mEqualizer = new Equalizer(0, mPlayer.getAudioSessionId());
        //mEqualizer = new Equalizer(0, 0);
        mNumFrequencyBands = mEqualizer.getNumberOfBands();
        mLowerBandLevel = mEqualizer.getBandLevelRange()[0];
        mUpperBandLevel = mEqualizer.getBandLevelRange()[1];

        mEqualizer.setEnabled(true);
        setupEqualizerUI();

        mVisualizer.setEnabled(true);
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });


        mPlayer.start();


    }

    private void setupVisualizerUI(){
        // Create the Visualizer object and attach it to our media player.
        mVisualizerView = (VisualizerView) mView.findViewById(R.id.myvisualizerview);
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);

    }

    private void setupEqualizerUI() {

        mLinearLayoutView = (LinearLayout)mView.findViewById(R.id.equalizer_detail);
        TextView equalizerHeading = new TextView(mContext);
        equalizerHeading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        equalizerHeading.setText("Equalizer");
        equalizerHeading.setTextSize(30);

        equalizerHeading.setGravity(Gravity.CENTER_HORIZONTAL);

        mLinearLayoutView.addView(equalizerHeading);

        for(short i=0; i < mNumFrequencyBands; i++){
            final short frequencyBandIndex = i;
            //Add center frequency text
            TextView frequencyHeaderView = new TextView(mContext);
            frequencyHeaderView.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            frequencyHeaderView.setText(mEqualizer.getCenterFreq(frequencyBandIndex) / 1000 + "HZ");
            frequencyHeaderView.setGravity(Gravity.CENTER_HORIZONTAL);
            mLinearLayoutView.addView(frequencyHeaderView);

            //setuup linear layout to contain seekbars for each center frequency
            LinearLayout seekBarRowLayout = new LinearLayout(mContext);
            seekBarRowLayout.setOrientation(LinearLayout.HORIZONTAL);
            //Set the lower level text
            TextView lowerEqualizerBandLevelTextView = new TextView(mContext);
            lowerEqualizerBandLevelTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) );
            lowerEqualizerBandLevelTextView.setText(mLowerBandLevel/100 + " dB");

            TextView upperEqualizerBandLevelTextView = new TextView(mContext);
            upperEqualizerBandLevelTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) );
            upperEqualizerBandLevelTextView.setText(mUpperBandLevel/100 + " dB");

            //Setup layout parameters for the seekbar
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;

            SeekBar seekBar = new SeekBar(mContext);
            seekBar.setId(i);

            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(mUpperBandLevel - mLowerBandLevel);
            seekBar.setProgress(mEqualizer.getBandLevel(frequencyBandIndex));

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                    mEqualizer.setBandLevel(frequencyBandIndex, (short)(progress + mLowerBandLevel));
                }

                public void onStartTrackingTouch(SeekBar seekBar){

                }

                public void onStopTrackingTouch(SeekBar seekBar){

                }
            });

            seekBarRowLayout.addView(lowerEqualizerBandLevelTextView);
            seekBarRowLayout.addView(seekBar);
            seekBarRowLayout.addView(upperEqualizerBandLevelTextView);

            mLinearLayoutView.addView(seekBarRowLayout);

        }

        loadPresets();



    }

    private void loadPresets(){

        ArrayList<String> equalizerPresetsNames = new ArrayList<String>();
        ArrayAdapter<String> equalizerPresetsSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, equalizerPresetsNames);
        equalizerPresetsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner equalizerPresetsSpinner = (Spinner) mView.findViewById(R.id.spinner);

        for(short i=0; i<mEqualizer.getNumberOfPresets();i++){
            equalizerPresetsNames.add(mEqualizer.getPresetName(i));
        }
        equalizerPresetsSpinner.setAdapter(equalizerPresetsSpinnerAdapter);

        equalizerPresetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEqualizer.usePreset((short) position);
                short numFrequencyBands = mEqualizer.getNumberOfBands();
                final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];

                for(short i=0; i< numFrequencyBands; i++){
                    short equalizerBandIndex = i;
                    SeekBar seekBar = (SeekBar) mView.findViewById(equalizerBandIndex);
                    seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onPause(){
        mVisualizer.setEnabled(false);
        mPlayer.pause();
    }

    public void onDetach(){
        mVisualizer.setEnabled(false);
        mPlayer.stop();

    }
}

