package com.example.auth1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLogout(View view) {
        FirebaseAuth.getInstance().signOut(); // logout
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }

    public void onClickSell(View view) {
        Intent intent = new Intent(this, SellActivity.class);
        startActivity(intent);
    }

    public void onClickBuy(View view) {
        Intent intent = new Intent(this, BuyActivity.class);
        startActivity(intent);
    }

}
