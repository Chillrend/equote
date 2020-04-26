package com.bulog.equote.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulog.equote.R;
import com.bulog.equote.databinding.FragmentProduuctDetailBinding;
import com.bulog.equote.model.smallproduct.SmallProduct;
import com.bulog.equote.utils.ApiCall;
import com.bulog.equote.utils.ApiService;
import com.bulog.equote.utils.Constant;
import com.bumptech.glide.Glide;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_PRODUCT_ID = "PRODUCT_ID";
    private FragmentProduuctDetailBinding binding;


    // TODO: Rename and change types of parameters
    private String product_id;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param product_id id for retrieving product details via API.
     * @return A new instance of fragment ProduuctDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(String product_id) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_PRODUCT_ID, product_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product_id = getArguments().getString(ARG_PARAM_PRODUCT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProduuctDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ApiService service = ApiCall.getClient().create(ApiService.class);
        Call<SmallProduct> call = service.getProductDetailById(product_id);
        call.enqueue(new Callback<SmallProduct>() {
            @Override
            public void onResponse(Call<SmallProduct> call, Response<SmallProduct> response) {
                if(!response.isSuccessful()){
                    Toasty.error(getContext(), R.string.err_general_api_error).show();
                    getFragmentManager().popBackStackImmediate();
                    return;
                }
                SmallProduct sp = response.body();

                Glide.with(getContext()).load(Constant.IMAGE_PRODUCT_BASE_URL + sp.getImageUrl()).fitCenter().into(binding.productImage);
                binding.cardWrapper.setBackgroundColor(Color.parseColor(sp.getColor()));
                binding.productName.setText(sp.getProductName());
                binding.productShortDesc.setText(sp.getShortDesc());
                binding.productLongDesc.setText(sp.getLongDesc());
                binding.productPrice.setText("Rp. " + sp.getPrice() + " / Kg");
            }

            @Override
            public void onFailure(Call<SmallProduct> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(getContext(), R.string.err_general_api_error).show();
                getFragmentManager().popBackStackImmediate();
                return;
            }
        });
    }
}
