package com.example.countsksh.helpers;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Toast;


import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.BaseDirection;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DealingWithPdfFiles {

    //get pdfs path
    public static   String getPdfPath(){
//        String fn=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String filePath= Environment.getExternalStorageDirectory().getAbsolutePath() +"/PicsFolder/";
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return  filePath;
    }

    //get fileName
    public static   String getPdfName(String path,String fileName){
//        String fn=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String file=path+fileName;
        return  file;
    }

    /**
     *
     *
     *  implementation 'com.itextpdf:kernel:7.1.8'
     *     implementation 'com.itextpdf:layout:7.1.8'
     *     implementation 'com.itextpdf:io:7.1.8'
     *
     *      implementation 'com.itextpdf:kernel:8.0.3'
     *           implementation 'com.itextpdf:layout:8.0.3'
     *          implementation 'com.itextpdf:io:8.0.3'
     */

    //make FileOutputStream for the pdf
    public static   FileOutputStream createFileOutputStream(String fileName) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(fileName);
        return fos;
    }

    //create pdf writer
    public static PdfWriter createPdfWriter(FileOutputStream fileOutputStream){
        PdfWriter pdfWriter=new PdfWriter(fileOutputStream);

        return pdfWriter;
    }

    //create pdf document
    public static PdfDocument createPdfDocument(PdfWriter pdfWriter){
        PdfDocument pdfDocument=new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        //Setting some required parameters
        pdfDocument.setTagged();
        pdfDocument.getCatalog().setLang(new PdfString("ar-YE"));
//        pdf.setTagged();
//        pdf.getCatalog().setLang(new PdfString("en-US"));


        return pdfDocument;
    }

    //create document
    public static Document createDocument(PdfDocument pdfDocument){
        Document document = new Document(pdfDocument);

        BaseDirection baseDirection=BaseDirection.DEFAULT_BIDI;
        document.setMargins(24f, 24f, 32f, 32f);
        document.setBaseDirection(baseDirection);

        return document;
    }

    //create Header Table
    public static Table createHeaderTable(String headerTitle){
        Table table=new Table(1);
        table.setTextAlignment(TextAlignment.CENTER);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(headerTitle);
        return table;
    }

    // create Font Program
    public static FontProgram createFontProgram(){
//        String fontPath = "src/main/assets/font/arabic1_font.ttf";
//        FontProgram fontProgram = FontProgramFactory.createFont(fontPath);

//        String fontPath = "assets/font/arbic1_font.ttf";
        String fontPath = "assets/font/arabtype.ttf";
        FontProgram fontProgram = null;

        try {
            fontProgram = FontProgramFactory.createFont(fontPath);

        } catch (IOException e) {
            e.printStackTrace();

        }

        return  fontProgram;

    }



    //function  to open pdf file after creating  it
   public static void openPdfFile(Context context, String fileName)throws IOException{

        File pdfFile=new File(fileName);
        Uri pdfuri=Uri.fromFile(pdfFile);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(pdfuri,"application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
            // toastErrorMessage("Can't read pdf file");
        }
    }






    public static void openImage(Context context, String fileName)throws IOException{

        File imgFile=new File(fileName);
        Uri imgUri=Uri.fromFile(imgFile);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(imgUri,"image/*");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Can't read image file", Toast.LENGTH_SHORT).show();
            // toastErrorMessage("Can't read pdf file");
        }
    }













}
