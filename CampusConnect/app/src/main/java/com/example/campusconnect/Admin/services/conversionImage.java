package com.example.campusconnect.Admin.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class conversionImage {
    public static Bitmap convertBitmapToString(String profilePicture) {
        byte[] imageBytes = Base64.decode(profilePicture, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static String convertBitmapToString(Bitmap profilePicture) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(array, Base64.DEFAULT);
    }
}
