package com.example.plantstracker.fragments;

import static com.example.plantstracker.utils.Constants.IMAGE_CAPTURE_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.adapter.ImagesAdapter;
import com.example.plantstracker.databinding.FragmentAddRecordBinding;
import com.example.plantstracker.models.PlantRecord;
import com.example.plantstracker.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddRecordFragment extends BottomSheetDialogFragment {
    private FragmentAddRecordBinding binding;
    private Uri imageUri = null;
    private MyDatabaseHelper myDatabaseHelper;
    private ImagesAdapter imagesAdapter;
    private ArrayList<String> imagesList;
    private int plantId;

    private final ActivityResultLauncher<String[]> openCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                boolean granted = true;
                for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                    if (!entry.getValue()) {
                        granted = false;
                        break;
                    }
                }
                if (granted) {
                    openCamera();
                }
            });


    private final ActivityResultLauncher<Intent> registerForActivityResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == IMAGE_CAPTURE_CODE && imageUri != null) {
                    imagesList.add(imageUri.toString());
                    imagesAdapter.updateList(imagesList);
                }
            });


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddRecordBinding.inflate(inflater, container, false);
        plantId = getArguments().getInt("plantId", 0);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListeners();
        binding.cdlMainLayout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    }

    private void init() {
        myDatabaseHelper = new MyDatabaseHelper(requireContext());
        imagesList = new ArrayList<>();

        imagesAdapter = new ImagesAdapter(requireContext());
        binding.rvImages.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvImages.setAdapter(imagesAdapter);
    }

    private void initListeners() {
        binding.ibtClose.setOnClickListener(v -> dismiss());

        binding.tvBtSave.setOnClickListener(v -> {
            String date = Objects.requireNonNull(binding.etSelectDate.getText()).toString();

            binding.tilSelectDate.setError(null);

            if (imageUri == null) {
                Utils.showToast(requireContext(), "Image is required");
            } else if (date.isEmpty()) {
                binding.tilSelectDate.setError("Required");
            } else {
                myDatabaseHelper.insertProgress(new PlantRecord(plantId, date), imagesList);
                Utils.showToast(requireContext(), "Inserted Data Successfully!");
                dismiss();
            }
        });

        binding.tvImage.setOnClickListener(v -> selectImage());
        binding.etSelectDate.setOnClickListener(v -> pickDateAndToast());
    }

    private void selectImage() {
        if (Utils.checkMultiplePermissions(requireContext(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) {
            openCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Utils.showToast(requireContext(), "Allow Camera Permission");
            openCameraPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Utils.showToast(requireContext(), "Allow Write Permission");
            openCameraPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Utils.showToast(requireContext(), "Allow Read Permission");
            openCameraPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        } else {
            openCameraPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        registerForActivityResult.launch(cameraIntent);
    }

    private void pickDateAndToast() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker<Long> datePicker = builder.build();

        // Show the date picker dialog
        datePicker.show(getChildFragmentManager(), "DATE_PICKER");

        // Set the selection listener for the date picker
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Convert the selected timestamp to a Date object
            Date date = new Date(selection);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            String formattedDateWithTime = sdfWithTime.format(date);
            String formattedDate = sdf.format(date);

            Toast.makeText(requireContext(), formattedDateWithTime, Toast.LENGTH_SHORT).show();
            binding.etSelectDate.setText(formattedDate);
        });
    }


    @Override
    public void onDismiss(@NonNull final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
