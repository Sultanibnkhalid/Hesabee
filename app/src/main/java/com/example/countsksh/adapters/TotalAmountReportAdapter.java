package com.example.countsksh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countsksh.R;
import com.example.countsksh.helpers.DataBaseAccess;
import com.example.countsksh.helpers.DealWithStrings;
import com.example.countsksh.models.AccountsModel;
import com.example.countsksh.models.DealsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TotalAmountReportAdapter extends RecyclerView.Adapter<TotalAmountReportAdapter.TARHolder> {

    private ArrayList<AccountsModel> accountsModels;
    private Context context;

    private TotalReportSubDetailsAdapter totalReportSubDetailsAdapter;
    private ArrayList<DealsModel> dealsModels=new ArrayList<>();
    private ArrayList<AccountsModel> clickedItems=new ArrayList<>();
    private DataBaseAccess dataBaseAccess;

    public TotalAmountReportAdapter( Context context ,ArrayList<AccountsModel> accountsModels) {
        this.accountsModels = accountsModels;
        this.context = context;
        dataBaseAccess=DataBaseAccess.getInstance(context);
    }

    @NonNull
    @Override
    public TARHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.t_a_r_a_recycleview_layout,parent,false);
        return new TARHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TARHolder holder, int position) {

        holder.tvName.setText(accountsModels.get(position).getName());
//        if(accountsModels.get(position).getTotal()<0){
//            holder.ivPoint.setImageResource(R.drawable.img_tringle_red);
//        }
        holder.ivPoint.setImageResource(accountsModels.get(position).getTotal()>0?R.drawable.ic_up_navigate_green:R.drawable.ic_down_navigate_red);
        holder.tvTotal.setText(DealWithStrings.formatMoney(Math.abs(accountsModels.get(position).getTotal())));
        holder.tvCount.setText(String.valueOf(Math.round(accountsModels.get(position).getDealCount())));

    }

    @Override
    public int getItemCount() {
        return accountsModels.size()>0?accountsModels.size():0;
    }

    public class TARHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvTotal,tvCount;
        ImageView ivUpDown,ivPoint;
        //for sub details
        LinearLayout linearLayout;
        RecyclerView recyclerView;
        public TARHolder(@NonNull View itemView) {
            super(itemView);
            tvCount=itemView.findViewById(R.id.tv_t_r_a_deal_count);
            tvName=itemView.findViewById(R.id.tv_rv_t_r_a_account_name);
            tvTotal=itemView.findViewById(R.id.tv_rv_t_r_a_amount);
            ivPoint=itemView.findViewById(R.id.iv_rv_t_r_a_point);
            ivUpDown=itemView.findViewById(R.id.iv_t_r_a_up_down);

            //
            //for sub details
            linearLayout=itemView.findViewById(R.id.tv_rv_t_r_a_ll);
            recyclerView=itemView.findViewById(R.id.tr_sub_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            ivUpDown.setOnClickListener(v -> {
                if(clickedItems.contains(accountsModels.get(getAdapterPosition()))){
                    Picasso.get().load(R.drawable.up_details).into(ivUpDown);
                    linearLayout.setVisibility(View.GONE);
                    clickedItems.remove(accountsModels.get(getAdapterPosition()));
                }else {

                    linearLayout.setVisibility(View.VISIBLE);
                    dataBaseAccess.open();
                    dealsModels=dataBaseAccess.getDeals(accountsModels.get(getAdapterPosition()).getId());
                    dataBaseAccess.close();
                    totalReportSubDetailsAdapter=new TotalReportSubDetailsAdapter(dealsModels,context);
                    recyclerView.setAdapter(totalReportSubDetailsAdapter);
                    clickedItems.add(accountsModels.get(getAdapterPosition()));
                    Picasso.get().load(R.drawable.down_details).into(ivUpDown);

                }

            });


        }
    }
}
