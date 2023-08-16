package com.example.auth1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyOffersAdapter extends RecyclerView.Adapter<MyOffersAdapter.MyOffersViewHolder> {

    private Context context;
    private List<Offer> offerList;
    private DatabaseReference mProductsDatabaseRef;
    private DatabaseReference mServicesDatabaseRef;

    public MyOffersAdapter(Context context, List<Offer> offerList) {
        this.context = context;
        this.offerList = offerList;
        mProductsDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        mServicesDatabaseRef = FirebaseDatabase.getInstance().getReference("services");
    }

    @NonNull
    @Override
    public MyOffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_offers_item, parent, false);
        return new MyOffersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOffersViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Offer offer = offerList.get(position);
        holder.offerName.setText(offer.getName());
        holder.offerDescription.setText(offer.getDescription());
        holder.offerPhoneNumber.setText(offer.getPhoneNumber());

        Glide.with(context)
                .load(offer.getImageUrl())
                .into(holder.offerImage);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditOfferActivity.class);
                intent.putExtra("offer", offer);
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference;
                if (offer instanceof Product) {
                    reference = FirebaseDatabase.getInstance().getReference("products");
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("services");
                }

                reference.child(offer.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteItem(position);
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish(); // Close the current activity
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public void deleteItem(int position) {
        offerList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyOffersViewHolder extends RecyclerView.ViewHolder {
        TextView offerName, offerDescription, offerPhoneNumber;
        ImageView offerImage;
        Button editButton, deleteButton;

        public MyOffersViewHolder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.offer_name);
            offerDescription = itemView.findViewById(R.id.offer_description);
            offerImage = itemView.findViewById(R.id.offer_image);
            editButton = itemView.findViewById(R.id.offer_edit_button);
            deleteButton = itemView.findViewById(R.id.offer_delete_button);
            offerPhoneNumber = itemView.findViewById(R.id.offer_phone_number);
        }
    }
}