package com.example.plantstracker.fragments;

import static com.example.plantstracker.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.example.plantstracker.utils.Constants.PLANT_TYPE;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;

import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.databinding.FragmentAddPlantBinding;
import com.example.plantstracker.models.Plant;
import com.example.plantstracker.utils.Constants;
import com.example.plantstracker.utils.Session;
import com.example.plantstracker.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Map;
import java.util.Objects;

public class AddPlantFragment extends BottomSheetDialogFragment {
    private FragmentAddPlantBinding binding;
    private Uri imageUri = null;
    private ListPopupWindow listPopupWindow;
    private MyDatabaseHelper myDatabaseHelper;
    private Session session;

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
                    binding.tvImage.setVisibility(View.GONE);
                    binding.ivImage.setVisibility(View.VISIBLE);
                    binding.ivImage.setImageURI(imageUri);
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
        binding = FragmentAddPlantBinding.inflate(inflater, container, false);
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
        session = new Session(requireContext());
        listPopupWindow = new ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle);
        // Set button as the list popup's anchor
        listPopupWindow.setAnchorView(binding.acTvPlantType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, PLANT_TYPE);
        listPopupWindow.setAdapter(adapter);
    }

    private void initListeners() {
        binding.ibtClose.setOnClickListener(v -> dismiss());

        binding.tvBtSave.setOnClickListener(v -> {
            String plantName = Objects.requireNonNull(binding.etPlantName.getText()).toString();
            String howToTakeCare = Objects.requireNonNull(binding.etHowToTakeCare.getText()).toString();
            String plantType = Objects.requireNonNull(binding.acTvPlantType.getText()).toString();
            int howManyTimesToChangeSoil = 0;
            int howManyTimesToWater = 0;
            try {
                howManyTimesToWater = Integer.parseInt(Objects.requireNonNull(binding.etHowManyTimesToWater.getText()).toString());
                howManyTimesToChangeSoil = Integer.parseInt(Objects.requireNonNull(binding.etHowManyTimesToChangeSoil.getText()).toString());
            } catch (NumberFormatException exception) {
                Log.d(Constants.DEBUGGING, exception.getMessage());
            }
            int doesRequireDirectSunlight;
            if (binding.cbDoesRequireDirectSunlight.isChecked())
                doesRequireDirectSunlight = 1;
            else
                doesRequireDirectSunlight = 0;

            binding.tilPlantName.setError(null);
            binding.tilHowToTakeCare.setError(null);
            binding.tilHowManyTimesToWater.setError(null);
            binding.tilHowManyTimesToChangeSoil.setError(null);
            binding.tilPlantType.setError(null);

            if (imageUri == null) {
                Utils.showToast(requireContext(), "Image is required");
            } else if (plantName.isEmpty()) {
                binding.tilPlantName.setError("Required");
            } else if (howToTakeCare.isEmpty()) {
                binding.tilHowToTakeCare.setError("Required");
            } else if (howManyTimesToWater == 0) {
                binding.tilHowManyTimesToWater.setError("Required");
            } else if (howManyTimesToChangeSoil == 0) {
                binding.tilHowManyTimesToChangeSoil.setError("Required");
            } else if (plantType.isEmpty()) {
                binding.tilPlantType.setError("Required");
            } else {
                myDatabaseHelper.insertPlant(new Plant(0, session.getUserId(), plantName, imageUri.toString(), howToTakeCare, howManyTimesToWater, howManyTimesToChangeSoil, doesRequireDirectSunlight, plantType, null, null));
                Utils.showToast(requireContext(), "Inserted Data Successfully!");
                dismiss();
            }
        });

        binding.tvImage.setOnClickListener(v -> selectImage());

        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            binding.acTvPlantType.setText(PLANT_TYPE[position], false);
            listPopupWindow.dismiss();
        });

        binding.acTvPlantType.setOnClickListener(v -> {
            listPopupWindow.show();
        });
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

    @Override
    public void onDismiss(@NonNull final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
