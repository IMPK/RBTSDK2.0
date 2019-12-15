package com.onmobile.rbt.baseline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    RbtSdkClient rbtSdkClient;
    private RadioGroup mOperatorGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mOperatorGroup = findViewById(R.id.radio_group);
        mOperatorGroup.setOnCheckedChangeListener(this);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtSdkClient.startSDK();
                finish();
            }
        });
        try {
            rbtSdkClient = new RbtSdkClient.Builder()
                    .init(LauncherActivity.this)
                    .setMsisdn("9513685616")
                    .setMsisdnType(MsisdnType.PRIMARY)
                    .setOperator("Vk9EQUZPTkU=")
                    .build();
        } catch (RbtSdkInitialisationException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
