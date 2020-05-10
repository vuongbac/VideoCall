package com.tuna.videocall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.stringee.call.StringeeCall;
import com.stringee.common.StringeeConstant;

import org.json.JSONObject;

public class IncomingCallActivity extends AppCompatActivity {
    private FrameLayout userRemote;
    private FrameLayout userLocal;
    private TextView tvStatus;
    private Button btnCancel;
    private Button btnAnswer;
    private Button btnReject;

    private StringeeCall stringeeCall;
    private StringeeCall.SignalingState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        initView();
        initAnswer();
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringeeCall != null) {
                    stringeeCall.answer();
                    btnAnswer.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringeeCall != null) {
                    stringeeCall.reject();
                }
            }
        });

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

    private void initAnswer() {
        String callId = getIntent().getStringExtra("callId");
        stringeeCall = MainActivity.callMap.get(callId);
        stringeeCall.enableVideo(true);
        stringeeCall.setQuality(StringeeConstant.QUALITY_FULLHD);
        stringeeCall.setCallListener(new StringeeCall.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s, int i, String s1) {

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
        stringeeCall.initAnswer(IncomingCallActivity.this,MainActivity.stringeeClient);
    }

    private void initView() {
        userRemote = (FrameLayout) findViewById(R.id.userRemote);
        userLocal = (FrameLayout) findViewById(R.id.userLocal);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnAnswer = (Button) findViewById(R.id.btnAnswer);
        btnReject = (Button) findViewById(R.id.btnReject);
    }
}
