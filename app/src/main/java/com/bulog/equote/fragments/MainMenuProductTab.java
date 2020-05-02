package com.bulog.equote.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bulog.equote.R;
import com.bulog.equote.adapter.MainMenuProductAdapter;
import com.bulog.equote.databinding.FragmentMainMenuProductTabBinding;
import com.bulog.equote.model.smallproduct.SmallProduct;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenuProductTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenuProductTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuProductTab extends Fragment implements MainMenuProductAdapter.OnMainMenuProductClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_PRODUCT_LIST = "LIST_OF_PRODUCT";
    private static final String ARG_PARAM_ORIENTATION = "ORIENTATION";

    public static final String ProductURL = "productUrl";
    public static final String ProductName = "productName";
    public static final String ProductDesc = "productDesc";
    public static final String ProductDescLong = "productDescLong";
    public static final String ProductPrice = "productPrice";




    // TODO: Rename and change types of parameters
    private ArrayList<SmallProduct> productList;
    private int orientation;

    private OnFragmentInteractionListener mListener;
    private FragmentMainMenuProductTabBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private MainMenuProductAdapter adapter;

    public MainMenuProductTab() {
        // Required empty public constructor
    }

    /**
     *
     * @param products pass an array list of products from main fragment
     *                 to generate a list of product.
     * @return A new instance of fragment MainMenuProductTab.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuProductTab newInstance(ArrayList<SmallProduct> products, int orientation) {
        MainMenuProductTab fragment = new MainMenuProductTab();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM_PRODUCT_LIST, products);
        args.putInt(ARG_PARAM_ORIENTATION, orientation);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productList = getArguments().getParcelableArrayList(ARG_PARAM_PRODUCT_LIST);
            orientation = getArguments().getInt(ARG_PARAM_ORIENTATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainMenuProductTabBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        layoutManager = new LinearLayoutManager(getContext(), orientation, false);
        binding.mainMenuProductRecyclerview.setLayoutManager(layoutManager);

        adapter = new MainMenuProductAdapter(productList, getContext(), this::onProductClick, orientation);
        binding.mainMenuProductRecyclerview.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProductClick(int pos) {
        //TODO: Go to Detail Product Activity/Fragment, passing the corresponding item in the ArrayList
        SmallProduct sp = productList.get(pos);

        Fragment f = ProductDetailFragment.newInstance(sp.getId());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, f).addToBackStack(null).commit();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
