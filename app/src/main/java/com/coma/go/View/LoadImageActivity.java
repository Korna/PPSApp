package com.coma.go.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.coma.go.Utils.ViewUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public abstract class LoadImageActivity extends AppCompatActivity {








    private final int PICK_IMAGE_REQUEST = 71;
    protected Uri filePath;
    protected String image = null;

    protected void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            uploadImage(filePath);

        }
    }


    private boolean uploadImage(Uri filePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String uuiid = UUID.randomUUID().toString();

            StorageReference ref = storageReference.child("images/" + uuiid);

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                       // ViewUtils.showToast(activity,"Loaded");
                        image = taskSnapshot.getDownloadUrl().toString();
                        loadImage(filePath);//загрузка из файловой системы а не с сервера

                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        ViewUtils.showToast(this,"Failed:" + e.getMessage());
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                                taskSnapshot.getTotalByteCount());

                        progressDialog.setMessage("Uploaded "+ (int) progress+"%");
                    });

        }else
            return false;

        return true;
    }

    protected abstract void loadImage(String path);
    protected abstract void loadImage(Uri path);

}
