package com.coma.go.Custom.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.coma.go.Utils.Logger;

import java.util.List;


/**
 * Created by Koma on 29.12.2017.
 */

public abstract class RecyclerAdapter<I, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> implements View.OnClickListener//, Filterable
{

    protected List<I> items;


    protected OnRecyclerViewItemClickListener<I> itemClickListener;
    protected int itemLayout;


    public RecyclerAdapter(List<I> items, int itemLayout, OnRecyclerViewItemClickListener<I> itemClickListener) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.itemClickListener = itemClickListener;

    }




    @Override
    public abstract void onBindViewHolder(final H holder, int position);

    @Override
    public int getItemCount(){
        if(items != null)
            return items.size();
        else
            return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View view) {
        I model = (I) view.getTag();
        if(model==null)
            Log.d("RecyclerAdapter", "model is null");
        else
            Log.d("RecyclerAdapter", "model is not null");
        itemClickListener.onItemClick(view, model);
    }

    public void add(I item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }


    public void addItems(List<I> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(I item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }


    public void removeAll() {
       // this.getRecycledViewPool().clear();
        if(items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public List<I> getItemList() {
        return items;
    }


    public I get(int pos){
        I i = null;
        try{
        i = items.get(pos);
        }catch (ArrayIndexOutOfBoundsException aoe){
            Logger.e("getItemList", aoe);
        }
        return i;
    }

}
