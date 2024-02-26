package com.example.countsksh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countsksh.R;
import com.example.countsksh.activities.AccountDetailsActivity;
import com.example.countsksh.helpers.DealWithStrings;
import com.example.countsksh.interfaces.AccountsInterface;
import com.example.countsksh.interfaces.recyclerViewInterface;
import com.example.countsksh.models.AccountsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.accountHolder> {
    private Context context;
    public ArrayList<AccountsModel> accountsModels;
    private final recyclerViewInterface recyclerViewInterface;
    private ArrayList<AccountsModel> selectedItems=new ArrayList<>();
    private boolean isSelectedMode=false;
    private AccountsInterface accountsInterface;

    public AccountsAdapter(Context context, ArrayList<AccountsModel> accountsModels,recyclerViewInterface f,AccountsInterface ai) {
        this.context = context;
        this.accountsModels = accountsModels;
        recyclerViewInterface = f;
        this.accountsInterface=ai;
    }

    @NonNull
    @Override
    public accountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.main_activity_account_layout,parent,false);
        return new accountHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull accountHolder holder, int position) {

        holder.tvName.setText(accountsModels.get(position).getName());
        if ((accountsModels.get(position).getTotal() < 0)) {
            Picasso.get().load(R.drawable.ic_up_navigate_green).into(holder.ivStats);
        } else {
            Picasso.get().load(R.drawable.ic_down_navigate_red).into(holder.ivStats);
        }
        //

        holder.tvTotal.setText(DealWithStrings.formatMoney(Math.abs(accountsModels.get(position).getTotal())));
        holder.tvCount.setText(String.valueOf(Math.round(accountsModels.get(position).getDealCount())));

    }

    @Override
    public int getItemCount() {
        return accountsModels.size();
    }

    public class accountHolder extends RecyclerView.ViewHolder {
        ImageView ivStats,delete,edit;
        ImageButton ibAdd;
        TextView tvName,tvTotal,tvCount;

        ConstraintLayout top,down;

        public accountHolder(@NonNull View itemView) {
            super(itemView);
            ivStats=itemView.findViewById(R.id.iv_rv_account_dept_point);
            ibAdd=itemView.findViewById(R.id.imageButtonAddAccount_deal);
            tvName=itemView.findViewById(R.id.tv_rv_account_name);
            tvTotal=itemView.findViewById(R.id.tv_rv_account_deal_sum);
            tvCount=itemView.findViewById(R.id.tv_deal_count);
            top=itemView.findViewById(R.id.cl_top);
            down=itemView.findViewById(R.id.cl_rv_down);
            delete=itemView.findViewById(R.id.iv_a_delete);
            edit=itemView.findViewById(R.id.iv_a_edit);
            //btn + on click
            ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewInterface!=null){
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                           recyclerViewInterface.onAddButtonClick(pos);
                        }
                    }

            }
        });

            //item view on cleck
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                    if(isSelectedMode){
                        if(selectedItems.contains(accountsModels.get(getAdapterPosition()))){
                            itemView.setBackgroundResource(R.color.InverseSurfaceVariantLight);
                            selectedItems.remove(accountsModels.get(getAdapterPosition()));
                            accountsInterface.SelectedAccounts(selectedItems);

                        }else {
                            itemView.setBackgroundResource(R.color.grey);
                            selectedItems.add(accountsModels.get(getAdapterPosition()));
                            //recyclerViewInterface.onItemClick(getAdapterPosition(),true);
                            accountsInterface.SelectedAccounts(selectedItems);

                        }
                        if(selectedItems.size()==0){
                            isSelectedMode=false;
                        }
                    }else {
                        if(recyclerViewInterface!=null){
                            int pos=getAdapterPosition();
                            if(pos!=RecyclerView.NO_POSITION){
                                recyclerViewInterface.onItemClick(pos);
                            }
                        }
                    }

                    //


                }
            });
            //
            //on long click listener
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                public boolean onLongClick(View view) {
                    isSelectedMode=true;
                    if(selectedItems.contains(accountsModels.get(getAdapterPosition()))){
                        itemView.setBackgroundResource(R.color.InverseSurfaceVariantLight);;
                        selectedItems.remove(accountsModels.get(getAdapterPosition()));
                        accountsInterface.SelectedAccounts(selectedItems);


                    }else {
                        itemView.setBackgroundResource(R.color.grey);
                        selectedItems.add(accountsModels.get(getAdapterPosition()));
                        if(selectedItems.size()==1){

                            // recyclerViewInterface.onItemLogClick(1);
                           accountsInterface.SelectedAccounts(selectedItems);
                        }
                        if(selectedItems.size()>=1){

                            //  recyclerViewInterface.onItemLogClick(2);
                            accountsInterface.SelectedAccounts(selectedItems);


                        }

                    }
                    if(selectedItems.size()==0){
                        isSelectedMode=false;
                        accountsInterface.SelectedAccounts(selectedItems);
                        // recyclerViewInterface.onItemLogClick(-1);

                    }

                    return true;
                }
            });
            //


        }
    }


    public void filterItems(ArrayList<AccountsModel> filteredList){
        this.accountsModels=filteredList;
        notifyDataSetChanged();
    }



}
