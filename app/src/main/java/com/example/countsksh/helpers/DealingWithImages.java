package com.example.countsksh.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DealingWithImages {
    //function to save images taken by camera to PicsFolder in storage
   public static String saveImageToFile(Bitmap imageToSave) {
       String fn=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
       String fileName="IMG"+fn+".jpg ";
        File directory = new File(Environment.getExternalStorageDirectory()+ "/PicsFolder");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }



   //    //function to retrieve saved images taken from PicsFolder in storage
    //not in use
   public static Bitmap getImageFromFile(String fileName){
                    File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/PicsFolder/"+fileName);
                    Log.d("path", dir.toString());
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inPreferredConfig=Bitmap.Config.ARGB_8888;
                    Bitmap bitmap=BitmapFactory.decodeFile(String.valueOf(dir), options);
                    return bitmap;
    }

}
