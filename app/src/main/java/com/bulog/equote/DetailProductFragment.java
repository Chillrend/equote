package com.bulog.equote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulog.equote.databinding.FragmentMainMenuProductTabBinding;
import com.bulog.equote.utils.Constant;
import com.squareup.picasso.Picasso;

import static com.bulog.equote.fragments.MainMenuProductTab.ProductDesc;
import static com.bulog.equote.fragments.MainMenuProductTab.ProductDescLong;
import static com.bulog.equote.fragments.MainMenuProductTab.ProductName;
import static com.bulog.equote.fragments.MainMenuProductTab.ProductPrice;
import static com.bulog.equote.fragments.MainMenuProductTab.ProductURL;

public class DetailProductFragment extends Fragment {


    public DetailProductFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Context c = getActivity().getApplicationContext();
        View RootView = inflater.inflate(R.layout.fragment_product_detail,container,false);

        Bundle bundle = getArguments();
        String imageUrl = bundle.getString(ProductURL);
        String productName = bundle.getString(ProductName);
        String shortDesc = bundle.getString(ProductDesc);
        String longDesc = bundle.getString(ProductDescLong);
        String price = bundle.getString(ProductPrice);

        ImageView imageView = (ImageView)RootView.findViewById(R.id.productImage);
        TextView textViewProduct = (TextView)RootView.findViewById(R.id.product);
        TextView textViewDescProduct = (TextView)RootView.findViewById(R.id.productdesc);
        TextView textViewFullDescProduct = (TextView)RootView.findViewById(R.id.productfulldesc);
        TextView textViewPrice = (TextView)RootView.findViewById(R.id.productprice);

        Picasso.with(c).load(Constant.IMAGE_PRODUCT_BASE_URL + imageUrl).fit().centerInside().into(imageView);
        textViewProduct.setText(productName);
        textViewDescProduct.setText(shortDesc);
        textViewFullDescProduct.setText(longDesc);
        textViewPrice.setText(price);

        return RootView;
    }
}
