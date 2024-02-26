package com.example.countsksh.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.countsksh.helpers.DealWithStrings;

import com.example.countsksh.interfaces.recyclerViewInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countsksh.R;
import com.example.countsksh.models.DealsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.dealHolder> {
    private Context context;
    private ArrayList<DealsModel> dealsModels;
    private recyclerViewInterface recyclerViewInterface;

    private boolean isSelectedMode=false;
    private ArrayList<DealsModel> selectedDeals=new ArrayList<>();

    public DealsAdapter(Context context, ArrayList<DealsModel> dealsModels, recyclerViewInterface f) {
        this.context = context;
        this.dealsModels = dealsModels;
        this.recyclerViewInterface=f;


    }

    @NonNull
    @Override
    public dealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.deal_deatails_layout,parent,false);
        return new dealHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dealHolder holder, int position) {

        holder.tvDate.setText(dealsModels.get(position).getDate());
        holder.tvDetails.setText(dealsModels.get(position).getDetails());
        holder.tvAmount.setText(DealWithStrings.formatMoney(Math.abs(Math.abs(dealsModels.get(position).getAmount()))));
        if (dealsModels.get(position).getAmount() > 0) {
            Picasso.get().load( R.drawable.ic_up_navigate_green).into(holder.ivStatus);
        } else {
            Picasso.get().load( R.drawable.ic_down_navigate_red).into(holder.ivStatus);

        }
        holder.tvBudget.setText(DealWithStrings.formatMoney(dealsModels.get(position).getBudget()));


    }

    //

    @Override
    public int getItemCount() {
      return   dealsModels.size();
    }

    public class dealHolder extends RecyclerView.ViewHolder {
        ImageView ivStatus;
        TextView tvDate,tvDetails,tvAmount,tvBudget;
        public dealHolder(@NonNull View itemView) {
            super(itemView);
            ivStatus=itemView.findViewById(R.id.iv_details_deal_type);
            tvDate=itemView.findViewById(R.id.tv_details_deal_date);
            tvAmount=itemView.findViewById(R.id.tv_details_deal_amount);
            tvBudget=itemView.findViewById(R.id.tv_details_budget);
            tvDetails=itemView.findViewById(R.id.tv_details_deal_details);

            itemView.setOnClickListener(view -> {
                //
                if(isSelectedMode){
                    if(selectedDeals.contains(dealsModels.get(getAdapterPosition()))){
                        itemView.setBackgroundResource(R.color.InverseSurfaceVariantLight);
                        selectedDeals.remove(dealsModels.get(getAdapterPosition()));
                        recyclerViewInterface.setSelectedItems(selectedDeals);

                    }else {
                        itemView.setBackgroundResource(R.color.grey);
                        selectedDeals.add(dealsModels.get(getAdapterPosition()));
                        recyclerViewInterface.setSelectedItems(selectedDeals);

                    }
                    if(selectedDeals.size()==0){
                        isSelectedMode=false;
                    }
                }else {
                            recyclerViewInterface.onItemClick(getAdapterPosition());

                }

                //


            });
            itemView.setOnLongClickListener(view -> {
                isSelectedMode=true;
                if(selectedDeals.contains(dealsModels.get(getAdapterPosition()))){
                  itemView.setBackgroundResource(R.color.InverseSurfaceVariantLight);;
                    selectedDeals.remove(dealsModels.get(getAdapterPosition()));
                    recyclerViewInterface.setSelectedItems(selectedDeals);


                }else {
                    itemView.setBackgroundResource(R.color.grey);
                    selectedDeals.add(dealsModels.get(getAdapterPosition()));
                    recyclerViewInterface.setSelectedItems(selectedDeals);
//                    if(selectedDeals.size()==1){
//                            recyclerViewInterface.setSelectedItems(selectedDeals);
//                        }
//                    if(selectedDeals.size()>=1){
//                        recyclerViewInterface.setSelectedItems(selectedDeals);
//                    }

                }
                if(selectedDeals.size()==0){
                    isSelectedMode=false;
                    recyclerViewInterface.setSelectedItems(selectedDeals);
                }

                return true;
            });

        }
    }
}
