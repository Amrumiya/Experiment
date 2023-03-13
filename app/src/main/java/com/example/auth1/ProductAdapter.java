package com.example.auth1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produc_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productModel = productList.get(position);

        holder.tvProductName.setText(productModel.getProductName());
        holder.tvProductDescription.setText(productModel.getProductDescription());
        holder.tvContactInfo.setText(productModel.getContactInfo());

        // Load product image using Glide or any other image loading library
        Glide.with(context)
                .load(productModel.getImageUrl())
                .into(holder.ivProductImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Create a new intent to launch the ProductDetailActivity
                Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);

                // Pass the productId of the clicked item to the ProductDetailActivity
                String productId = productList.get(holder.getAdapterPosition()).getProductId();
                intent.putExtra("productId", productId);

                // Start the ProductDetailActivity
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductDescription;
        private TextView tvContactInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            tvContactInfo = itemView.findViewById(R.id.tvContactInfo);
        }
    }


}

