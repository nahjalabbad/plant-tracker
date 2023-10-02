package com.example.plantstracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plantstracker.R;
import com.example.plantstracker.databinding.ItemImageBinding;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private final Context context;
    private List<String> myList = new ArrayList<>();
    private OnItemClick mListener;

    public ImagesAdapter(Context context) {
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemImageBinding binding;

        public ViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = myList.get(position);
        Glide.with(context).load(item).placeholder(R.drawable.ic_plant).into(holder.binding.ivImage);

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
        void setOnItemClickListener(Object item);
    }

    public void setOnClickListener(OnItemClick onItemClick) {
        mListener = onItemClick;
    }

    public void updateList(List<String> list) {
        myList = list;
        notifyDataSetChanged();
    }
}