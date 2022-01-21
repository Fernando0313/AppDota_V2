package com.example.app_dota;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;



public class UploadImg extends AppCompatActivity {

    ImageView imageProfile;
    ProgressDialog progressDialog;
    private  static final int CAMERA_REQUEST = 100;
    private static  final int IMAGE_PICK_CAMERA_REQUEST = 400;
    private static final int PICK_IMAGE = 1;
    String cameraPermission[];
    Uri imageUri;
    String profileOrCoverImage;
    MaterialButton editImage;
    MaterialButton btn_save;
    TextInputLayout tl_nombre;
    TextInputEditText te_nombre;
    TextView nombre;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);
        FirebaseApp.initializeApp(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);







        editImage = findViewById(R.id.edit_imagen);
        imageProfile = findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        nombre = findViewById(R.id.name);


        te_nombre = findViewById(R.id.txt_editName);




        tl_nombre = findViewById(R.id.text_layout_name);



        btn_save = findViewById(R.id.btn_save);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating Profile Picture");
                profileOrCoverImage = "image";
               showImagePicDialog();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                        tl_nombre.requestFocus();


                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference folderRef = storageRef.child("Heroes_IMG");
                        StorageReference fotoRef = folderRef.child(te_nombre.getText().toString());
                        fotoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uriTask.isSuccessful());
                                Uri dowloadUri = uriTask.getResult();

                            }
                        });
                        Toast.makeText(UploadImg.this, "Registro guardado",Toast.LENGTH_SHORT).show();
                        te_nombre.setText("");
                    }





        });

    }

    @Override
    protected  void  onPause(){
        super.onPause();
        Glide.with(this).load(imageUri).into(imageProfile);
    }


    @Override
    public  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        switch (requestCode){
            case CAMERA_REQUEST: {
                if(grantResult.length > 0) {
                    boolean cameraAccepted = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResult[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this,"Please enable camera and storage permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Something went wrong! try again...", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }


    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which ==0) {
                    if(!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                        //pickPhotoFromGallery();
                    }
                }
                else if(which == 1){
                    if(!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        //pickFromCamera();
                        pickPhotoFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }


    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void  requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");
        imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST);
    }





    private void pickPhotoFromGallery(){

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        //img2 = intent.getData();
        startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){

            /*Uri path =data.getData();
            Glide.with(this).load(path).into(imageProfile);
            //imageProfile.setImageURI(path);
            Toast.makeText(UploadImg.this,"Todo ok bb",Toast.LENGTH_SHORT).show();*/
            switch (requestCode){
                case PICK_IMAGE:
                    Uri path =data.getData();
                    Glide.with(this).load(path).into(imageProfile);
                    imageUri = path;
                    break;

                case IMAGE_PICK_CAMERA_REQUEST:
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
                    contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");
                    imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);



                    break;
            }
        }
    }













    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                action(R.string.action_add);
                Intent intent = new Intent(UploadImg.this, UploadImg.class);
                startActivity(intent);
                finish();
                Toast.makeText(UploadImg.this,"Bienvenido",Toast.LENGTH_SHORT).show();


            case R.id.action_settings:
                action(R.string.action_settings);
                return true;
            case R.id.action_help:
                action(R.string.action_help);
                return true;
            case R.id.action_about:
                action(R.string.action_about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void action(int resid) {
        Toast.makeText(this, getText(resid), Toast.LENGTH_SHORT).show();
    }

}