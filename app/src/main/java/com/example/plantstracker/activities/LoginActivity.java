package com.example.plantstracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.databinding.ActivityLoginBinding;
import com.example.plantstracker.models.User;
import com.example.plantstracker.utils.Session;
import com.example.plantstracker.utils.Utils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private MyDatabaseHelper myDatabaseHelper;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
    }

    private void init(){
        myDatabaseHelper = new MyDatabaseHelper(this);
        session = new Session(this);
        myDatabaseHelper.insertAdmin();

        // service
        Utils.stopService(this);
    }

    private void initListeners() {
        binding.btLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

            binding.tiEmail.setError(null);
            binding.tiPassword.setError(null);

            if (email.isEmpty())
                binding.tiEmail.setError("Required");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                binding.tiEmail.setError("Please enter valid email");
            else if (password.isEmpty())
                binding.tiPassword.setError("Required");
            else{
                User user = myDatabaseHelper.getUser(email, password);
                if (user != null) {
                    if (user.getName().equals("Admin"))
                        myDatabaseHelper.insertPlants(user.getId());
                    session.setLoggedIn(true);
                    session.setUserName(user.getName());
                    session.setUserEmail(user.getEmail());
                    session.setUserId(user.getId());
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                else
                    Utils.showToast(this, "User doesn't exist!");
            }
        });
        binding.tvGoToRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}