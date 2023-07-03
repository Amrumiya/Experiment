package com.example.auth1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditWishActivity extends AppCompatActivity {
    private EditText wishEditText;
    private EditText contactInfoEditText;
    private Button updateWishButton;
    private String wishId; // The ID of the wish to be edited
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wish);

        wishEditText = findViewById(R.id.wish_edit_text);
        contactInfoEditText = findViewById(R.id.contact_info_edit_text);
        updateWishButton = findViewById(R.id.update_wish_button);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        wishId = getIntent().getStringExtra("wishId");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("wishes").child(wishId);

        fetchWishDetails();

        updateWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWish();
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
                    wishEditText.setText(wish.getWish());
                    contactInfoEditText.setText(wish.getContactInfo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void updateWish() {
        String wish = wishEditText.getText().toString();
        String contactInfo = contactInfoEditText.getText().toString();

        mDatabase.child("wish").setValue(wish);
        mDatabase.child("contactInfo").setValue(contactInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditWishActivity.this, "Wish updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(EditWishActivity.this, "Failed to update the wish. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
