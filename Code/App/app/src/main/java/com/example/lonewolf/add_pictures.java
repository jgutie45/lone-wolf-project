package com.example.lonewolf;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class add_pictures extends AppCompatActivity {

    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pictures);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);

        //btnChoose.setOnClickListener(btnChooseListener);
        //btnUpload.setOnClickListener(btnUploadListener);

        btnChoose.setOnClickListener(v -> chooseImage());

        btnUpload.setOnClickListener(v -> uploadImage());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }  //end onCreate

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    } //end onActivityResult


    //upload image to firebase
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(add_pictures.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(add_pictures.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    });
        }
    } //end uploadImage

}  //end class add_pictures
