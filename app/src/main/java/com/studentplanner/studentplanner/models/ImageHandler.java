package com.studentplanner.studentplanner.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageHandler {
    private ImageHandler(){

    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }
    public static Bitmap decodeBitmapByteArray(byte[] imgByte){
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }
}
