package com.example.auth1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        // Get the productId from the intent
        String productId = getIntent().getStringExtra("productId");

        // Get a reference to the Firebase Realtime Database
        Query databaseReference = FirebaseDatabase.getInstance().getReference("products").child(productId);

        // Attach a ValueEventListener to retrieve the details of the selected product
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the details of the selected product
                String productName = dataSnapshot.child("productName").getValue(String.class);
                String productDescription = dataSnapshot.child("productDescription").getValue(String.class);
                String imageURL = dataSnapshot.child("imageUrl").getValue(String.class);
                // TODO: Get other details of the product if needed

                // Update the views with the product details
                TextView productNameTextView = findViewById(R.id.product_name_text_view);
                TextView productDescriptionTextView = findViewById(R.id.product_description_text_view);
                ImageView productImageView = findViewById(R.id.product_image_view);
                // TODO: Get other views and update them with the product details

                productNameTextView.setText(productName);
                productDescriptionTextView.setText(productDescription);

                Glide.with(ProductDetailActivity.this)
                        .load(imageURL)
                        .into(productImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
}