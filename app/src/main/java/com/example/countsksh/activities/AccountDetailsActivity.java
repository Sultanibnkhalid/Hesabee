package com.example.countsksh.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countsksh.R;
import com.example.countsksh.adapters.DealsAdapter;
import com.example.countsksh.helpers.CalculatorHelper;
import com.example.countsksh.helpers.DataBaseAccess;
import com.example.countsksh.helpers.DealWithStrings;
import com.example.countsksh.helpers.DealingWithImages;
import com.example.countsksh.helpers.DealingWithPdfFiles;
import com.example.countsksh.helpers.StaticsVariablesValue;
import com.example.countsksh.interfaces.recyclerViewInterface;
import com.example.countsksh.models.AccountsModel;
import com.example.countsksh.models.DealsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AccountDetailsActivity extends AppCompatActivity implements recyclerViewInterface{

    private RecyclerView recyclerView;
    private ImageView ivStatus,ivExport,ivShare,ivBack,ivMenu;
    private FloatingActionButton floatingActionButton;
    private TextView tvIn,tvOut,tvName;
    private AccountsModel accountsModel;
    private ArrayList<DealsModel> dealsModels;
    private DataBaseAccess dataBaseAccess;
    private DealsAdapter dealsAdapter;
    private DealsModel dealsModel;
    private Calendar calendar;
    private CalculatorHelper calculatorHelper;
    private ImageView cameraImage;
    private ConstraintLayout constraintLayout;
    private LinearLayout itemOptionsLinerLayout;
    private ImageView ivEdit,ivMenuEdit,ivSync,ivCancelOp,ivDeleteItem;
    private TextView tvSelectedItems;

    private ArrayList<DealsModel> selectedDeals=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        //
        itemOptionsLinerLayout=findViewById(R.id.ly_edit_deal_options);
        ivEdit=findViewById(R.id.iv_details_edit_deal);
        ivDeleteItem=findViewById(R.id.iv_details_delete_deal);
        ivEdit=findViewById(R.id.iv_details_edit_deal);
        ivCancelOp=findViewById(R.id.iv_details_edit_deal_back);
        ivMenuEdit=findViewById(R.id.iv_details_share_dael);
        tvSelectedItems=findViewById(R.id.tv_details_edit_deal_item_count);
        constraintLayout=findViewById(R.id.cl_details_action_bar);//cl_details_action_bar

        //
        recyclerView=findViewById(R.id.rv_details_activity);
        ivStatus=findViewById(R.id.imageView4);
        ivBack=findViewById(R.id.iv_details_activity_back);
        ivExport=findViewById(R.id.iv_export_details_activity);
        ivShare=findViewById(R.id.iv_share_details_activity);
        ivMenu=findViewById(R.id.iv_menu_details_activity);
        floatingActionButton=findViewById(R.id.floatingButtonAddDeal);
        tvIn=findViewById(R.id.tv_account_total_deal_Due);
        tvOut=findViewById(R.id.tv_account_total_deal_dept);
        tvName=findViewById(R.id.tv_account_name_details_activity);
        if(itemOptionsLinerLayout.getVisibility()==View.VISIBLE){
            itemOptionsLinerLayout.setVisibility(View.GONE);
        }
        calculatorHelper=new CalculatorHelper(AccountDetailsActivity.this);
        dealsModel=new DealsModel();
        dataBaseAccess=DataBaseAccess.getInstance(this);
        accountsModel=new AccountsModel();
        accountsModel.setId(getIntent().getIntExtra("id",0));
        accountsModel.setGroup(getIntent().getIntExtra("group",0));
        accountsModel.setName(getIntent().getStringExtra("name"));
        accountsModel.setPhone(getIntent().getStringExtra("phone"));

        tvName.setText(accountsModel.getName());
        if(accountsModel.getId()<=0){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            finish();
        }

        //setting  recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true));
        //get account deals
        //
        showDeals();

        //when clicking add button
        floatingActionButton.setOnClickListener(View-> addDeal());
        //return back
        ivBack.setOnClickListener(View->finish());
        //

        //selected Items Options
        //..

        //edit selected deal
        ivEdit.setOnClickListener(View->editDealData());

        //
        //cancel the view
        ivCancelOp.setOnClickListener(View->hideOptionView());
        //delete items
        ivDeleteItem.setOnClickListener(View->deleteItems());
        //when export to pdf
       ivExport.setOnClickListener(v -> {
          //  createImage();
           if (ActivityCompat.checkSelfPermission(AccountDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
              try {
                  createPdf();
              }catch (IOException ioException){
                  ioException.printStackTrace();
              }
           }else {
               ActivityCompat.requestPermissions(AccountDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticsVariablesValue.READ_STORAGE_REQUEST_PERMISSION_COD);
           }
       });
    }
    /// end on create
    private void showDeals() {
        dataBaseAccess.open();
        dealsModels=dataBaseAccess.getDeals(accountsModel.getId());
        double sumIn= DealWithStrings.formatDouble(dataBaseAccess.getInSum(accountsModel.getId()));
        dataBaseAccess.close();
        dataBaseAccess.open();
        double sumOut= DealWithStrings.formatDouble(dataBaseAccess.getOutSum(accountsModel.getId()));
        dataBaseAccess.open();
        if(dealsModels!=null){
            dealsAdapter=new DealsAdapter(AccountDetailsActivity.this,dealsModels,this);
            recyclerView.setAdapter(dealsAdapter);
        }
        tvIn.setText(String.format("%s%s%s", getResources().getString(R.string.str_for_you),":",DealWithStrings.formatMoney(sumIn)));
        tvOut.setText(String.format("%s%s%s", getResources().getString(R.string.str_for_it),":",DealWithStrings.formatMoney(sumIn)));
        if(sumIn<sumOut){
            Picasso.get().load(R.drawable.ic_up_navigate_green).into(ivStatus);
        }
    }

    //show dialog to add deal dialog
    private void addDeal() {
        Dialog dialog = new Dialog(AccountDetailsActivity.this);
        dialog.setContentView(R.layout.add_deal_layout);
        dialog.setCancelable(false);
        dialog.setTitle(accountsModel.getName());
        Button btnSaveIn = dialog.findViewById(R.id.btn_save_for_account_deals);
        Button btnSaveOut = dialog.findViewById(R.id.btn_save_on_account_deals);
        EditText etDetails = dialog.findViewById(R.id.et_add_account_deal_details);
        EditText etAmount = dialog.findViewById(R.id.et_add_deal_amount);
        TextView etDate = dialog.findViewById(R.id.et_add_deal_deal_date);
        TextView tvAccountName = dialog.findViewById(R.id.tv_add_deals_account_name);
        //

        ImageView ivCalculator = dialog.findViewById(R.id.iv_add_deal_calculator);
        ImageView ivCamera = dialog.findViewById(R.id.iv_add_account_deal_img);
        cameraImage = ivCamera;
        //
        ImageButton ibCancel = dialog.findViewById(R.id.imageButton);
        dialog.show();
        //
        etDate.setText(DealWithStrings.formatDate());
        tvAccountName.setText(accountsModel.getName());
        //cancel
        ibCancel.setOnClickListener(view -> {
            dealsModel = new DealsModel();
            dialog.dismiss();
        });
        //
        //save in
        btnSaveIn.setOnClickListener(view -> {
            dealsModel.setDate(etDate.getText().toString().trim());
            dealsModel.setDetails(etDetails.getText().toString().trim());
            if (checkInput(etAmount.getText().toString().trim())) {
                double am = DealWithStrings.formatDouble(etAmount.getText().toString().trim());
                dealsModel.setAmount(am);
                int i = insertDealData();
                if (i > 0) {
                    showDeals();
                    dealsModel = new DealsModel();
                    dialog.dismiss();
                }
            }

        });
        // save out
        btnSaveOut.setOnClickListener(view -> {
            dealsModel.setDate(etDate.getText().toString().trim());
            dealsModel.setDetails(etDetails.getText().toString().trim());
            if (checkInput(etAmount.getText().toString().trim())) {
                double am = DealWithStrings.formatDouble(etAmount.getText().toString().trim());
                dealsModel.setAmount(am * -1);
                int i = insertDealData();
                if (i > 0) {
                    showDeals();
                    dealsModel = new DealsModel();
                    dialog.dismiss();
                }
            }
        });
        //
        //show date picker
        etDate.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AccountDetailsActivity.this, (datePicker, year1, month1, day1) -> {
                String date = day1 + "-" + month1 + "-" + year1;
                etDate.setText(date);
            }, year, month, day
            );
            datePickerDialog.show();
        });
        //add picture
        ivCamera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (getCameraPermission()) {
                startActivityForResult(intent, StaticsVariablesValue.CAMERA_REQUEST_COD);
            }
        });
        //
        ivCalculator.setOnClickListener(v -> calculatorHelper.show(etAmount));
    }
    //check deal data before insert it
    private boolean checkInput(String s) {
        return !s.equals("") && !dealsModel.getDetails().equals("") && !dealsModel.getDate().equals("");
    }
     ///insert the data to data base
    private int insertDealData() {
        dataBaseAccess.open();
        int i=dataBaseAccess.addDeal(dealsModel,accountsModel.getId());
        dataBaseAccess.close();
        return i;
        }
    //
    //dealing with edit dialog
    //
    private void   editDealData(){

        Toast.makeText(this, dealsModel.getDetails(), Toast.LENGTH_SHORT).show();

        if(dealsModel==null)return;
        Dialog dialog=new Dialog(AccountDetailsActivity.this);
        dialog.setContentView(R.layout.edit_deal_dialog_layout);
        dialog.show();
        EditText etEditDetails=dialog.findViewById(R.id.et_edit_account_deal_details);
        EditText etEditAmount=dialog.findViewById(R.id.et_edit_deal_amount);

        TextView etEditDate=dialog.findViewById(R.id.et_edit_deal_deal_date_t1);
        ImageView ivECalculator=dialog.findViewById(R.id.iv_edit_deal_calculator);
        ImageView ivECamera=dialog.findViewById(R.id.iv_edit_deals_deal_camera_img);
        ImageView ivEditImg=dialog.findViewById(R.id.iv_edit_deals_deal_img);
        Button btnSave=dialog.findViewById(R.id.btn_save_for_account_deals);
//      Button btnCancel=dialog.findViewById(R.id.btn_save_on_account_deals);
        RadioButton rBtnIn=dialog.findViewById(R.id.rBtn_in);
        RadioButton rBtnOut=dialog.findViewById(R.id.rBtn_out);
        dialog.setCancelable(true);
        cameraImage=ivECamera;
        etEditDetails.setText(dealsModel.getDetails());
        etEditAmount.setText(String.valueOf(dealsModel.getAmount()));
        //
        etEditDate.setText(dealsModel.getDate());
        //check img path
        if(dealsModel.getImg()==null){
            ivEditImg.setVisibility(View.GONE);
        }else{
            ivEditImg.setVisibility(View.VISIBLE);
            ivEditImg.setImageBitmap(DealingWithImages.getImageFromFile(dealsModel.getImg()));
        }
        //check deal type
        rBtnIn.setChecked(dealsModel.getAmount() > 0);
        rBtnOut.setChecked(dealsModel.getAmount() < 0);
        //
        rBtnIn.setOnClickListener(v -> {
            if(rBtnOut.isChecked())rBtnOut.setChecked(false);
        });
        //
        rBtnOut.setOnClickListener(v -> {
            if(rBtnIn.isChecked())rBtnIn.setChecked(false);
        });
        // datePecker
        etEditDate.setOnClickListener(v -> {
            calendar=Calendar.getInstance();
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH);
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog=new DatePickerDialog(AccountDetailsActivity.this, (datePicker, year1, month1, day1) -> {
                String date= day1 +"-"+ month1 +"-"+ year1;

                etEditDate.setText(date);
            }, year,month,day
            );datePickerDialog.show();
        });
         //save
        btnSave.setOnClickListener(view -> {
            dealsModel.setDate(etEditDate.getText().toString().trim());
            dealsModel.setDetails(etEditDetails.getText().toString().trim());
            if( checkInput(etEditAmount.getText().toString().trim())){

                double am=Math.abs(DealWithStrings.formatDouble(etEditAmount.getText().toString().trim()));
                dealsModel.setAmount(rBtnIn.isChecked() ?am:am*-1);
                int i= updateDealData();
                if(i>0){
                    showDeals();
                }
                else {
                    Toast.makeText(AccountDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                hideOptionView();
                dialog.dismiss();
            }
        });
        //on click camera
        ivECamera.setOnClickListener(v -> {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(getCameraPermission()){
                startActivityForResult(intent,StaticsVariablesValue.CAMERA_REQUEST_COD);
            }
        });
        //clicking camera
        ivECalculator.setOnClickListener(v->calculatorHelper.show(etEditAmount));
    }//()=>end function
    //update  deal data
    private int  updateDealData(){
        dataBaseAccess.open();
        int t=dataBaseAccess.updateDeal(dealsModel,accountsModel.getId());
        dataBaseAccess.close();
      return  t;
    }
    //hide edit action bare
    private void hideOptionView(){
        itemOptionsLinerLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
        dealsModel=new DealsModel();
        selectedDeals.clear();
        recyclerView.setAdapter(dealsAdapter);
    }
    //get deal for thr account
    private void  showDealDetails(){
        Dialog dialog=new Dialog(AccountDetailsActivity.this);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setContentView(R.layout.deal_details_dialog_layout);
        TextView tvAName=dialog.findViewById(R.id.tv_deal_details_dialog_account_name);
        TextView tvDAmount=dialog.findViewById(R.id.tv_deal_details_dialog_deal_amount);
        TextView tvDInOut=dialog.findViewById(R.id.tv_deal_details_dialog_deal_in_out);
        TextView tvDDetails=dialog.findViewById(R.id.tv_deal_details_dialog_deal_details);
        TextView tvDTime=dialog.findViewById(R.id.tv_deal_details_dialog_deal_tim);
        TextView tvDDate=dialog.findViewById(R.id.tv_deal_details_dialog_deal_date);
        ImageView ivDDImg=dialog.findViewById(R.id.iv_deal_details_dialog_deal_img);
        Button btnDEdit=dialog.findViewById(R.id.btn_deal_details_dialog_edit);
        Button btnDOk=dialog.findViewById(R.id.btn_deal_details_dialog_ok);

        //
        tvAName.setText(accountsModel.getName());
        tvDDate.setText(dealsModel.getDate());
        tvDAmount.setText(DealWithStrings.formatMoney(dealsModel.getAmount()));
        tvDDetails.setText(dealsModel.getDetails());
        tvDTime.setText(dealsModel.getDealTime());
        tvDInOut.setText(dealsModel.getAmount()>0?getResources().getString(R.string.str_in):getResources().getString(R.string.str_out));
        if(dealsModel.getImg()!=null){
                ivDDImg.setVisibility(View.VISIBLE);
                Picasso.get().load(StaticsVariablesValue.IMAGES_FOLDER_PATH+dealsModel.getImg()).into(ivDDImg);
            }
        //ok click
        btnDOk.setOnClickListener(view -> {
            dealsModel=new DealsModel();
            dialog.dismiss();
        });
        //click edit
        btnDEdit.setOnClickListener(view -> {
            dialog.dismiss();
            editDealData();
        });
    }
    //show dialog dealing with delete click
    @SuppressLint("DefaultLocale")
    private void deleteItems(){
        if(selectedDeals.size()==0)return;
        Dialog d=new Dialog(AccountDetailsActivity.this);
        d.setContentView(R.layout.ok_cancel_dialog_layout);
        d.setCancelable(true);
        d.setTitle(getResources().getString(R.string.str_warning));
        Button btnOK=d.findViewById(R.id.btn_ok_cancel_ok);
        Button btnCancel=d.findViewById(R.id.btn_ok_cancel_cancel);
        TextView textView=d.findViewById(R.id.tv_ok_cancel_tv2);
        d.show();
        textView.setText(String.format("%s %d %s", getResources().getString(R.string.str_sure_delete), selectedDeals.size(), getResources().getString(R.string.str_item)));
        btnOK.setOnClickListener(view -> {
            deleteDeals();
            d.dismiss();
        });
        //
        btnCancel.setOnClickListener(view -> d.dismiss());
    }
    //
    //delete deal from the data base
    private void   deleteDeals(){
        try {
            for (DealsModel d :selectedDeals){
                dataBaseAccess.open();
                int r=dataBaseAccess.deleteDeal(d);
                dataBaseAccess.close();
                if (r==-1){
                    Toast.makeText(this, getResources().getString(R.string.str_error), Toast.LENGTH_SHORT).show();
                   // return;
                }
            }
            selectedDeals.clear();
            dealsModel=new DealsModel();
            showDeals();
           // hideOptionView();
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //
    //on request permission
    //override
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==StaticsVariablesValue.READ_STORAGE_REQUEST_PERMISSION_COD ){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED ){
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if(requestCode==StaticsVariablesValue.WRITE_STORAGE_REQUEST_PERMISSION_COD ) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(requestCode==StaticsVariablesValue.CAMERA_REQUEST_PERMISSION_COD){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, StaticsVariablesValue.CAMERA_REQUEST_COD);
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==StaticsVariablesValue.CAMERA_REQUEST_COD && resultCode==RESULT_OK){
            Toast.makeText(AccountDetailsActivity.this, "jojpjsdj", Toast.LENGTH_SHORT).show();
            try {
                //get image as bitmap
                assert data != null;
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");
                //save and set image
                String b= DealingWithImages.saveImageToFile(bitmap);
                Picasso.get().load(StaticsVariablesValue.IMAGES_FOLDER_PATH+b).into(cameraImage);
                dealsModel.setImg(b);
            }catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    ///
    @Override
    public void onAddButtonClick(int p) {

    }


    //
    @Override
    public void onItemClick(int pos) {
        dealsModel=dealsModels.get(pos);
          showDealDetails();
    }
    //
    @Override
    public void onItemLogClick(int pos) {
    }

    ///
    @SuppressLint("DefaultLocale")
    @Override
    public void setSelectedItems(ArrayList<DealsModel> selectedItems) {

        tvSelectedItems.setText(String.format("%s: %d", getResources().getString(R.string.str_selected_items), selectedItems.size()));
        if(selectedItems.size()==1){
            constraintLayout.setVisibility(View.GONE);
            itemOptionsLinerLayout.setVisibility(View.VISIBLE);
            this.selectedDeals=selectedItems;
            this.dealsModel=selectedItems.get(0);
        }
        else if(selectedItems.size()>1){
            this.selectedDeals=selectedItems;
            this.dealsModel=selectedItems.get(0);
            ivEdit.setVisibility(View.GONE);

        }
        else {
            constraintLayout.setVisibility(View.VISIBLE);
            itemOptionsLinerLayout.setVisibility(View.GONE);
            selectedDeals.clear();
        }

    }
    //




    /**
     * function to deal with  the creation of the pdf for this report
     * @throws FileNotFoundException if it acu
     */
    private void createPdf()throws FileNotFoundException{
        //setting text from the date
        String fn=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        //    String fileName=Environment.getExternalStorageDirectory().getAbsolutePath() +"/PicsFolder/"+"pf"+fn+".pdf";


        //add it to file name temp//
        String fl =accountsModel.getName()+fn+".pdf";


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

        //Document document = new Document(pdfDocument);

        /*
         * create  FontProgram
         *from
         * "assets/font/arabic1_font.ttf"
         */

        FontProgram fontProgram=DealingWithPdfFiles.createFontProgram();

        //create  PdfFont
        PdfFont font=null;
        try {
            font= PdfFontFactory.createFont(fontProgram, PdfEncodings.UTF8, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        }catch (Exception e){
            e.printStackTrace();
        }

        /*
         * create document
         */

        // create document
        Document document=DealingWithPdfFiles.createDocument(pdfDocument);

        //set the
        document.setFont(font);


        /*
         * create table
         */
        // Table headerTable=DealingWithPdfFiles.createHeaderTable("total public");
        Table table=new Table(5).addHeaderCell(accountsModel.getName()+"#"+"عام");


        table.setTextAlignment(TextAlignment.CENTER);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell("date");
        table.addCell("amount");
        table.addCell("details");
        table.addCell("status");
        table.addCell("budget");


        double sumIn=0;
        double sumOut=0;

        for(DealsModel dm:dealsModels){

            //1
            table.addCell(dm.getDate());


            sumIn+=dm.getAmount()>0?dm.getAmount():0;
            sumOut+=dm.getAmount()>0?0:dm.getAmount();
            //2
            table.addCell(String.valueOf(dm.getAmount()));
            //3
            table.addCell(dm.getDetails());
            String status=dm.getAmount()>0?getResources().getString(R.string.str_for_you):getResources().getString(R.string.str_for_it);
            //4
            table.addCell(status);
            //5
            table.addCell(String.valueOf(dm.getBudget()));

        }
        table.addFooterCell(getResources().getString(R.string.str_for_you)+" : "+sumIn);
        table.addFooterCell(getResources().getString(R.string.str_for_it)+" : "+sumOut);
        table.addFooterCell( " الرصيد: "+sumOut+sumIn);
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
            DealingWithPdfFiles.openPdfFile(AccountDetailsActivity.this,fileName);
        }catch (IOException exception){
            exception.printStackTrace();
        }


    }


    //createImage not in use yet

//    private void createImage(){
//
//        View rootView=getWindow().getDecorView().getRootView();
//        rootView.setDrawingCacheEnabled(true);
//
//        Bitmap screenShot=Bitmap.createBitmap(rootView.getDrawingCache());
//        rootView.setDrawingCacheEnabled(false);
//
//        Bitmap combinedBitmap=Bitmap.createBitmap(screenShot.getWidth(),40,screenShot.getConfig());
//        Canvas canvas=new Canvas(combinedBitmap);
//
//        canvas.drawBitmap(screenShot,0,0,null);
//
//        RecyclerView.Adapter adapter=recyclerView.getAdapter();
//
//       for(int i = 0; i< Objects.requireNonNull(adapter).getItemCount(); i++){
//           RecyclerView.ViewHolder viewHolder=adapter.createViewHolder(recyclerView,adapter.getItemViewType(i));
//           adapter.onBindViewHolder(viewHolder,i);
//
//
//           viewHolder.itemView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(),View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(0,
//                   View.MeasureSpec.UNSPECIFIED));
//           viewHolder.itemView.layout(0,i*40,viewHolder.itemView.getMeasuredWidth(),viewHolder.itemView.getMeasuredHeight());
//           viewHolder.itemView.draw(canvas);
//
//       }
//
//
//        String fn=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        //    String fileName=Environment.getExternalStorageDirectory().getAbsolutePath() +"/PicsFolder/"+"pf"+fn+".pdf";
//
//        //add it to file name temp//
//        String fl ="IMG"+fn+".png";
//
//
//        //set the file name to the path of the files
//        String path= DealingWithPdfFiles.getPdfPath();
//        //get complete file name + path as file name
//        String fileName=DealingWithPdfFiles.getPdfName(path,fl);
//
//        FileOutputStream fileOutputStream;
//
//        try {
//            fileOutputStream=new FileOutputStream(fileName);
//            combinedBitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//
//        }catch (IOException ioException){
//            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
//        }
//
//
//        try {
//            DealingWithPdfFiles.openImage(AccountDetailsActivity.this,fileName);
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//
//
//    }

/**
 * get camera permission
 */

private  boolean getCameraPermission(){
    if (ActivityCompat.checkSelfPermission(AccountDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(AccountDetailsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else {
            ActivityCompat.requestPermissions(AccountDetailsActivity.this, new String[]{Manifest.permission.CAMERA}, StaticsVariablesValue.CAMERA_REQUEST_PERMISSION_COD);
       return false;
        }
    }else {
        ActivityCompat.requestPermissions(AccountDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticsVariablesValue.READ_STORAGE_REQUEST_PERMISSION_COD);
        return false;
    }

}





}