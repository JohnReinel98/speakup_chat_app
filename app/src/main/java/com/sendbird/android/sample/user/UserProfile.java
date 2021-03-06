package com.sendbird.android.sample.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.sample.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView txtFullname, txtGender, txtStreet, txtCity, txtProvince, txtBirthday;
    private DatabaseReference firebaseDatabaseGender;
    private CircleImageView imgProfilePic;
    private Firebase mRef;
    public String address, street, city, province;
    private ProgressDialog progressDialog;
    private Button btnUpdate;
    private int showed = 0;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        progressDialog = new ProgressDialog(this);

        txtFullname = findViewById(R.id.txtFullname);
        txtGender = findViewById(R.id.txtGender);
        txtStreet = findViewById(R.id.txtStreet);
        txtCity = findViewById(R.id.txtCity);
        txtProvince = findViewById(R.id.txtProv);
        txtBirthday = findViewById(R.id.txtBirthday);

        btnUpdate = findViewById(R.id.btnUpdate);
        imgProfilePic = findViewById(R.id.imgProfilePic);


        loadUserInfo();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Glide.with(this)
                .load(user.getPhotoUrl().toString())
                .into(imgProfilePic);
        String name = user.getDisplayName();
        txtFullname.setText(name);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, UserUpdateProfile.class));
            }
        });
    }

    public void loadUserInfo(){
        progressDialog.setMessage("Loading user info...");
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();
        //Toast.makeText(this,id.toString(),Toast.LENGTH_LONG).show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refGender = database.getReference(id).child("gender");
        refGender.keepSynced(true);
        refGender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtGender.setText(user.toString());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refStreet = database.getReference(id).child("street");
        refStreet.keepSynced(true);
        refStreet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtStreet.setText(user.toString()+", ");

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refCity = database.getReference(id).child("city");
        refCity.keepSynced(true);
        refCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtCity.setText(user.toString()+", ");
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refProv = database.getReference(id).child("province");
        refProv.keepSynced(true);
        refProv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtProvince.setText(user.toString());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refBday = database.getReference(id).child("birthday");
        refBday.keepSynced(true);
        refBday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                //txtGender.setText(user.getGender());
                System.out.println(user);
                txtBirthday.setText(user.toString());
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().getDisplayName()==null&&firebaseAuth.getCurrentUser().getPhotoUrl()==null){
            finish();
            startActivity(new Intent(UserProfile.this, UserRegister2.class));
            Toast.makeText(this,"Please finish registraion first", Toast.LENGTH_SHORT).show();
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();

//        if(!user.isEmailVerified()){
//            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//            alertBuilder.setMessage("Verify Email to explore more features on this app")
//                    .setCancelable(false)
//                    .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(final DialogInterface dialog, int which) {
//                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(ProfileActivity.this,"Email Verification Sent", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            });
//
//                        }
//                    })
//                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = alertBuilder.create();
//            alert.setTitle("Email Verification");
//            alert.show();
//        }


    }

    public void Logout(View v){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(UserProfile.this, UserLogin.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Logout");
        alert.show();
    }
}
