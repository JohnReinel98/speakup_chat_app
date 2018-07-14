package com.sendbird.android.sample.main;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.sample.R;
import com.sendbird.android.sample.groupchannel.GroupChannelActivity;
import com.sendbird.android.sample.utils.PreferenceUtils;
import com.sendbird.android.sample.utils.PushUtils;
import com.sendbird.android.sample.utils.SharedPrefManager;

public class ChooseActivity extends AppCompatActivity {
    Button btnDepression, btnHappy;
    private static final String APP_ID_DEPRESSION = "C05228E5-306E-4E8E-9F4B-83B743B5C961";
    private static final String APP_ID_HAPPY = "B951ECFF-5070-46EC-AEBD-7BD387483E4F";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        PreferenceUtils.init(getApplicationContext());

        btnDepression = findViewById(R.id.btnDepression);
        btnHappy = findViewById(R.id.btnHappy);

        btnDepression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendBird.init(APP_ID_DEPRESSION, getApplicationContext());
                connectToSendBird();
                //startActivity(new Intent(ChooseActivity.this, LoginActivity.class));
                //finish();
            }
        });

        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendBird.init(APP_ID_HAPPY, getApplicationContext());
                connectToSendBird();
                //startActivity(new Intent(ChooseActivity.this, LoginActivity.class));
                //finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PreferenceUtils.getConnected()) {
            connectToSendBird();
        }
    }

    private void connectToSendBird() {
        // Show the loading indicator
        final String userID = SharedPrefManager.getInstance(getApplicationContext()).getuserId();
        final String nickName = SharedPrefManager.getInstance(getApplicationContext()).getNickN();
        ConnectionManager.login(userID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.

                if (e != null) {
                    // Error!
                    Toast.makeText(
                            ChooseActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
                    PreferenceUtils.setConnected(false);
                    return;
                }

                PreferenceUtils.setNickname(nickName);
                PreferenceUtils.setProfileUrl(user.getProfileUrl());
                PreferenceUtils.setConnected(true);

                // Update the user's nickname
                updateCurrentUserInfo(nickName);
                updateCurrentUserPushToken();

                // Proceed to MainActivity
                Intent intent = new Intent(ChooseActivity.this, GroupChannelActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(ChooseActivity.this, null);
    }

    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            ChooseActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar

                    return;
                }

                PreferenceUtils.setNickname(userNickname);
            }
        });
    }

    // Displays a Snackbar from the bottom of the screen


    // Shows or hides the ProgressBar

}
