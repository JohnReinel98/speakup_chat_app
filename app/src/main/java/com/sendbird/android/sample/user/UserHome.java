package com.sendbird.android.sample.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sendbird.android.sample.R;
import com.sendbird.android.sample.main.ChooseActivity;

import java.util.Calendar;
import java.util.Random;

public class UserHome extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private FirebaseAuth firebaseAuth;

    private ChatFragment chatFragment;
    private AlarmFragment alarmFragment;
    private DepressionFragment depressionFragment;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private NavigationView navView;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        navView = findViewById(R.id.navView);
        firebaseAuth = FirebaseAuth.getInstance();

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatFragment = new ChatFragment();
        alarmFragment = new AlarmFragment();
        depressionFragment = new DepressionFragment();

        //when device rotated!=load again
        if(savedInstanceState==null){
            setFragment(chatFragment);
        }


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_chat:
                        setFragment(chatFragment);
                        return true;

                    case R.id.nav_medSched:
                        setFragment(alarmFragment);
                        return true;

                    case R.id.nav_depBar:
                        setFragment(depressionFragment);
                        return true;

                    default:
                        return false;

                }
            }


        });

        dailyQuoteTest();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.nav_home):
                        mMainNav.setVisibility(View.VISIBLE);
                        chatFragment = new ChatFragment();
                        setFragment(chatFragment);
                        break;
                    case(R.id.nav_profile):
                        mMainNav.setVisibility(View.GONE);
                        //ProfileFragment profileFragment = new ProfileFragment();
                        //setFragment(profileFragment);
                        startActivity(new Intent(UserHome.this, UserProfile.class));
                        break;
                    case(R.id.nav_choose):
                        mMainNav.setVisibility(View.GONE);
                        //ProfileFragment profileFragment = new ProfileFragment();
                        //setFragment(profileFragment);
                        startActivity(new Intent(UserHome.this, ChooseActivity.class));
                        break;
                    case(R.id.nav_logout):
                        Logout();
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().getDisplayName()==null&&firebaseAuth.getCurrentUser().getPhotoUrl()==null){
            finish();
            startActivity(new Intent(UserHome.this, UserRegister2.class));
            Toast.makeText(this,"Please finish registraion first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dailyQuoteTest() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        int lastDay = settings.getInt("day", 1);

        if(lastDay!=currentDay){
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.commit();
            //code that will be started only once a day
            String[] arrayOfStrings = this.getResources().getStringArray(R.array.dailyQuotes);
            String rndQuotes = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
            dailyQuotes(rndQuotes);
        }
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

    private void dailyQuotes(String quote){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(UserHome.this).inflate(R.layout.daily_quotes_layout,null);
        TextView quoteCon = v.findViewById(R.id.dailyQuotes);

        quoteCon.setText(quote);

        if(quote.length()>90)
            quoteCon.setTextSize(15);

        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(v);
        builder.show();
    }

//    private void testQuote(){
//        Date now = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(now);
//        int testTime = calendar.get(Calendar.HOUR_OF_DAY);
//        //Toast.makeText(this,testTime+"",Toast.LENGTH_LONG).show();
//        String[] arrayOfStrings = this.getResources().getStringArray(R.array.dailyQuotes);
//        String rndQuotes = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
//
//        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        editor = sharedPref.edit();
//        editor.putInt("dayPassed", 00);
//        editor.commit();
//
//
//        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor mEditor = mPreferences.edit();
//
//        Boolean seen = mPreferences.getBoolean("isSeen", false);
//        int pass = mPreferences.getInt("dayPassed", testTime);
//
//
//
//        if(seen.equals(false)){
//            dailyQuotes(rndQuotes);
//            editor.putBoolean("isSeen", true);
//            editor.commit();
//        }
//        if(pass==00){
//            editor.putBoolean("isSeen", false);
//            return;
//        }
//    }

    public void Logout(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(UserHome.this, UserLogin.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Log out");
        alert.show();
    }
}
