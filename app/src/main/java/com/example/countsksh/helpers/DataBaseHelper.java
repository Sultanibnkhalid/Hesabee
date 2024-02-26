package com.example.countsksh.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBaseHelper extends SQLiteAssetHelper {


    //database
    public static final String DB_NAME="sksh.db";
    public static final int DB_VERSION= 1;
    //tables
    public static final String tbUser="sksh_user";
    public static final String tbChatReq="sksh_chat_req";
    public static  final String tbCustomers="sksh_customers";
    public static final String tbCurrency="sksh_accurncey";
    public static final String tbDeal="sksh_deal";
    public static final String tbGoogleMetaData="sksh_google_metadata";
    public static final String tbGroup="sksh_group";
    public static final String tbValid="sksh_valid";

    //table filed names
    public static final String tfId="id";
    public static final String tfName="name";
    public static final String tfImg="img";
    public static final String tfEmail="email";
    public static final String tfPhone="phone";
    public static final String tfAmount="amount";
    public static final String tfDetails="details";
    public static final String tfNotes="notes";
    public static final String tfIn="in";
    public static final String tfOut="out";




    //special for deals
    public static final String tbDealCustomer="cust_id";
    public static final String tbDealDate="deal_date";
    //special for customers
    public static final String tbCustomersGroupId="group_id";
    public static final String tbCustomersLastAdd="last_add";
    public static final String tbCustomersCreateDate="create_on";

    //for total
    public static final String tfTotal="total";
    public static final String tfDealCount="dc";
    public static final String tfCustomerCount="cc";




    public DataBaseHelper(Context c) {
        super(c, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

    }

}
