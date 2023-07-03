package com.example.auth1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.WishViewHolder> {

    private Context context;
    private List<Wish> wishes;
    private OnWishClickListener onWishClickListener;

    public WishAdapter(Context context, List<Wish> wishes, OnWishClickListener onWishClickListener) {
        this.context = context;
        this.wishes = wishes;
        this.onWishClickListener = onWishClickListener;
    }

    @NonNull
    @Override
    public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wish_item, parent, false);
        return new WishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishViewHolder holder, int position) {
        Wish wish = wishes.get(position);
        holder.wishText.setText(wish.getWish());
        holder.contactInfoText.setText(wish.getContactInfo());
    }

    @Override
    public int getItemCount() {
        return wishes.size();
    }

    class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView wishText;
        TextView contactInfoText;

        public WishViewHolder(@NonNull View itemView) {
            super(itemView);
            wishText = itemView.findViewById(R.id.wish_text);
            contactInfoText = itemView.findViewById(R.id.contact_info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onWishClickListener.onWishClick(getAdapterPosition());
        }
    }

    public interface OnWishClickListener {
        void onWishClick(int position);
    }
}
