package com.example.countsksh.activities;

import static com.itextpdf.styledxmlparser.css.CommonCssConstants.FONT;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countsksh.R;
import com.example.countsksh.adapters.TotalAmountReportAdapter;
import com.example.countsksh.helpers.DataBaseAccess;
import com.example.countsksh.helpers.DealingWithPdfFiles;
import com.example.countsksh.models.AccountsModel;
import com.example.countsksh.models.DealsModel;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import com.itextpdf.kernel.font.PdfFontFactory.*;
import com.itextpdf.kernel.font.PdfSimpleFont;
import com.itextpdf.kernel.font.PdfType1Font;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;

public class Total_Amount_Report_Activity extends AppCompatActivity {

    private ArrayList<AccountsModel> accountsModels;
    private ArrayList<DealsModel> dealsModels;
    private DealsModel dealsModel;
    private AccountsModel accountsModel;
    private TotalAmountReportAdapter reportAdapter;

    private DataBaseAccess dataBaseAccess;

    private ImageView ivExport,ivBack,ivSync;
    private TextView tvBarTitle;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_amount_report);

        ivExport=findViewById(R.id.iv_export_t_r_a);
        recyclerView=findViewById(R.id.rv_t_r_a);
        ivSync=findViewById(R.id.iv_sync_t_r_a);
        ivBack=findViewById(R.id.iv_t_r_a_back);
        tvBarTitle=findViewById(R.id.tv_t_r_a_title);

        tvBarTitle.setText("التقرير #//العام");

        accountsModel=new AccountsModel();
        dealsModel=new DealsModel();
        accountsModels=new ArrayList<>();
        dealsModels=new ArrayList<>();
        dataBaseAccess=DataBaseAccess.getInstance(Total_Amount_Report_Activity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));

        dataBaseAccess.open();
        accountsModels=dataBaseAccess.getAccounts();
        dataBaseAccess.close();

        reportAdapter=new TotalAmountReportAdapter(this,accountsModels);
        recyclerView.setAdapter(reportAdapter);

        ivExport.setOnClickListener(v -> {

//               createImage();
            try {
                createPdf();
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }

        });


        //
        //when click back row
        ivBack.setOnClickListener(View->finish());
        //


    }


    /**
     * function to deal with  the creation of the pdf for this report
     * @throws FileNotFoundException
     */

    private void createPdf()throws FileNotFoundException{
        //setting text from the date
        String fn=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
      //    String fileName=Environment.getExternalStorageDirectory().getAbsolutePath() +"/PicsFolder/"+"pf"+fn+".pdf";

        //add it to file name temp//
        String fl ="pf"+fn+".pdf";


        //set the file name to the path of the files
        String path= DealingWithPdfFiles.getPdfPath();
        //get complete file name + path as file name
        String fileName=DealingWithPdfFiles.getPdfName(path,fl);


        // create  FileOutputStream
       // FileOutputStream fos = new FileOutputStream(fileName);
        FileOutputStream fos=DealingWithPdfFiles.createFileOutputStream(fileName);
        //create PdfWriter
        //PdfWriter pdfWriter=new PdfWriter(fos);
        PdfWriter pdfWriter=DealingWithPdfFiles.createPdfWriter(fos);

        //create PdfDocument
       //  PdfDocument pdfDocument=new PdfDocument(pdfWriter);
        PdfDocument pdfDocument=DealingWithPdfFiles.createPdfDocument(pdfWriter);

        pdfDocument.setTagged();
        pdfDocument.getCatalog().setLang(new PdfString("ar-AR"));

        pdfDocument.getCatalog().setViewerPreferences(
                new PdfViewerPreferences().setDisplayDocTitle(true));


        //PdfDocument pdf = new PdfDocument(new PdfWriter(dest,new WriterProperties().addXmpMetadata()));

        //Document document = new Document(pdfDocument);

        /**
         *   //create  FontProgram
         *         //f
         *    from
         *     //"assets/font/arabic1_font.ttf"
         */

      FontProgram fontProgram=DealingWithPdfFiles.createFontProgram();

      //create  PdfFont
        PdfFont font=null;
        try {
            font=PdfFontFactory.createFont(fontProgram,PdfEncodings.UTF8,EmbeddingStrategy.PREFER_EMBEDDED);
        }catch (Exception e){

        }

        /**
         * create document
         */

        // create document
        Document document=DealingWithPdfFiles.createDocument(pdfDocument);

        //set the
        document.setFont(font);


        /**
         * create table
         */
        // Table headerTable=DealingWithPdfFiles.createHeaderTable("total public");
        Table table=new Table(4).addHeaderCell("this table public تالااتلال");


        table.setTextAlignment(TextAlignment.CENTER);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);


        table.addCell("name");
        table.addCell("amount");
        table.addCell("status");
        table.addCell("deal count");


        for(AccountsModel am:accountsModels){

            String status=am.getTotal()>0?"for you":"on you";

            table.addCell(am.getName());
            table.addCell(String.valueOf(Math.abs(am.getTotal())));
            table.addCell(status);
            table.addCell(String.valueOf(Math.round(am.getDealCount())));
        }
//        document.add(headerTable);
        //get the sum for this report
        dataBaseAccess.open();
        double sum=dataBaseAccess.getSum();
        dataBaseAccess.close();
        if(sum>0){
            String c="for you:"+Math.round(Math.abs(sum));
            table.addFooterCell(c);

        }
        if(sum<0){
            String c="on you:"+Math.round(Math.abs(sum));
            table.addFooterCell(c);

        }


        document.add(table);

        //still dos not support arabic texts

        Paragraph p1=new Paragraph("بغتببغب");
        p1.setBold();

        p1.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        p1.setVerticalAlignment(VerticalAlignment.BOTTOM);
        p1.setMarginBottom(0.0f);
        p1.setUnderline();
        document.add(p1);


        document.close();

        //call open pdf function
        try {
            DealingWithPdfFiles.openPdfFile(Total_Amount_Report_Activity.this,fileName);
        }catch (IOException exception){

        }


    }


    /**
     * try with html
     * pdf
     */

//


    private void createImage(){


//        ConstraintLayout constraintLayout=findViewById(R.id.cl2__t_r_a);

        //cl_t_r_a_b_l
        ConstraintLayout constraintLayout=findViewById(R.id.cl_t_r_a_b_l);

        View rootView=getWindow().getDecorView().getRootView();
      constraintLayout.setDrawingCacheEnabled(true);
        rootView.setDrawingCacheEnabled(true);

//          ConstraintLayout constraintLayout2=findViewById(R.id.cl2__t_r_a);
//        constraintLayout2.setDrawingCacheEnabled(true);

        Bitmap screenShot=Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        constraintLayout.setDrawingCacheEnabled(false);
        Bitmap combinedBitmap=Bitmap.createBitmap(screenShot.getWidth(),rootView.getHeight(),screenShot.getConfig());
        Canvas canvas=new Canvas(combinedBitmap);

//        int h=constraintLayout.getHeight();

        canvas.drawBitmap(screenShot,0,0,null);
       // ConstraintLayout constraintLayout2=findViewById(R.id.cl2__t_r_a);




        RecyclerView.Adapter adapter=recyclerView.getAdapter();

       for(int i=0;i<adapter.getItemCount();i++){
           RecyclerView.ViewHolder viewHolder=adapter.createViewHolder(recyclerView,adapter.getItemViewType(i));
           adapter.onBindViewHolder(viewHolder,i);

           viewHolder.itemView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(),View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(0,
                   View.MeasureSpec.UNSPECIFIED));
           viewHolder.itemView.layout(0,0,viewHolder.itemView.getMeasuredWidth(),viewHolder.itemView.getMeasuredHeight());

           viewHolder.itemView.draw(canvas);

       }


        String fn=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        //    String fileName=Environment.getExternalStorageDirectory().getAbsolutePath() +"/PicsFolder/"+"pf"+fn+".pdf";

        //add it to file name temp//
        String fl ="IMG"+fn+".png";


        //set the file name to the path of the files
        String path= DealingWithPdfFiles.getPdfPath();
        //get complete file name + path as file name
        String fileName=DealingWithPdfFiles.getPdfName(path,fl);

       FileOutputStream fileOutputStream=null;

       try {
           fileOutputStream=new FileOutputStream(fileName);
           combinedBitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
         //  fileOutputStream.flush();
           fileOutputStream.close();

       }catch (IOException ioException){
           Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
       }


        try {
            DealingWithPdfFiles.openImage(Total_Amount_Report_Activity.this,fileName);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }



}