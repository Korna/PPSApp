package com.coma.go.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

/**
 * Created by Koma on 17.03.2018.
 */

public class UploadImage {
    //Firebase

    public UploadImage(){
      //  storage = FirebaseStorage.getInstance();
      //  storageReference = storage.getReference();
    }

    public @Nullable String uploadImage(Uri filePath, Activity activity) {

        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        String uuiid = null;
        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            uuiid = UUID.randomUUID().toString();

            StorageReference ref = storageReference.child("images/"+ uuiid);

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        ViewUtils.showToast(activity,"Loaded");
                        taskSnapshot.getDownloadUrl();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        ViewUtils.showToast(activity,"Failed:" + e.getMessage());
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                                taskSnapshot.getTotalByteCount());

                        progressDialog.setMessage("Uploaded "+ (int) progress+"%");
                    });

        }
        return uuiid;
    }
}

