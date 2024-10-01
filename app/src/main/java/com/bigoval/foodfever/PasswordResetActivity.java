package com.bigoval.foodfever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PasswordResetActivity extends AppCompatActivity {
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Uri uri = getIntent().getData();
        Intent intent = getIntent();
        phonenumber=intent.getStringExtra("phonenumber");
        System.out.println(phonenumber);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString("phonenumber",phonenumber);
            Fragment fragment = new passwordresetotpfragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container3, fragment).commit();

        }
    }
}