package com.example.auth1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishListActivity extends AppCompatActivity implements WishAdapter.OnWishClickListener {

    private RecyclerView wishesRecyclerView;
    private WishAdapter wishAdapter;
    private List<Wish> wishList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        wishesRecyclerView = findViewById(R.id.wishes_recyclerview);
        wishesRecyclerView.setHasFixedSize(true);
        wishesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        wishList = new ArrayList<>();
        wishAdapter = new WishAdapter(this, wishList, this);
        wishesRecyclerView.setAdapter(wishAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("wishes");
        fetchWishes();
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

    private void fetchWishes() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wishList.clear();
                for (DataSnapshot wishSnapshot : dataSnapshot.getChildren()) {
                    Wish wish = wishSnapshot.getValue(Wish.class);
                    wishList.add(wish);
                }
                wishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    public void onClickAddWish(View view) {
        Intent intent = new Intent(this, AddWishActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWishClick(int position) {
        Wish clickedWish = wishList.get(position);
        Intent intent = new Intent(this, WishDetailActivity.class);
        intent.putExtra("wishId", clickedWish.getId());
        startActivity(intent);
    }
}
