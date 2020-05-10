package com.tuna.videocall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.stringee.call.StringeeCall;

import org.json.JSONObject;

public class OutgoingCallActivity extends AppCompatActivity {
    private FrameLayout userRemote;
    private FrameLayout userLocal;
    private TextView tvStatus;
    private Button btnCancel;
    private String from, to;

    private StringeeCall stringeeCall;
    private StringeeCall.SignalingState state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_call);
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        initView();
        makeCall();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringeeCall != null) {
                    stringeeCall.hangup();
                    finish();
                }
            }
        });
    }

    private void makeCall() {
        stringeeCall = new StringeeCall(OutgoingCallActivity.this, MainActivity.stringeeClient,from,to);
        stringeeCall.setVideoCall(true);
        stringeeCall.setCallListener(new StringeeCall.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s, int i, String s1) {
                tvStatus.setText(signalingState.toString());
                state = signalingState;
                if (state == StringeeCall.SignalingState.ENDED) {
                    finish();
                }
            }

            @Override
            public void onError(StringeeCall stringeeCall, int i, String s) {

            }

            @Override
            public void onHandledOnAnotherDevice(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s) {

            }

            @Override
            public void onMediaStateChange(StringeeCall stringeeCall, StringeeCall.MediaState mediaState) {

            }

            @Override
            public void onLocalStream(StringeeCall stringeeCall) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userLocal.addView(stringeeCall.getLocalView());
                        stringeeCall.renderLocalView(true);
                    }
                });
            }

            @Override
            public void onRemoteStream(StringeeCall stringeeCall) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userRemote.addView(stringeeCall.getRemoteView());
                        stringeeCall.renderRemoteView(false);
                    }
                });
            }

            @Override
            public void onCallInfo(StringeeCall stringeeCall, JSONObject jsonObject) {

            }
        });
        stringeeCall.makeCall();
    }

    private void initView() {
        userRemote = (FrameLayout) findViewById(R.id.userRemote);
        userLocal = (FrameLayout) findViewById(R.id.userLocal);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }
}
