package com.example.vodafonecallertunes_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.onmobile.rbt.baseline.LauncherActivity;
import com.onmobile.rbt.baseline.MsisdnType;
import com.onmobile.rbt.baseline.RbtSdkClient;
import com.onmobile.rbt.baseline.RbtSdkInitialisationException;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    final String VODAFONE_ENCODED_KEY = "Vk9EQUZPTkU=";
    final String IDEA_ENCODED_KEY = "aWRlYQ==";
    final String GRAMEEN_ENCODED_KEY = "Z3JhbWVlbg==";
    final String VODA_PLAY_ENCODED_KEY = "Vm9kYWZvbmUgUGxheQ==";

    RbtSdkClient rbtSdkClient;
    RbtSdkClient.Builder rbtSdkClientBuilder;
    TextInputEditText textInputEditText;
    String mMsisdn;
    Button startButton;

    private RadioGroup mOperatorGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOperatorGroup = findViewById(com.onmobile.rbt.baseline.R.id.radio_group_operator);
        mOperatorGroup.setOnCheckedChangeListener(this);
        textInputEditText = findViewById(R.id.textInputEditText);
        textInputEditText.setText("9513685616");
        startButton = findViewById(com.onmobile.rbt.baseline.R.id.button);
        int checkedId = mOperatorGroup.getCheckedRadioButtonId();
        rbtSdkClientBuilder = getRbtSdkClientBuilder(checkedId);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMsisdn = textInputEditText.getText().toString();
                if(mMsisdn != null && !mMsisdn.isEmpty() && mMsisdn.length() == 10) {
                    try {
                        rbtSdkClientBuilder.setMsisdn(mMsisdn).build().startSDK();
                    } catch (RbtSdkInitialisationException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Ente valid msisdn", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        try {
//            rbtSdkClient = new RbtSdkClient.Builder()
//                    .init(MainActivity.this)
//                    .setMsisdn("9513685616")
//                    .setMsisdnType(MsisdnType.PRIMARY)
//                    .setOperator("Vk9EQUZPTkU=")
//                    .build();
//        } catch (RbtSdkInitialisationException e) {
//            e.printStackTrace();
//        }

    }

    public RbtSdkClient.Builder getRbtSdkClientBuilder(int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_vodafone:
                rbtSdkClientBuilder = new RbtSdkClient.Builder()
                        .init(MainActivity.this)
                        .setMsisdnType(MsisdnType.PRIMARY)
                        .setOperator(VODAFONE_ENCODED_KEY);
                startButton.setEnabled(true);
                break;
            case R.id.radio_button_idea:
                rbtSdkClientBuilder = new RbtSdkClient.Builder()
                        .init(MainActivity.this)
                        .setMsisdnType(MsisdnType.PRIMARY)
                        .setOperator(IDEA_ENCODED_KEY);
                startButton.setEnabled(false);
                Toast.makeText(this,"In Progress Select Different Operator",Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_button_grameen_phone:
                rbtSdkClientBuilder = new RbtSdkClient.Builder()
                        .init(MainActivity.this)
                        .setMsisdnType(MsisdnType.PRIMARY)
                        .setOperator(GRAMEEN_ENCODED_KEY);
                startButton.setEnabled(false);
                Toast.makeText(this,"In Progress Select Different Operator",Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_button_vodafone_play:
                rbtSdkClientBuilder = new RbtSdkClient.Builder()
                        .init(MainActivity.this)
                        .setMsisdnType(MsisdnType.PRIMARY)
                        .setOperator(VODA_PLAY_ENCODED_KEY);
                startButton.setEnabled(false);
                Toast.makeText(this,"In Progress Select Different Operator",Toast.LENGTH_SHORT).show();
                break;
        }

        return rbtSdkClientBuilder;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mMsisdn = textInputEditText.getText().toString();
        rbtSdkClientBuilder = getRbtSdkClientBuilder(checkedId);
    }
}
