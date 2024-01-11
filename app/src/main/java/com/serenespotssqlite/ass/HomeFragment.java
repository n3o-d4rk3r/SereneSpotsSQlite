package com.serenespotssqlite.ass;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeFragment extends Fragment {

    private ImageView imageView;
    private EditText nameEditText, admissionEditText, textEditText;
    private Button submitButton;

    private DatabaseHelper databaseHelper;
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                handleImagePickResult(data.getData());
                            }
                        }
                    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = rootView.findViewById(R.id.imagePickAndView);
        nameEditText = rootView.findViewById(R.id.nameId);
        admissionEditText = rootView.findViewById(R.id.admissionId);
        textEditText = rootView.findViewById(R.id.textId);
        submitButton = rootView.findViewById(R.id.submitBtnId);

        databaseHelper = new DatabaseHelper(getActivity());

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String admissionNo = admissionEditText.getText().toString().trim();
            String text = textEditText.getText().toString().trim();

            if (name.length() < 4 || admissionNo.length() < 4 || text.length() < 4) {
                Toast.makeText(getActivity(), "Please enter at least 4 characters for each field", Toast.LENGTH_SHORT).show();
                return;
            }

            // Store image path instead of byte array
            String imagePath = saveImageToExternalStorage(imageView);

            User user = new User();
            user.setImagePath(imagePath);
            user.setName(name);
            user.setAdmissionNo(admissionNo);
            user.setText(text);

            long result = databaseHelper.addUser(user);
            if (result != -1) {
                Toast.makeText(getActivity(), "Save Successfully", Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(getActivity(), "Failed to Save Data", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
    }

    private String saveImageToExternalStorage(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Save the image to external storage
        return saveBitmapToExternalStorage(bitmap);
    }

    private String saveBitmapToExternalStorage(Bitmap bitmap) {
        String imagePath = ""; // Set the image path where you want to save the image

        try {
            File directory = new File(Environment.getExternalStorageDirectory(), "MyItems");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, "image_" + System.currentTimeMillis() + ".png");
            imagePath = file.getAbsolutePath();

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagePath;
    }

    private void clearForm() {
        imageView.setImageResource(R.drawable.ic_pick);
        nameEditText.getText().clear();
        admissionEditText.getText().clear();
        textEditText.getText().clear();
    }

    private void handleImagePickResult(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, filePathColumn, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pickImageLauncher.unregister();
    }
}
