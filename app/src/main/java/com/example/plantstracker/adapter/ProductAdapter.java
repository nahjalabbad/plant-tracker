package com.example.plantstracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plantstracker.R;
import com.example.plantstracker.databinding.ItemProductBinding;
import com.example.plantstracker.models.Plant;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final Context context;
    private List<Plant> myList = new ArrayList<>();
    private OnItemClick mListener;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Plant item = myList.get(position);
        Glide.with(context).load(item.getImage()).placeholder(R.drawable.ic_plant).into(holder.binding.ivImage);
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvHowToTakeCare.setText(item.getHowToTakeCare());
        holder.binding.tvHowManyTimesToWater.setText(String.valueOf(item.getHowManyTimesToWater()));
        holder.binding.tvHowManyTimesToChangeSoil.setText(String.valueOf(item.getHowManyTimesToChangeSoil()));
        holder.binding.tvType.setText(String.valueOf(item.getType()));
        if (item.getIsRequiredDirectSunLight() == 0)
            holder.binding.tvDoesRequireDirectSunlight.setText(context.getString(R.string.no));
        else
            holder.binding.tvDoesRequireDirectSunlight.setText(context.getString(R.string.yes));


        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.setOnItemClickListener(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public interface OnItemClick {
        void setOnItemClickListener(Plant item);
    }

    public void setOnClickListener(OnItemClick onItemClick) {
        mListener = onItemClick;
    }

    public void updateList(List<Plant> list) {
        myList = list;
        notifyDataSetChanged();
    }
}