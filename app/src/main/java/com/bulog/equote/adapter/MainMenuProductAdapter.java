package com.bulog.equote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bulog.equote.R;
import com.bulog.equote.model.smallproduct.DataSmallProduct;
import com.bulog.equote.model.smallproduct.SmallProduct;
import com.bulog.equote.utils.Constant;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainMenuProductAdapter extends RecyclerView.Adapter<MainMenuProductAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        ImageView productImage;
        TextView productTitle;
        TextView productShortDescr;
        OnMainMenuProductClickListener clickListener;

        public ViewHolder(View itemView, OnMainMenuProductClickListener clickListener){
            super(itemView);

            cv = itemView.findViewById(R.id.item_mainmenu_product_wrapper);
            productImage = itemView.findViewById(R.id.rv_product_image);
            productTitle = itemView.findViewById(R.id.rv_product_name);
            productShortDescr = itemView.findViewById(R.id.rv_product_short_desc);
            this.clickListener = clickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onProductClick(getAdapterPosition());
        }
    }
    
    private ArrayList<SmallProduct> itemList;
    private Context appCtx;
    private OnMainMenuProductClickListener clickListener;

    public MainMenuProductAdapter(ArrayList<SmallProduct> itemList, Context appCtx, OnMainMenuProductClickListener clickListener){
        this.itemList = itemList;
        this.appCtx = appCtx;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount(){
        return itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view_mainmenu_product, viewGroup, false);

        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        SmallProduct sp = itemList.get(position);

        Glide.with(appCtx).load(Constant.IMAGE_PRODUCT_BASE_URL + sp.getImageUrl()).fitCenter().into(holder.productImage);
        holder.cv.setCardBackgroundColor(Color.parseColor(sp.getColor()));
        holder.productTitle.setText(sp.getProductName());
        holder.productShortDescr.setText(sp.getShortDesc());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

    }

    public interface OnMainMenuProductClickListener{
        void onProductClick(int pos);
    }

}
