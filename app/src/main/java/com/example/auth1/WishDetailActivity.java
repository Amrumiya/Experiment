package com.example.auth1;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WishDetailActivity extends AppCompatActivity {

    private TextView wishText;
    private TextView contactInfoText;
    private DatabaseReference mDatabase;
    private Button editWishButton;
    private Button deleteWishButton;
    private String currentUserId; // Logged in user's ID


    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_detail);

        wishText = findViewById(R.id.wish_text);
        contactInfoText = findViewById(R.id.contact_info_text);
        editWishButton = findViewById(R.id.edit_wish_button);
        deleteWishButton = findViewById(R.id.delete_wish_button);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String wishId = getIntent().getStringExtra("wishId");
        mDatabase = FirebaseDatabase.getInstance().getReference("wishes").child(wishId);

        Button contactButton = findViewById(R.id.contact_button);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String phoneNumber = dataSnapshot.child("contactInfo").getValue(String.class);
                        String wish = dataSnapshot.child("wish").getValue(String.class);
                        String message = "Hello! I am interested in your wish: " + wish;
                        openWhatsApp(phoneNumber, message);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });
            }
        });

        //TODO
        // Assuming that you are using Firebase Authentication

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchWishDetails();

        deleteWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWish();
            }
        });

        editWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWish();
            }
        });
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

    private void fetchWishDetails() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wish wish = dataSnapshot.getValue(Wish.class);
                if (wish != null) {
                    wishText.setText(wish.getWish());
                    contactInfoText.setText(wish.getContactInfo());
                    if (wish.getUserId() != null && wish.getUserId().equals(currentUserId)) {
                        // If the wish belongs to the logged in user, show the Edit and Delete buttons
                        editWishButton.setVisibility(View.VISIBLE);
                        deleteWishButton.setVisibility(View.VISIBLE);
                    } else {
                        // If the wish doesn't belong to the logged in user, hide the Edit and Delete buttons
                        editWishButton.setVisibility(View.GONE);
                        deleteWishButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void deleteWish() {
        mDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(WishDetailActivity.this, "Wish deleted successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(WishDetailActivity.this, "Failed to delete the wish. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editWish() {
        Intent intent = new Intent(WishDetailActivity.this, EditWishActivity.class);
        intent.putExtra("wishId", mDatabase.getKey()); // Pass the ID of the wish to be edited
        startActivity(intent);
    }


    private void openWhatsApp(String phoneNumber, String message) {
        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

