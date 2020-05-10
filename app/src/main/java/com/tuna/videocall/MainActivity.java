package com.tuna.videocall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StringeeConnectionListener;
import com.tuna.videocall.model.Constant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView tvUser;
    private EditText edtUser;
    private Button btnVideoCall;
    private Constant constant;

    //private String token = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSzlsYkt1RGw1STZKaFplVjJOUlUxTFhScGhNTmZtZXlSLTE1ODkwNzk4NTQiLCJpc3MiOiJTSzlsYkt1RGw1STZKaFplVjJOUlUxTFhScGhNTmZtZXlSIiwiZXhwIjoxNTkxNjcxODU0LCJ1c2VySWQiOiJUdW5hIn0.Lza-RrX3Tnu_8CRTBebrZzkuazV6a0JIFhzgwq76eEM";
    private String token = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSzlsYkt1RGw1STZKaFplVjJOUlUxTFhScGhNTmZtZXlSLTE1ODkwODM4ODIiLCJpc3MiOiJTSzlsYkt1RGw1STZKaFplVjJOUlUxTFhScGhNTmZtZXlSIiwiZXhwIjoxNTkxNjc1ODgyLCJ1c2VySWQiOiJCYWNWVCJ9.qwOQSB9VcoWawC6VNvBqX8q4xLam5t-jgJ6caexrZp0";

    public static StringeeClient stringeeClient;
    public static Map<String,StringeeCall> callMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initStringee();
        requirePermissions();
        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OutgoingCallActivity.class);
                intent.putExtra("from", stringeeClient.getUserId());
                intent.putExtra("to",edtUser.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void requirePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        },1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initStringee() {
        stringeeClient = new StringeeClient(this);
        stringeeClient.setConnectionListener(new StringeeConnectionListener() {
            @Override
            public void onConnectionConnected(StringeeClient stringeeClient, boolean b) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvUser.setText(stringeeClient.getUserId());
                    }
                });
            }

            @Override
            public void onConnectionDisconnected(StringeeClient stringeeClient, boolean b) {

            }

            @Override
            public void onIncomingCall(StringeeCall stringeeCall) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callMap.put(stringeeCall.getCallId(),stringeeCall);
                        Intent intent = new Intent(MainActivity.this,IncomingCallActivity.class);
                        intent.putExtra("callId",stringeeCall.getCallId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onConnectionError(StringeeClient stringeeClient, StringeeError stringeeError) {

            }

            @Override
            public void onRequestNewToken(StringeeClient stringeeClient) {

            }

            @Override
            public void onCustomMessage(String s, JSONObject jsonObject) {

            }

            @Override
            public void onTopicMessage(String s, JSONObject jsonObject) {

            }
        });

        stringeeClient.connect(token);
    }

    private void initView() {
        tvUser = findViewById(R.id.tvUser);
        edtUser = findViewById(R.id.edtUser);
        btnVideoCall = findViewById(R.id.btnVideoCall);
    }
}
