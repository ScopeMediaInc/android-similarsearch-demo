package com.scopemedia.android_similarsearch_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Maikel Rehl on 3/8/2017.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> {

    private String[] itemList;
    private Context context;

    RecyclerViewAdapter(Context context, String[] itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        Picasso.with(context)
                .load(itemList[position])
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.resultImage);
    }

    @Override
    public int getItemCount() {
        return this.itemList.length;
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {

        ImageView resultImage;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            resultImage = (ImageView)itemView.findViewById(R.id.resultImageItem);
        }
    }
}