package com.example.countsksh.interfaces;

import com.example.countsksh.models.DealsModel;

import java.util.ArrayList;

public interface recyclerViewInterface {
    public void onAddButtonClick(int p);
    public void onItemClick(int pos);
    public void onItemLogClick(int pos);
    public void setSelectedItems(ArrayList<DealsModel> selectedItems);

}
