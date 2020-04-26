package com.bulog.equote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulog.equote.adapter.MainMenuTabAdapter;
import com.bulog.equote.databinding.FragmentProductListBinding;
import com.bulog.equote.fragments.FragmentMain;
import com.bulog.equote.model.SmallPromo;
import com.bulog.equote.model.smallproduct.DataSmallProduct;
import com.bulog.equote.utils.ApiCall;
import com.bulog.equote.utils.ApiService;
import com.bulog.equote.utils.GPSTracker;
import com.bulog.equote.utils.SPService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment{

    private FragmentProductListBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Gson gson = new Gson();

    private List<SmallPromo> promoList = new ArrayList<>();

    //TODO: Remove this after retrieving data from API
    private ArrayList<DataSmallProduct> mockData = new ArrayList<>();
    private ArrayList<DataSmallProduct> products = new ArrayList<>();
    private MainMenuTabAdapter pageAdapter;

    public ProductListFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }}

        public void onViewCreated (View view, Bundle savedInstanceState){

            ApiService productService = ApiCall.getClient().create(ApiService.class);
            Call<JsonObject> productCall = productService.getProduct();
            productCall.enqueue(new Callback<JsonObject>() {

                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (!response.isSuccessful()) {
                        Toasty.error(getContext(), R.string.err_general_api_error).show();
                        return;
                    }
                    JsonElement resp = response.body().get("data");
                    products = gson.fromJson(resp, new TypeToken<List<DataSmallProduct>>() {
                    }.getType());

                    pageAdapter = new MainMenuTabAdapter(getChildFragmentManager(), getLifecycle(), products);
                    binding.productViewPagerMainmenu.setAdapter(pageAdapter);
                    binding.productViewPagerMainmenu.setUserInputEnabled(false);
                    binding.productViewPagerMainmenu.setOffscreenPageLimit(4);

                    binding.productTabLayoutMainmenu.setTabMode(TabLayout.MODE_SCROLLABLE);
                    TabLayoutMediator mediator = new TabLayoutMediator(binding.productTabLayoutMainmenu, binding.productViewPagerMainmenu, (tab, i) -> tab.setText(products.get(i).getCategory()));
                    mediator.attach();

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toasty.error(getContext(), R.string.err_general_api_error).show();
                    t.printStackTrace();
                }
            });
        }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
