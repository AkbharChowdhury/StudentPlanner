package com.studentplanner.studentplanner.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

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
    public static void showImage(final byte[] image, ImageView imageView){
        if (image !=null){
            imageView.setImageBitmap(ImageHandler.decodeBitmapByteArray(image));
            imageView.setVisibility(View.VISIBLE);
        }
    }

}
