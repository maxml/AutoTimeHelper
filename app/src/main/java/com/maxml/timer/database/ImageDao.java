package com.maxml.timer.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maxml.timer.BuildConfig;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageDao {
    private StorageReference storageRef;
    private DbController controller;

    public ImageDao(DbController controller) {
        this.controller = controller;
        if (storageRef == null) {
            storageRef = FirebaseStorage.getInstance().getReference();
        }
    }

    public void load(String name) {
        File localFile = null;
        try {
            localFile = File.createTempFile("Images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        storageRef.child(name).getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap image = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        controller.sendImage(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void save(String uri) {
        StorageMetadata metadata = new StorageMetadata
                .Builder()
                .setContentType("image/jpg")
                .build();

        Uri file = Uri.fromFile(new File(uri));
        final String s = "image-" + file.getLastPathSegment().hashCode() + "-" + file.getLastPathSegment();
        UploadTask uploadTask = storageRef.child(s).putFile(file, metadata);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                if (BuildConfig.DEBUG)
                    Log.i(Constants.LOG_TAG, "onProgress " + (int) progress);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (BuildConfig.DEBUG)
                    Log.i(Constants.LOG_TAG, "onSuccess Https " + taskSnapshot.getDownloadUrl());
                if (taskSnapshot.getDownloadUrl() != null) {
                    controller.imageSaved(taskSnapshot.getDownloadUrl().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (BuildConfig.DEBUG)
                    Log.i(Constants.LOG_TAG, "onFailure " + e.toString());
            }
        });
    }

    public void saveBitmap(Bitmap bitmap, String nameImage) {
        final String path = "image-" + nameImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.child(path).putBytes(data);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                if (BuildConfig.DEBUG)
                    Log.i(Constants.LOG_TAG, "onProgress " + (int) progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO:
                if (BuildConfig.DEBUG)
                    Log.i(Constants.LOG_TAG, "onError loading: " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (BuildConfig.DEBUG)
                    Log.i(Constants.LOG_TAG, "image loated");
                if (taskSnapshot.getMetadata() == null ||
                        taskSnapshot.getMetadata().getDownloadUrl() == null) {
                    return;
                }
                String temp = taskSnapshot.getMetadata().getDownloadUrl().toString();
                controller.imageSaved(temp);
            }
        });
    }
}
