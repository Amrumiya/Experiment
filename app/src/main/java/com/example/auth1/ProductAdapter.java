package com.example.auth1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    List<Product> filteredProductList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.filteredProductList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produc_item_layout, parent, false);
        return new ViewHolder(view);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<Product> filteredList = new ArrayList<>();

                if (query.isEmpty()) {
                    filteredList.addAll(productList);
                } else {
                    for (Product product : productList) {
                        if (product.getProductName().toLowerCase().contains(query) || product.getProductDescription().toLowerCase().contains(query)) {
                            filteredList.add(product);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredProductList.clear();
                filteredProductList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productModel = filteredProductList.get(position);
        holder.productName.setText(productModel.getProductName());
        holder.productDescription.setText(productModel.getProductDescription());
        holder.contactInfo.setText(productModel.getContactInfo());

        // Apply RequestOptions for Glide to display rounded images
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(250)); // Change the value to adjust the corner radius

        Glide.with(context)
                .load(productModel.getImageUrl())
                .apply(requestOptions)
                .into(holder.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);

                String productId = productList.get(holder.getAdapterPosition()).getProductId();
                intent.putExtra("productId", productId);

                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return filteredProductList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productDescription;
        private TextView contactInfo;
        private ImageView productImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.ivProductImage);
            productName = itemView.findViewById(R.id.tvProductName);
            productDescription = itemView.findViewById(R.id.tvProductDescription);
            contactInfo = itemView.findViewById(R.id.tvContactInfo);
        }
    }



}

