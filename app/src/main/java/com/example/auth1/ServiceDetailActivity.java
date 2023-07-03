package com.example.auth1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ServiceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String serviceId = getIntent().getStringExtra("serviceId");

        Query databaseReference = FirebaseDatabase.getInstance().getReference("services").child(serviceId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String serviceName = dataSnapshot.child("serviceName").getValue(String.class);
                String serviceDescription = dataSnapshot.child("serviceDescription").getValue(String.class);
                String contactInfo = dataSnapshot.child("contactInfo").getValue(String.class);
                String imageURL = dataSnapshot.child("imageUrl").getValue(String.class);

                TextView serviceNameTextView = findViewById(R.id.service_name_text_view);
                TextView serviceDescriptionTextView = findViewById(R.id.service_description_text_view);
                TextView contactInfoTextView = findViewById(R.id.contact_info_text_view);
                ImageView serviceImageView = findViewById(R.id.service_image_view);

                serviceNameTextView.setText(serviceName);
                serviceDescriptionTextView.setText(serviceDescription);
                contactInfoTextView.setText(contactInfo);

                if (imageURL != null) {
                    Glide.with(ServiceDetailActivity.this)
                            .load(imageURL)
                            .into(serviceImageView);
                } else {
                    // Set a default image or handle the null case as needed
                }

                Button contactButton = findViewById(R.id.contact_button);
                contactButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phoneNumber = dataSnapshot.child("contactInfo").getValue(String.class);
                        String message = "Hello! I am interested in your service: " + serviceName;
                        openWhatsApp(phoneNumber, message);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
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
