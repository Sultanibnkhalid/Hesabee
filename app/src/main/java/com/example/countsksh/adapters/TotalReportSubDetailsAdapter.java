package com.example.countsksh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countsksh.R;
import com.example.countsksh.helpers.DealWithStrings;
import com.example.countsksh.models.DealsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TotalReportSubDetailsAdapter extends RecyclerView.Adapter<TotalReportSubDetailsAdapter.ViewHolser> {
    private ArrayList<DealsModel> dealsModels;
    private Context context;

    public TotalReportSubDetailsAdapter(ArrayList<DealsModel> dealsModels, Context context) {
        this.dealsModels = dealsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.total_report_sub_details_layout,parent,false);
        return new ViewHolser(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolser holder, int position) {

        holder.tvDate.setText(dealsModels.get(position).getDate());
        holder.tvDetails.setText(dealsModels.get(position).getDetails());
        holder.tvAmount.setText(DealWithStrings.formatMoney(Math.abs(dealsModels.get(position).getAmount())));
        if(dealsModels.get(position).getAmount()>=0){
            Picasso.get().load(R.drawable.ic_up_navigate_green).into(holder.ivStatus);
        }else{
            Picasso.get().load(R.drawable.ic_down_navigate_red).into(holder.ivStatus);

        }
        holder.tvBudget.setText(DealWithStrings.formatMoney(dealsModels.get(position).getBudget()));
    }

    @Override
    public int getItemCount() {
        return dealsModels.size()>0?dealsModels.size():0;
    }

    //
    public class ViewHolser extends RecyclerView.ViewHolder {
        ImageView ivStatus;
        TextView tvDate,tvDetails,tvAmount,tvBudget;
        public ViewHolser(@NonNull View itemView) {
            super(itemView);
            ivStatus=itemView.findViewById(R.id.iv_tr_sub_details_deal_type);
            tvDate=itemView.findViewById(R.id.tv_tr_sub_details_deal_date);
            tvAmount=itemView.findViewById(R.id.tv_tr_sub_details_deal_amount);
            tvBudget=itemView.findViewById(R.id.tv_tr_sub_details_budget);
            tvDetails=itemView.findViewById(R.id.tv_tr_sub_details_deal_details);
        }
    }
}
