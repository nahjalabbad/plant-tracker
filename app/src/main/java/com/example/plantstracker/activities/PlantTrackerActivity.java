package com.example.plantstracker.activities;

import static com.example.plantstracker.utils.Constants.ADD_PROGRESS;
import static com.example.plantstracker.utils.Constants.CHANGE_SOIL;
import static com.example.plantstracker.utils.Constants.WATER_THE_PLANT;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.R;
import com.example.plantstracker.adapter.RecordAdapter;
import com.example.plantstracker.databinding.ActivityPlantTrackerBinding;
import com.example.plantstracker.fragments.AddRecordFragment;
import com.example.plantstracker.models.Plant;
import com.example.plantstracker.models.PlantRecord;
import com.example.plantstracker.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlantTrackerActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private ActivityPlantTrackerBinding binding;
    private MyDatabaseHelper myDatabaseHelper;
    private int id;
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
    }

    private void init() {
        setSupportActionBar(binding.topAppBar);
        myDatabaseHelper = new MyDatabaseHelper(this);
        id = getIntent().getIntExtra("id", 0);

        recordAdapter = new RecordAdapter(this, myDatabaseHelper);
        binding.rvRecords.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecords.setAdapter(recordAdapter);

        setData();
    }

    private void initListeners() {
        binding.topAppBar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );
        binding.fabAdd.setOnClickListener(v -> selectDialog());
    }

    private void setData() {
        Plant plant = myDatabaseHelper.getProductById(id);
        ArrayList<PlantRecord> plantRecords = new ArrayList<>();
        PlantRecord plantRecord = myDatabaseHelper.getPlantRecordById(id);
        if (plantRecord != null)
            plantRecords.add(plantRecord);
        Glide.with(this).load(plant.getImage()).into(binding.ivImage);
        String wateredDate;
        String changedSoilDate;
        if (plant.getWateredDate() == null)
            wateredDate = "Never";
        else
            wateredDate = Utils.getTimeDifferenceFromWateredDate(plant.getWateredDate());
        if (plant.getChangedSoilDate() == null)
            changedSoilDate = "Never";
        else
            changedSoilDate = Utils.getTimeDifferenceFromWateredDate(plant.getChangedSoilDate());


        binding.tvWateredDate.setText(wateredDate);
        binding.tvChangedSoilDate.setText(changedSoilDate);
        recordAdapter.updateList(plantRecords);
    }

    private void selectDialog() {
        final String[] selected = {WATER_THE_PLANT};
        List<String> selections = Arrays.asList(WATER_THE_PLANT, CHANGE_SOIL, ADD_PROGRESS);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(getResources().getString(R.string.select))
                .setNeutralButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                    // Respond to neutral button press
                })
                .setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
                    switch (selected[0]) {
                        case WATER_THE_PLANT:
                            myDatabaseHelper.updateWateredDateToCurrent(id);
                            setData();
                            break;
                        case CHANGE_SOIL:
                            myDatabaseHelper.updateChangedSoilDateToCurrent(id);
                            setData();
                            break;
                        case ADD_PROGRESS:
                            AddRecordFragment addRecordFragment = new AddRecordFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("plantId", id);
                            addRecordFragment.setArguments(bundle);
                            addRecordFragment.show(getSupportFragmentManager(), addRecordFragment.getTag());
                            break;
                        default:
                            break;
                    }
                })
                .setSingleChoiceItems(selections.toArray(new String[0]), 0, (dialog, which) -> selected[0] = selections.get(which))
                .show();
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        setData();
    }

}