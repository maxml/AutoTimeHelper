package com.maxml.timer.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

    public static File fPhoto;

    public static Intent createIntentForLoadImage(Context context) {
        try {
            fPhoto = createTempImageFile(context.getExternalCacheDir());

            List<Intent> intentList = new ArrayList<>();
            Intent chooserIntent = null;

            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            takePhotoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fPhoto));

            intentList = addIntentsToList(context, intentList, pickIntent);
            intentList = addIntentsToList(context, intentList, takePhotoIntent);

            if (!intentList.isEmpty()) {
                chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "Choose your image source");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
            }

            return chooserIntent;
        } catch (IOException e) {
            Log.e(Constants.TAG, e.getMessage());
        }
        return null;
    }

    private static File createTempImageFile(File storageDir) throws IOException {
        String imageFileName = "profile_photo";

        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }
}
