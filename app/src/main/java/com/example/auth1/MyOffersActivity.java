package com.example.auth1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOffersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyOffersAdapter mAdapter;

    private DatabaseReference mProductsDatabaseRef;
    private DatabaseReference mServicesDatabaseRef;
    private List<Offer> mOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.my_offers_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mOffers = new ArrayList<>();
        mAdapter = new MyOffersAdapter(MyOffersActivity.this, mOffers);
        mRecyclerView.setAdapter(mAdapter);

        // Initialize Firebase
        mProductsDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        mServicesDatabaseRef = FirebaseDatabase.getInstance().getReference("services");

        // Filter offers by user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Fetch products data from Firebase
        mProductsDatabaseRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    if (product != null) {
                        product.setId(postSnapshot.getKey());
                        product.setPhoneNumber(postSnapshot.child("contactInfo").getValue(String.class)); // Add this line to set the phone number
                        mOffers.add(product);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyOffersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch services data from Firebase
        mServicesDatabaseRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service service = postSnapshot.getValue(Service.class);
                    if (service != null) {
                        service.setId(postSnapshot.getKey());
                        service.setPhoneNumber(postSnapshot.child("contactInfo").getValue(String.class)); // Add this line to set the phone number
                        mOffers.add(service);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyOffersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
}
