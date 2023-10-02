package com.example.plantstracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.R;
import com.example.plantstracker.adapter.ProductAdapter;
import com.example.plantstracker.databinding.ActivityMainBinding;
import com.example.plantstracker.fragments.AddPlantFragment;
import com.example.plantstracker.models.Plant;
import com.example.plantstracker.utils.Session;
import com.example.plantstracker.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private ActivityMainBinding binding;
    private MyDatabaseHelper myDatabaseHelper;
    private ProductAdapter productAdapter;
    private Session session;
    private ArrayList<Plant> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
    }

    private void init(){
        setSupportActionBar(binding.topAppBar);
        myDatabaseHelper = new MyDatabaseHelper(this);
        session = new Session(this);

        //set adapter
        productAdapter = new ProductAdapter(this);
        binding.rvPlants.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPlants.setAdapter(productAdapter);

        // service
        Utils.startService(this);
    }

    private void initListeners() {
        plantList = myDatabaseHelper.getAllPlants(session.getUserId());
        productAdapter.updateList(plantList);

        binding.fabAdd.setOnClickListener(v -> {
            AddPlantFragment addPlantFragment = new AddPlantFragment();
            addPlantFragment.show(getSupportFragmentManager(), addPlantFragment.getTag());
        });
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.item_logout){
                session.clear();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            return false;
        });
        productAdapter.setOnClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, PlantTrackerActivity.class);
            intent.putExtra("id", item.getId());
            startActivity(intent);
        });
    }

    //for top app bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plants, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        plantList.clear();
        plantList.addAll(myDatabaseHelper.getAllPlants(session.getUserId()));
        productAdapter.updateList(plantList);
        // service
        Utils.startService(this);
    }
}