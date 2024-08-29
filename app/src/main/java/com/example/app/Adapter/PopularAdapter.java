package com.example.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.Activity.DetailActivity;
import com.example.app.Domain.ItemsDomain;
import com.example.app.databinding.ViewholderPopListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    private ArrayList<ItemsDomain> items;
    private ArrayList<ItemsDomain> itemsFull; // For storing the full list of items
    private Context context;

    public PopularAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
        this.itemsFull = new ArrayList<>(items); // Copy of the original list
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        ItemsDomain currentItem = items.get(position);
        holder.binding.title.setText(currentItem.getTitle());
        holder.binding.reviewTxt.setText("" + currentItem.getReview());
        holder.binding.priceTxt.setText("€" + currentItem.getPrice());
        holder.binding.ratingTxt.setText("(" + currentItem.getRating() + ")");
        holder.binding.oldPriceTxt.setText("€" + currentItem.getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.ratingBar.setRating((float) currentItem.getRating());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(context)
                .load(currentItem.getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filter(String text) {
        if (text.isEmpty()) {
            items.clear();
            items.addAll(itemsFull);
        } else {
            items.clear();
            List<ItemsDomain> filteredList = itemsFull.stream()
                    .filter(item -> item.getTitle().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
            items.addAll(filteredList);
        }
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopListBinding binding;

        public Viewholder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
