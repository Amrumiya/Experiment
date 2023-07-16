package com.example.auth1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddWishActivity extends AppCompatActivity {


    private EditText editTextWish;
    private EditText editTextContactInfo;
    private DatabaseReference mDatabase;
    private String currentUserId; // Logged in user's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextWish = findViewById(R.id.edit_text_wish);
        editTextContactInfo = findViewById(R.id.edit_text_contact_info);

        // Assuming that you are using Firebase Authentication
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("wishes");
    }

    // Handle the back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickAdd(View view) {
        String wish = editTextWish.getText().toString();
        String contactInfo = editTextContactInfo.getText().toString();

        if (TextUtils.isEmpty(wish) || TextUtils.isEmpty(contactInfo)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        } else {
            String id = mDatabase.push().getKey();
            Wish newWish = new Wish(id, wish, contactInfo, currentUserId); // Include the user ID in the constructor call
            mDatabase.child(id).setValue(newWish);

            Toast.makeText(this, "Wish added successfully", Toast.LENGTH_SHORT).show();

            // Return to WishListActivity
            Intent intent = new Intent(this, WishListActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
