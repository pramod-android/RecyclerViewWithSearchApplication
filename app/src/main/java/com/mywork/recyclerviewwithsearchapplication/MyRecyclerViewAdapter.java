package com.mywork.recyclerviewwithsearchapplication;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Product> mData;

    private List<Product> mFilteredList;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context mContext;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<Product> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mFilteredList=data;
        mContext=context;
    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtProductName.setText(mFilteredList.get(position).getProductName());
        holder.txtPrice.setText(String.valueOf(mFilteredList.get(position).getPrice()));
        holder.txtQty.setText(String.valueOf(mFilteredList.get(position).getQuantity()));
        holder.txtUnit.setText(mFilteredList.get(position).getUnit());

        Picasso.with(mContext).load(mFilteredList.get(position).getImageUrl()).fit().into(holder.imgProductImage);
    }
    // total number of rows
    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtProductName,txtPrice,txtQty,txtUnit;

        ImageView imgProductImage;

        ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.tvName);
            txtPrice = itemView.findViewById(R.id.tvPrice);
            txtQty = itemView.findViewById(R.id.tvQty);
            txtUnit = itemView.findViewById(R.id.tvUnit);
            imgProductImage=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).getProductName();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mData;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product product : mData) {
                        if (product.getProductName().toLowerCase().contains(charString) ) {
                            filteredList.add(product);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}