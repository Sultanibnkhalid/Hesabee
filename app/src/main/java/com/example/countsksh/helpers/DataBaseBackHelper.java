package com.example.countsksh.helpers;




import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataBaseBackHelper {

//    private String lastBackNam=;
    public static  File targetDBPath=new File( Environment.getExternalStorageDirectory()+ "/HesabeeDB");
    public static File  currentDBPath=new File(Environment.getDataDirectory()+"/data/com.example.countsksh/databases");
    public static String currentDBName="sksh.db";
     private void checkTargetPath(){

    }


    /**
     * make backup for the current db
     * @return type string
     * like a massage
     */

    public static String makeBackup() {

        try {


            String errorText = "";
            String fn = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = fn + ".db";
            if (!targetDBPath.exists()) {
                targetDBPath.mkdirs();
            }

            File targetDB = new File(targetDBPath, fileName);
            File currentDB = new File(currentDBPath, currentDBName);

                if (currentDBPath.exists()) {
                    FileChannel srcDB = new FileInputStream(currentDB).getChannel();
                    FileChannel dstDB = new FileOutputStream(targetDB).getChannel();
                    dstDB.transferFrom(srcDB, 0, srcDB.size());
                    srcDB.close();
                    dstDB.close();

                    return targetDB.getAbsolutePath();
                }


        }catch (Exception e){
            return e.toString();
        }
        return currentDBPath.toString();

    }









    /**
     * restore backup db for the current db
     * @return type string
     * like a massage
     */
    public static String restoreBackup(String path) {


        String errorText="";;
//        if (!currentDBPath.canWrite()) {
            File targetDB = new File(Environment.getExternalStorageDirectory(),path);
            File currentDB=new File(currentDBPath,currentDBName);
            try {
                if (currentDB.exists()) {
                    FileChannel srcDB = new FileInputStream(targetDB).getChannel();
                    FileChannel dstDB = new FileOutputStream(currentDB).getChannel();
                    dstDB.transferFrom(srcDB, 0, srcDB.size());
                    srcDB.close();
                    dstDB.close();
                    return "success";

                }
            } catch (Exception e) {
                e.printStackTrace();

                return e.toString();
            }
//        }

        return "some thing wrong";
    }
/*

thi
 */


























}
