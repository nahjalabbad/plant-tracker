package com.example.plantstracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.databinding.ActivityRegisterBinding;
import com.example.plantstracker.models.User;
import com.example.plantstracker.utils.Utils;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
    }

    private void init() {
        myDatabaseHelper = new MyDatabaseHelper(this);
        myDatabaseHelper.insertAdmin();
    }

    private void initListeners() {
        binding.btLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
            String confirmPassword = Objects.requireNonNull(binding.etConfirmPassword.getText()).toString().trim();
            String name = Objects.requireNonNull(binding.etName.getText()).toString().trim();

            binding.tiName.setError(null);
            binding.tiEmail.setError(null);
            binding.tiPassword.setError(null);
            binding.tiConfirmPassword.setError(null);

            if (name.isEmpty())
                binding.tiName.setError("Required");
            else if (email.isEmpty())
                binding.tiEmail.setError("Required");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                binding.tiEmail.setError("Please enter valid email");
            else if (password.isEmpty())
                binding.tiPassword.setError("Required");
            else if (confirmPassword.isEmpty())
                binding.tiConfirmPassword.setError("Required");
            else if (!password.equals(confirmPassword))
                binding.tiConfirmPassword.setError("Password & Confirm Password doesn't match!");
            else if (myDatabaseHelper.doesUserExists(email))
                Utils.showToast(this, "User already exists!");
            else {
                if (myDatabaseHelper.insertUser(new User(name, email, password))) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    Utils.showToast(this, "User Created Successfully!");
                }
                else
                    Utils.showToast(this, "Failed to Create User!");
            }
        });
    }
}