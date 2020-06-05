package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddImage_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button selectButton,saveButton,displyButton;
    private EditText imageNameEditText;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_);

        databaseReference = FirebaseDatabase.getInstance().getReference("Image");
        storageReference = FirebaseStorage.getInstance().getReference("Image");

        selectButton = findViewById(R.id.selectImageButtonId);
        saveButton = findViewById(R.id.SaveImageButtonId);
        displyButton= findViewById(R.id.DisplyImageButtonId);

        imageNameEditText= findViewById(R.id.imageNameEditTextId);
        imageView= findViewById(R.id.imageViewId);
        progressBar= findViewById(R.id.progressbarID);

        selectButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        displyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.selectImageButtonId:
                openFileChooser();
                break;

            case R.id.SaveImageButtonId:
                if (uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),"Upload in Progress",Toast.LENGTH_LONG).show();
                }else {
                    saveData();
                }
                break;

            case R.id.DisplyImageButtonId:

                break;
        }

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }

    //Getting file extension
    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void saveData() {

        final String imageName = imageNameEditText.getText().toString().trim();

        if (imageName.isEmpty()){
            imageNameEditText.setError("Enter the image name");
            imageNameEditText.requestFocus();
            return;
        }

        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                        UploadImage uploadImage = new UploadImage(imageName,taskSnapshot.getStorage().getDownloadUrl().toString());
                        String imageId = databaseReference.push().getKey();
                        databaseReference.child(imageId).setValue(uploadImage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
