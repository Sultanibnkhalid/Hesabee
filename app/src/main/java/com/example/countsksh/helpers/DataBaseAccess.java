package com.example.countsksh.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.countsksh.models.AccountsModel;
import com.example.countsksh.models.CurrencyModel;
import com.example.countsksh.models.DealsModel;
import com.example.countsksh.models.GroupModel;

import java.util.ArrayList;

public class DataBaseAccess {

//    //database
//    public static final String DB_NAME="sksh.db";
//    public static final int DB_VERSION= 1;
//    //tables
//    public static final String tbUser="sksh_user";
//    public static final String tbChatReq="sksh_chat_req";
//    public static  final String tbCustomers="sksh_customers";
//    public static final String tbCurrency="sksh_accurncey";
//    public static final String tbDeal="sksh_deal";
//    public static final String tbGoogleMetaData="sksh_google_metadata";
//    public static final String tbGroup="sksh_group";
//    public static final String tbValid="sksh_valid";
//
//    //table filed names
//    public static final String tfId="id";
//    public static final String tfName="name";
//    public static final String tfImg="img";
//    public static final String tfEmail="email";
//    public static final String tfPhone="phone";
//    public static final String tfAmount="amount";
//    public static final String tfDetails="details";
//    public static final String tfNotes="notes";
//    public static final String tfIn="in";
//    public static final String tfOut="out";
//
//
//
//
//    //special for deals
//    public static final String tbDealCustomer="cust_id";
//    public static final String tbDealDate="deal_date";
//    //special for customers
//    public static final String tbCustomersGroupId="group_id";
//    public static final String tbCustomersLastAdd="last_add";
//    public static final String tbCustomersCreateDate="create_on";


    //for class
    private SQLiteDatabase Database;
    private final SQLiteOpenHelper openHelper;
    private static DataBaseAccess instance;
    //for make connection with them database
    private DataBaseAccess(Context c) {

        this.openHelper = new DataBaseHelper(c);
    }

    public static DataBaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.Database = this.openHelper.getWritableDatabase();
    }

    public void close() {
        if (this.Database != null)
            this.Database.close();

    }


        //for dealing with database
    //     public  long insertCurrency() {
    //        ContentValues values = new ContentValues();
    //        values.put(DataBaseHelper.tfName,"يورو");
    //       long result = Database.insert(DataBaseHelper.tbCurrency, null, values);
    //        return result ;
    //
    //    }

   // function to get Accounts
    public ArrayList<AccountsModel> getAccounts(){
     ArrayList<AccountsModel> accountsModels=new ArrayList<>();
//        String query = "SELECT sksh_customers.id,sksh_customers.name,sksh_customers.email,sksh_customers.phone,sksh_customers.group_id,\n" +
//                "sksh_customers.last_add,sksh_customers.img,MAX(strftime('%Y-%m-d',sksh_deal.deal_date))as dt,SUM(sksh_deal.amount)as total, count(sksh_deal.id)as dc\n" +
//                " FROM sksh_customers JOIN sksh_deal on sksh_deal.cust_id=sksh_customers.id ORDER by dt DESC;";


        Cursor cursor = Database.rawQuery("SELECT sksh_customers.id,sksh_customers.name,sksh_customers.email,sksh_customers.phone,sksh_customers.group_id," +
                " sksh_customers.last_add,sksh_customers.img,MAX(strftime('%Y-%m-d',sksh_deal.deal_date))as dt,SUM(sksh_deal.amount)as total, count(sksh_deal.id)as dc FROM sksh_customers " +
                "JOIN sksh_deal on sksh_deal.cust_id=sksh_customers.id GROUP by sksh_customers.id ORDER by dt DESC", null);
        if (cursor.moveToFirst()) {
            do {
                AccountsModel am = new AccountsModel();
                int id=cursor.getColumnIndex(DataBaseHelper.tfId);
                int name=cursor.getColumnIndex(DataBaseHelper.tfName);
                int img=cursor.getColumnIndex(DataBaseHelper.tfImg);
               int email=cursor.getColumnIndex(DataBaseHelper.tfEmail);
                int total=cursor.getColumnIndex(DataBaseHelper.tfTotal);
                int phone=cursor.getColumnIndex(DataBaseHelper.tfPhone);
               int group=cursor.getColumnIndex(DataBaseHelper.tbCustomersGroupId);
               int count=cursor.getColumnIndex(DataBaseHelper.tfDealCount);

                am.setId(cursor.getInt(id));
                am.setName(cursor.getString(name));
               am.setEmil(cursor.getString(email));
                am.setImg(cursor.getString(img));
                am.setPhone(cursor.getString(phone));
                am.setDealCount(cursor.getInt(count));
                am.setTotal(cursor.getDouble(total));
//                am.setGroup(cursor.getInt(group));
                accountsModels.add(am);

            } while (cursor.moveToNext());
            cursor.close();

        }

        return accountsModels;
    }//end function to get Accounts

    //function to get account associate deals
    public ArrayList<DealsModel> getDeals(int ID){
        ArrayList<DealsModel> dealsModels=new ArrayList<>();
        String query = "SELECT sksh_deal.id,sksh_deal.amount,sksh_deal.deal_date,sksh_deal.details,sksh_deal.img,strftime('%Y-%m-d',sksh_deal.deal_date) as dt " +
                "FROM sksh_deal WHERE sksh_deal.cust_id="+ID+" ORDER BY dt Desc;";

        Cursor cursor = Database.rawQuery(query, null);
        double bu=0;
        if (cursor.moveToFirst()) {
            do {
                DealsModel dealsModel = new DealsModel();
                int id=cursor.getColumnIndex(DataBaseHelper.tfId);
                int date=cursor.getColumnIndex(DataBaseHelper.tbDealDate);
                int img=cursor.getColumnIndex(DataBaseHelper.tfImg);
                int details=cursor.getColumnIndex(DataBaseHelper.tfDetails);
                int amount=cursor.getColumnIndex(DataBaseHelper.tfAmount);

                //
                dealsModel.setId(cursor.getInt(id));
                dealsModel.setImg(cursor.getString(img));
                dealsModel.setDetails(cursor.getString(details));
                dealsModel.setDate(cursor.getString(date));
                dealsModel.setAmount(cursor.getDouble(amount));
                double am=cursor.getDouble(amount);
                bu+=am;
                dealsModel.setBudget(bu);
                //
                dealsModels.add(dealsModel);
            } while (cursor.moveToNext());
            cursor.close();

        }

        return dealsModels;
    }//end function to get account associate deals

    //function to get groups
    public ArrayList<GroupModel> getGroups(){
        ArrayList<GroupModel> groupModels=new ArrayList<>();
        //SELECT * FROM sksh_group
        String query = "SELECT * FROM sksh_group;";

        Cursor cursor = Database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                GroupModel gm =new GroupModel();
                int id=cursor.getColumnIndex(DataBaseHelper.tfId);
                int name=cursor.getColumnIndex(DataBaseHelper.tfName);
                gm.setId(cursor.getInt(id));
               gm.setName(cursor.getString(name));
               groupModels.add(gm);

            } while (cursor.moveToNext());
            cursor.close();

        }

        return  groupModels;
    }//end function to get groups

    //not in use
    //function to get get currencies
//    public ArrayList<CurrencyModel> getCurrencies(){
//        ArrayList<CurrencyModel> currencyModels=new ArrayList<>();
//        //SELECT * FROM sksh_accurncey;
//        String query = "SELECT * FROM sksh_accurncey;";
//
//        Cursor cursor = Database.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                CurrencyModel cm=new CurrencyModel();
//                int id=cursor.getColumnIndex(DataBaseHelper.tfId);
//                int name=cursor.getColumnIndex(DataBaseHelper.tfName);
//                cm.setId(cursor.getInt(id));
//                cm.setName(cursor.getString(name));
//                currencyModels.add(cm);
//
//            } while (cursor.moveToNext());
//            cursor.close();
//
//        }
//
//        return  currencyModels;
//    }//end function to get currencies


    //add account
   public int addAccount(AccountsModel accountsModel){
       ContentValues values = new ContentValues();
       values.put(DataBaseHelper.tfName,accountsModel.getName());
       values.put(DataBaseHelper.tfPhone,accountsModel.getPhone());
       values.put(DataBaseHelper.tbCustomersGroupId,accountsModel.getGroup());
       long result = Database.insert(DataBaseHelper.tbCustomers, null, values);
       //return last id as int
        return (int)result;
    } //end-add account

    //add deal
    public int addDeal(DealsModel dealsModel,int Id){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.tfAmount,dealsModel.getAmount());
        values.put(DataBaseHelper.tfDetails,dealsModel.getDetails());
        values.put(DataBaseHelper.tbDealDate,dealsModel.getDate());
        values.put(DataBaseHelper.tfImg,dealsModel.getImg());
        values.put(DataBaseHelper.tbDealCustomer,Id);
        long result = Database.insert(DataBaseHelper.tbDeal, null, values);
        //return last id as int
        return (int)result;
    }//end-add deal
    //add group
    public int addGroup(String name){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.tfName,name);
        long result = Database.insert(DataBaseHelper.tbGroup, null, values);
        //return last id as int
        return (int)result;
    }//end-add group
    //add currency
    public int addCurrency(String name){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.tfName,name);
        long result = Database.insert(DataBaseHelper.tbCurrency, null, values);
        //return last id as int
        return (int)result;
    }//end-add group

    //update functions
    //update account
    public int  updateAccount(AccountsModel accountsModel){
        ContentValues values=new ContentValues();
        values.put(DataBaseHelper.tfName,accountsModel.getName());
        values.put(DataBaseHelper.tfPhone,accountsModel.getPhone());
        values.put(DataBaseHelper.tfImg,accountsModel.getImg());
        values.put(DataBaseHelper.tbCustomersGroupId,accountsModel.getGroup());
        String []st={String.valueOf(accountsModel.getId())};
//        int r= Database.update(DataBaseHelper.tbCustomers,values,"id=?",st);
        return Database.update(DataBaseHelper.tbCustomers,values,"id=?",st);
    } //end-update account

    //update Deal
    public int  updateDeal(DealsModel dealsModel,int customerId){
        ContentValues values=new ContentValues();
        values.put(DataBaseHelper.tfAmount,dealsModel.getAmount());
        values.put(DataBaseHelper.tfDetails,dealsModel.getDetails());
        values.put(DataBaseHelper.tbDealDate,dealsModel.getDate());
        values.put(DataBaseHelper.tfImg,dealsModel.getImg());
        //values.put(DataBaseHelper.tbDealCustomer,customerId);
        String []st={String.valueOf(dealsModel.getId())};
//        int r= Database.update(DataBaseHelper.tbDeal,values,"id=?",st);
        return Database.update(DataBaseHelper.tbDeal,values,"id=?",st);
    } //end-update account

    // other queries
    //get sum in
    public String getInSum() {
        String query = "SELECT sum(sksh_deal.amount) as total FROM sksh_deal where amount>0;";
        double sum = 0;
        Cursor cursor = Database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int s = cursor.getColumnIndex(DataBaseHelper.tfTotal);
                sum = cursor.getDouble(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return  DealWithStrings.formatMoney(sum);
    }
    //get sum in
    public String getInSum(int id) {
        String query = "SELECT sum(sksh_deal.amount) as total FROM sksh_deal where amount>0 AND sksh_deal.cust_id="+id+";";
        double sum = 0;
        Cursor cursor = Database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int s = cursor.getColumnIndex(DataBaseHelper.tfTotal);
                sum = cursor.getDouble(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return   DealWithStrings.formatMoney(sum);
    }
    public String getOutSum() {
        String query = "SELECT sum(sksh_deal.amount) as total FROM sksh_deal where amount<0;";
        double sum = 0;
        Cursor cursor = Database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int s = cursor.getColumnIndex(DataBaseHelper.tfTotal);
                sum = cursor.getDouble(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return   DealWithStrings.formatMoney(sum);
    }
    //get sum
    public String getOutSum(int id) {
        String query = "SELECT sum(sksh_deal.amount) as total FROM sksh_deal where amount<0 AND sksh_deal.cust_id="+id+";";
        double sum = 0;
        Cursor cursor = Database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int s = cursor.getColumnIndex(DataBaseHelper.tfTotal);
                sum = cursor.getDouble(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return   DealWithStrings.formatMoney(sum);
    }

//
    //check users
    public boolean  checkAccount(String s){
        int cc=-1;
        //SELECT count(sksh_customers.id) as cc FROM sksh_customers where sksh_customers.name='dd';
        Cursor cursor = Database.rawQuery("SELECT count(sksh_customers.id) as cc FROM sksh_customers where sksh_customers.name=?",new String[]{s});
        if (cursor.moveToFirst()) {
            do {
                int c = cursor.getColumnIndex(DataBaseHelper.tfCustomerCount);
                cc = cursor.getInt(c);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cc<=0;
    }
    //

    //get Account
    public AccountsModel getAccount(String n) {
        AccountsModel am = new AccountsModel();
        Cursor cursor = Database.rawQuery("SELECT * from sksh_customers WHERE sksh_customers.name=?", new String[]{n});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getColumnIndex(DataBaseHelper.tfId);
                int name = cursor.getColumnIndex(DataBaseHelper.tfName);
                int img = cursor.getColumnIndex(DataBaseHelper.tfImg);
                int email = cursor.getColumnIndex(DataBaseHelper.tfEmail);
//                int total = cursor.getColumnIndex(DataBaseHelper.tfTotal);
                int phone = cursor.getColumnIndex(DataBaseHelper.tfPhone);
                int group = cursor.getColumnIndex(DataBaseHelper.tbCustomersGroupId);
//                int count = cursor.getColumnIndex(DataBaseHelper.tfDealCount);
                am.setId(cursor.getInt(id));
                am.setName(cursor.getString(name));
                am.setEmil(cursor.getString(email));
                am.setImg(cursor.getString(img));
                am.setPhone(cursor.getString(phone));
//                am.setDealCount(cursor.getInt(count));
//                am.setTotal(cursor.getDouble(total));
                am.setGroup(cursor.getInt(group));

            } while (cursor.moveToNext());
            cursor.close();
        }
        return  am;
    }
    //get Account
    public AccountsModel getAccount(int n) {
        AccountsModel am = new AccountsModel();
        Cursor cursor = Database.rawQuery("SELECT * from sksh_customers WHERE sksh_customers.id=?", new String[]{String.valueOf(n)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getColumnIndex(DataBaseHelper.tfId);
                int name = cursor.getColumnIndex(DataBaseHelper.tfName);
                int img = cursor.getColumnIndex(DataBaseHelper.tfImg);
                int email = cursor.getColumnIndex(DataBaseHelper.tfEmail);
//                int total = cursor.getColumnIndex(DataBaseHelper.tfTotal);
                int phone = cursor.getColumnIndex(DataBaseHelper.tfPhone);
                int group = cursor.getColumnIndex(DataBaseHelper.tbCustomersGroupId);
//                int count = cursor.getColumnIndex(DataBaseHelper.tfDealCount);
                am.setId(cursor.getInt(id));
                am.setName(cursor.getString(name));
                am.setEmil(cursor.getString(email));
                am.setImg(cursor.getString(img));
                am.setPhone(cursor.getString(phone));
                /*
                 am.setDealCount(cursor.getInt(count));
                am.setTotal(cursor.getDouble(total));
                */
                am.setGroup(cursor.getInt(group));

            } while (cursor.moveToNext());
            cursor.close();
        }
        return  am;
    }
    //
    //Delete deal
    public int deleteDeal(DealsModel dealsModel){
        return Database.delete(DataBaseHelper.tbDeal,DataBaseHelper.tfId+"=?",new String[]{String.valueOf(dealsModel.getId())});
    }//end-delete deal
    //Delete account
    public int deleteAccount(int id){
        return Database.delete(DataBaseHelper.tbCustomers,DataBaseHelper.tfId+"=?",new String[]{String.valueOf(id)});
    }//end-delete deal
    //get sum
    public double getSum() {
        String query = "SELECT sum(sksh_deal.amount) as total FROM sksh_deal";
        double sum = 0;
        Cursor cursor = Database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int s = cursor.getColumnIndex(DataBaseHelper.tfTotal);
                sum = cursor.getDouble(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return   sum;
    }

  }



