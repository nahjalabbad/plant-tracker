package com.example.plantstracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.databinding.ItemRecordBinding;
import com.example.plantstracker.models.PlantRecord;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private final Context context;
    private List<PlantRecord> myList = new ArrayList<>();
    private OnItemClick mListener;
    private ImagesAdapter imagesAdapter;
    private MyDatabaseHelper myDatabaseHelper;

    public RecordAdapter(Context context, MyDatabaseHelper myDatabaseHelper) {
        this.context = context;
        this.myDatabaseHelper = myDatabaseHelper;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecordBinding binding;

        public ViewHolder(ItemRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecordBinding binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        imagesAdapter = new ImagesAdapter(context);
        binding.rvImages.setLayoutManager(new LinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, true));
        binding.rvImages.setAdapter(imagesAdapter);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlantRecord item = myList.get(position);
        holder.binding.tvDate.setText(item.getDate());

        imagesAdapter.updateList(myDatabaseHelper.getImageById(item.getId()));

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
        void setOnItemClickListener(PlantRecord item);
    }

    public void setOnClickListener(OnItemClick onItemClick) {
        mListener = onItemClick;
    }

    public void updateList(List<PlantRecord> list) {
        myList = list;
        notifyDataSetChanged();
    }
}