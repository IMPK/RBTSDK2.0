package com.example.vodafonecallertunes_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.onmobile.rbt.baseline.LauncherActivity;
import com.onmobile.rbt.baseline.MsisdnType;
import com.onmobile.rbt.baseline.RbtSdkClient;
import com.onmobile.rbt.baseline.RbtSdkInitialisationException;

public class MainActivity extends AppCompatActivity {

    RbtSdkClient rbtSdkClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(com.onmobile.rbt.baseline.R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtSdkClient.startSDK();
                finish();
            }
        });
        try {
            rbtSdkClient = new RbtSdkClient.Builder()
                    .init(MainActivity.this)
                    .setMsisdn("9513685616")
                    .setMsisdnType(MsisdnType.PRIMARY)
                    .setOperator("Vk9EQUZPTkU=")
                    .build();
        } catch (RbtSdkInitialisationException e) {
            e.printStackTrace();
        }

    }
}
