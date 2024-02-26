package com.example.countsksh.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.countsksh.R;
import com.example.countsksh.adapters.DealsAdapter;
import com.example.countsksh.helpers.CalculatorHelper;
import com.example.countsksh.helpers.DataBaseAccess;
import com.example.countsksh.helpers.DealWithStrings;
import com.example.countsksh.helpers.DealingWithImages;
import com.example.countsksh.helpers.StaticsVariablesValue;
import com.example.countsksh.interfaces.recyclerViewInterface;
import com.example.countsksh.models.AccountsModel;
import com.example.countsksh.models.DealsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import org.mariuszgromada.math.mxparser.*;
public class AddAccountActivity extends AppCompatActivity implements recyclerViewInterface {

    private EditText etName,etDetails,etAmount;
    private ImageView ivCamera,ivCalculator,ivBack;
    private RecyclerView recyclerView;
    private Button btnAddIn,btnAddOut;

    private CalculatorHelper calculatorHelper;
    private TextView etDate;
    private AccountsModel accountsModel;
    private ArrayList<DealsModel> dealsModels;
    private DealsModel dealsModel;

    private DataBaseAccess dataBaseAccess;
    private DealsAdapter dealsAdapter;
    private Calendar calendar;
    private ArrayList<DealsModel> selectedDeals=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        //views
        etName=findViewById(R.id.et_person_name);
        etAmount=findViewById(R.id.et_add_account_amount);
        etDate=findViewById(R.id.et_add_account_deal_date);
        etDetails=findViewById(R.id.et_add_account_deal_details);
        ivCalculator=findViewById(R.id.iv_add_account_calculator);
        ivCamera=findViewById(R.id.iv_add_account_deal_img);
        btnAddIn=findViewById(R.id.btn_sav_for_account);
        btnAddOut=findViewById(R.id.btn_sav_on_account);
        ivBack=findViewById(R.id.iv_add_account_back);

        recyclerView=findViewById(R.id.rv_add_account_activity);

        calculatorHelper=new CalculatorHelper(AddAccountActivity.this);
        //variables
        dataBaseAccess=DataBaseAccess.getInstance(AddAccountActivity.this);
        accountsModel=new AccountsModel();
        dealsModels=new  ArrayList<>();
        dealsModel=new DealsModel();
        int id=getIntent().getIntExtra("id",-1);
        if(id>0){
            getAccountData(id);
        }
//        accountsModel.setId(-1);

        etDate.setText(DealWithStrings.formatDate());


        //
        /*
         * setting the  recyclerView
         */

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
         //
        /*
         * show the calculator dialog
         * and setting the result to it
         */
        ivCalculator.setOnClickListener(View-> calculatorHelper.show(etAmount));

        //
//
        /*
         * return back
         */
        ivBack.setOnClickListener(View->finish());

        /*
         * save on
         *
         */
        btnAddIn.setOnClickListener(View-> checkInputs(1));
        /*
         * save on
         *
         */
        btnAddOut.setOnClickListener( View->checkInputs(-1));
        /*
         * show date picker
         *
         */
        etDate.setOnClickListener(v -> {
            calendar= Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(AddAccountActivity.this,
                        (datePicker, year1, month1, day1) -> {
                            String date= day1 +"-"+ month1 +"-"+ year1;

                            etDate.setText(date);
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.setCancelable(true);
            datePickerDialog.show();

        });


        /*
         * clicking camera
         * img
         */

        ivCamera.setOnClickListener(View-> {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(getCameraPermission()){
               startActivityForResult(intent,StaticsVariablesValue.CAMERA_REQUEST_COD);
            }

        });





    }
    //

    /**
     * get account saved deals
     * if it had or pre created ones
     */
    private void showDeals(){
        if(accountsModel.getId()>0){
            dataBaseAccess.open();
            dealsModels=dataBaseAccess.getDeals(accountsModel.getId());
            dataBaseAccess.close();
            if(dealsModels!=null){
                dealsAdapter=new DealsAdapter(AddAccountActivity.this,dealsModels,this);
                recyclerView.setAdapter(dealsAdapter);
            }

        }
    }//

    /**
     *
     * @param t type int
     *  check correct input values
     *   and t to set the deal type in or out
     */
    private void checkInputs(int t){
        try {
            String name = etName.getText().toString().trim();
            String sAmount = etAmount.getText().toString().trim();
            String details = etDetails.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            Expression expression = new Expression(sAmount);
            if (name.equals("") || sAmount.equals("") || details.equals("") || date.equals("")) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                return;
            }
            accountsModel.setName(name);
            dealsModel.setAmount(expression.calculate());
            dealsModel.setDetails(details);
            dealsModel.setDate(date);
            if (t == -1) {
                dealsModel.setAmount(dealsModel.getAmount() * -1);
            }
            Toast.makeText(this, String.valueOf(dealsModel.getAmount()), Toast.LENGTH_SHORT).show();
            addData();
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }//

    /**
     * add data deals with
     * inserting the deal data to the dataBase
     */
    ///add data to db
    private void addData() {

        dataBaseAccess.open();
        boolean b=dataBaseAccess.checkAccount(accountsModel.getName());
        dataBaseAccess.close();
        if(b && dealsModel.getId()>0){
            dataBaseAccess.open();
            dataBaseAccess.addDeal(dealsModel,accountsModel.getId());
            dataBaseAccess.close();
            showDeals();
            clearInputs();

        }
        if(!b){
            dataBaseAccess.open();
            accountsModel=dataBaseAccess.getAccount(accountsModel.getName());
            dataBaseAccess.close();
            if(accountsModel.getName().length()>0 && accountsModel.getId()>=0){
                dataBaseAccess.open();
                dataBaseAccess.addDeal(dealsModel,accountsModel.getId());
                dataBaseAccess.close();
                showDeals();
                clearInputs();
            }
            else {
                Toast.makeText(this, "error with:"+etName.getText().toString().trim(), Toast.LENGTH_LONG).show();
                finish();
            }


        }
        else {
            final Dialog dialog=new Dialog(AddAccountActivity.this);
            dialog.setContentView(R.layout.add_account_dialog_layout);
            dialog.setCancelable(false);

            dialog.setTitle(getResources().getString(R.string.add_account_dialog_title));
            Button save=dialog.findViewById(R.id.btn_save_add_account);
            Button cancel=dialog.findViewById(R.id.btn_cancel_add_account);
            EditText etPhone=dialog.findViewById(R.id.et_add_account_phone);
            TextView textView=dialog.findViewById(R.id.textView6);
            textView.setText(String.format("%s:%s", getResources().getString(R.string.str_name), accountsModel.getName()));
            dialog.show();
            //
            //
            save.setOnClickListener(view -> {
                accountsModel.setPhone(etPhone.getText().toString().trim());
                dataBaseAccess.open();
                int id=dataBaseAccess.addAccount(accountsModel);
                dataBaseAccess.close();
                if (id>0){
                    accountsModel.setId(id);
                    dataBaseAccess.open();
                    dataBaseAccess.addDeal(dealsModel,accountsModel.getId());
                    dataBaseAccess.close();
                    showDeals();
                    clearInputs();
                    dialog.dismiss();
                }
            });
            cancel.setOnClickListener(view -> dialog.dismiss());
        }
    }//
//
    //
    /**
     *  setting input fields to null
     */
    private void clearInputs(){
        etDetails.setText("");
        etAmount.setText("");
        dealsModel=new DealsModel();
        etAmount.setFocusable(true);

    }
    ///

    /**
     * function to deal if we put account data to this activity
     * @param id type int
     *  return void
     *  get the account data to this activity
     * function to deal if we put account data to this activity
     */
    private void getAccountData(int id){
        dataBaseAccess.open();
        accountsModel=dataBaseAccess.getAccount(id);
        dataBaseAccess.close();
        etName.setText(accountsModel.getName());
        showDeals();

    }//end function

    //
    private void  showDealDetails(){
        Dialog dialog=new Dialog(AddAccountActivity.this);
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
        dialog.setCancelable(true);
        dialog.show();
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
        } else {
            ivDDImg.setVisibility(View.GONE);
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

    //
    private int  updateDealData(){
        dataBaseAccess.open();
        int t=dataBaseAccess.updateDeal(dealsModel,accountsModel.getId());
        dataBaseAccess.close();
        return  t;
    }
    //
    private void   editDealData(){

        Toast.makeText(this, dealsModel.getDetails(), Toast.LENGTH_SHORT).show();

        if(dealsModel==null)return;
        Dialog dialog=new Dialog(AddAccountActivity.this);
        dialog.setContentView(R.layout.edit_deal_dialog_layout);
        dialog.show();
        EditText etEditDetails=dialog.findViewById(R.id.et_edit_account_deal_details);
        EditText etEditAmount=dialog.findViewById(R.id.et_edit_deal_amount);

        TextView etEditDate=dialog.findViewById(R.id.et_edit_deal_deal_date_t1);
        ImageView ivECalculator=dialog.findViewById(R.id.iv_edit_deal_calculator);
        ImageView ivECamera1=dialog.findViewById(R.id.iv_edit_deals_deal_camera_img);
        ImageView ivEditImg=dialog.findViewById(R.id.iv_edit_deals_deal_img);
        Button btnSave=dialog.findViewById(R.id.btn_save_for_account_deals);
//      Button btnCancel=dialog.findViewById(R.id.btn_save_on_account_deals);
        RadioButton rBtnIn=dialog.findViewById(R.id.rBtn_in);
        RadioButton rBtnOut=dialog.findViewById(R.id.rBtn_out);
        dialog.setCancelable(true);
        ivCamera=ivECamera1;
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
            DatePickerDialog datePickerDialog=new DatePickerDialog(AddAccountActivity.this, (datePicker, year1, month1, day1) -> {
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
                    Toast.makeText(AddAccountActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        //on click camera
        ivECamera1.setOnClickListener(v -> {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(getCameraPermission()){
                startActivityForResult(intent,StaticsVariablesValue.CAMERA_REQUEST_COD);
            }
        });
        //clicking camera
        ivECalculator.setOnClickListener(v->calculatorHelper.show(etEditAmount));
    }//()=>end function
    ///
    private boolean checkInput(String s) {
        return !s.equals("") && !dealsModel.getDetails().equals("") && !dealsModel.getDate().equals("");
    }
    @Override
    public void onAddButtonClick(int p) {


    }

    @Override
    public void onItemClick(int pos) {
        dealsModel=dealsModels.get(pos);
        showDealDetails();
    }

    @Override
    public void onItemLogClick(int pos) {

    }

    @Override
    public void setSelectedItems(ArrayList<DealsModel> selectedItems) {

    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== StaticsVariablesValue.READ_STORAGE_REQUEST_PERMISSION_COD ){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED ){
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                return;
            }

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
            try {
                //get image as bitmap
                assert data != null;
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");
                //save and set image
                String b= DealingWithImages.saveImageToFile(bitmap);
                Picasso.get().load(StaticsVariablesValue.IMAGES_FOLDER_PATH+b).into(ivCamera);
                dealsModel.setImg(b);

            }catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * @return bool
     * check if storage and capture camera are allowed
     * else request the permission and return false
     */
    private boolean getCameraPermission(){
        if (ActivityCompat.checkSelfPermission(AddAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(AddAccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                return true;
            }else {
                ActivityCompat.requestPermissions(AddAccountActivity.this, new String[]{Manifest.permission.CAMERA}, StaticsVariablesValue.CAMERA_REQUEST_PERMISSION_COD);
                return false;
            }
        }else {
            ActivityCompat.requestPermissions(AddAccountActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticsVariablesValue.READ_STORAGE_REQUEST_PERMISSION_COD);
        }

        return false;
    }


}