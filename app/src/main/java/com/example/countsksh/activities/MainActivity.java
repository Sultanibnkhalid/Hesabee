package com.example.countsksh.activities;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import  com.example.countsksh.R;
import com.example.countsksh.adapters.AccountsAdapter;
import com.example.countsksh.helpers.DataBaseAccess;
import com.example.countsksh.helpers.DataBaseBackHelper;
import com.example.countsksh.helpers.StaticsVariablesValue;
import com.example.countsksh.interfaces.AccountsInterface;
import com.example.countsksh.interfaces.recyclerViewInterface;
import com.example.countsksh.models.AccountsModel;
import com.example.countsksh.models.DealsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements recyclerViewInterface, AccountsInterface {
  private DataBaseAccess dataBaseAccess;
  private RecyclerView recyclerView;
  private FloatingActionButton floatingActionButton;
  private ImageView menu;
  private ArrayList<AccountsModel> accountsModels;
  private TextView tvIn,tvOut;
  private AccountsAdapter accountsAdapter;

  private NavigationView navigationView;
  private DrawerLayout drawerLayout;
  private long check;
 // private LinearLayout linearLayout;

    private TextView tvBar;
    private ImageView ivSearch,ivShare;
    private EditText etSearch;
    private  ArrayList<AccountsModel> selectedAccounts=new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.main_activity_recyclerView);
        floatingActionButton=findViewById(R.id.floatingButtonAddAccount);
        dataBaseAccess=DataBaseAccess.getInstance(MainActivity.this);

        //views
        drawerLayout=findViewById(R.id.layoutDrawer);
        navigationView=findViewById(R.id.nv_main);
       // linearLayout=navigationView.findViewById(R.id.m_a_n_h_layout);


       //  ImageView ivAddAmount = (ImageView) navigationView.findViewById(R.id.iv_m_a_m_h_add_new);
        menu=findViewById(R.id.ivMainActivityMenu);
        tvIn=findViewById(R.id.tvTotalDept);
        tvOut=findViewById(R.id.tvTotalDue);
        ivSearch=findViewById(R.id.iv_search_main_activity);
        ivShare=findViewById(R.id.iv_share_main_activity);
        etSearch=findViewById(R.id.et_search_main_activity);
        tvBar=findViewById(R.id.tv1);

        tvIn.setText("");
        tvOut.setText("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
       new ItemTouchHelper( itemTouchHelperSimpleCallback).attachToRecyclerView(recyclerView);

        //variables
        check=1;
        accountsModels=new ArrayList<>();
        showAccounts();
        floatingActionButton.setOnClickListener(View->goToAddNewAccount());
        //


        //click menu
        menu.setOnClickListener(view ->{
            if(etSearch.getVisibility()==View.VISIBLE){
                etSearch.setText("");
                etSearch.setVisibility(View.GONE);
                tvBar.setVisibility(View.VISIBLE);
                menu.setImageResource(R.drawable.vc_menu);
            }else drawerLayout.openDrawer(GravityCompat.END);

        });
        //click search img
        ivSearch.setOnClickListener(v->{
            tvBar.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
            menu.setImageResource(R.drawable.vc_back_row);
        });

        //when search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterItems(s.toString());
            }
        });


        //->
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, null,
                R.string.str_open, R.string.str_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //when header nav add new clicked
//        ivAddAmount.setOnClickListener(View->goToAddNewAccount());
        //-->
        navigationView.setNavigationItemSelectedListener(item -> {

            int id=item.getItemId();
            if (id == R.id.mn_add_amount) {
              goToAddNewAccount();
            }
            else if(id==R.id.mn_total_repo){
                Intent i=new Intent(MainActivity.this,Total_Amount_Report_Activity.class);
                startActivity(i);

            }
            else if(id==R.id.mn_backup){
               if(getWriteStoragePermission()){
                   ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
                   progressDialog.setTitle(getResources().getString(R.string.str_backing_up));
                   progressDialog.setMessage(getResources().getString(R.string.str_wait));
                   progressDialog.setCancelable(false);
                   progressDialog.show();
                  String msg= DataBaseBackHelper.makeBackup();
                   progressDialog.setTitle(getResources().getString(R.string.str_done));
                   progressDialog.setMessage(msg);
                   progressDialog.setCancelable(true);
               }

            }
            else if(id==R.id.mn_restore_backup){
                if(getReadStoragePermission()){
                    Intent newIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    newIntent.setType("*/*");
                    startActivityForResult(newIntent,StaticsVariablesValue.DB_REQUEST_COD);
                }

            }
            else if(id==R.id.mn_about){
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.about_layout);
                dialog.setCancelable(true);
                dialog.show();

            }
            else if(id==R.id.mn_setting){
                Toast.makeText(this, "sorry this ander development", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawer(GravityCompat.END);

            return false;
        });

        //

        //when clicking  add new from thr navigation view
//        linearLayout.setOnClickListener(View->goToAddNewAccount());




//        dataBaseAccess.open();
//        int b=dataBaseAccess.addGroup("عام");
//        b=dataBaseAccess.addGroup("عملاء");
//        b=dataBaseAccess.addGroup("موردين");
//         dataBaseAccess.close();
//        dataBaseAccess.open();
//         b=dataBaseAccess.addCurrency("ريال");
//        b=dataBaseAccess.addCurrency("دولار");
//        b=dataBaseAccess.addCurrency("ريال سعودي");
//        dataBaseAccess.close();

    }
    private void filterItems(String text){
        ArrayList<AccountsModel> filteredItems=new ArrayList<>();
        for (AccountsModel am:accountsModels){
            if(am.getName().contains(text)){
                filteredItems.add(am);
            }
        }
        accountsAdapter.filterItems(filteredItems);

    }
    private void goToAddNewAccount() {
        Intent i = new Intent(MainActivity.this, AddAccountActivity.class);
        i.putExtra("id", -1);
        startActivityForResult(i, 100);
    }
    private void showAccounts(){
        dataBaseAccess.open();
        accountsModels=dataBaseAccess.getAccounts();
        dataBaseAccess.close();

        if(accountsModels!=null){
            accountsAdapter=new AccountsAdapter(MainActivity.this,accountsModels,this,this);
            recyclerView.setAdapter(accountsAdapter);
        }
        dataBaseAccess.open();
        tvIn.setText(String.format("%s%s%s", getResources().getString(R.string.str_for_you),":", dataBaseAccess.getInSum()));
        tvOut.setText(String.format("%s%s%s", getResources().getString(R.string.str_for_it),":", dataBaseAccess.getOutSum()));
        dataBaseAccess.close();
    }

    //start add account activity
    @Override
    public void onAddButtonClick(int p) {

        Intent i=new Intent(MainActivity.this,AddAccountActivity.class);
        i.putExtra("id",accountsModels.get(p).getId());
        startActivityForResult(i,1099);


    }


    //
    private boolean getWriteStoragePermission(){

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
       return true;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticsVariablesValue.READ_STORAGE_REQUEST_PERMISSION_COD);
        return false;
    }

   private boolean getReadStoragePermission(){

       if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED) {
           return true;
       }
       ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticsVariablesValue.READ_STORAGE_REQUEST_PERMISSION_COD);
       return false;
   }

    //use override function
    //
    @Override
    public void onItemClick(int pos) {
        Intent intent=new Intent(MainActivity.this, AccountDetailsActivity.class);
        intent.putExtra("id",accountsModels.get(pos).getId());
        intent.putExtra("name",accountsModels.get(pos).getName());
        intent.putExtra("phone",accountsModels.get(pos).getPhone());
        intent.putExtra("group",accountsModels.get(pos).getGroup());
        startActivityForResult(intent,101);

    }

    @Override
    public void onItemLogClick(int pos) {

    }

    @Override
    public void setSelectedItems(ArrayList<DealsModel> selectedItems) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==StaticsVariablesValue.DB_REQUEST_COD&&resultCode==RESULT_OK){
            try {

            assert data != null;
            Uri uri=data.getData();
            String path=uri.getPath();
            if(path.contains(".db")){
                ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
                progressDialog.setTitle(getResources().getString(R.string.str_restoring));
                progressDialog.setMessage(getResources().getString(R.string.str_wait));
                progressDialog.setCancelable(false);
                progressDialog.show();
                path=path.replace("document/primary:","");

                String msg=  DataBaseBackHelper.restoreBackup(path);
                progressDialog.setMessage(msg.equals("success")?getResources().getString(R.string.str_success):getResources().getString(R.string.str_failed));
                showAccounts();
               progressDialog.setCancelable(true);

            }


            }catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            }

        }

        else {
            showAccounts();
        }
    }

    @Override
    public void SelectedAccounts(ArrayList<AccountsModel> accountsModels) {
        this.selectedAccounts=accountsModels;
        //
        //complete

    }

    //recycler
//to allow swipe for recycler view
ItemTouchHelper.SimpleCallback itemTouchHelperSimpleCallback =
        new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
             deleteItem(viewHolder.getAdapterPosition());
//             ConstraintLayout top=viewHolder.itemView.findViewById(R.id.cl_top);
//              ConstraintLayout  down=viewHolder.itemView.findViewById(R.id.cl_rv_down);
//              down.setVisibility(View.VISIBLE);
//                top.setVisibility(View.GONE);
            }
        };

private void deleteItem(int pos){
    dataBaseAccess.open();
    int b= dataBaseAccess.deleteAccount(accountsModels.get(pos).getId());
    dataBaseAccess.close();
    showAccounts();
}













}