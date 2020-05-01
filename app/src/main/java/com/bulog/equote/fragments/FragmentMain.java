package com.bulog.equote.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulog.equote.AuthActivity;
import com.bulog.equote.ProductListFragment;
import com.bulog.equote.R;
import com.bulog.equote.RpkFullActivity;
import com.bulog.equote.adapter.MainMenuTabAdapter;
import com.bulog.equote.databinding.FragmentMainBinding;
import com.bulog.equote.model.RPKMap;
import com.bulog.equote.model.SmallPromo;
import com.bulog.equote.model.smallproduct.DataSmallProduct;
import com.bulog.equote.model.smallproduct.SmallProduct;
import com.bulog.equote.model.UserModel;
import com.bulog.equote.utils.*;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jama.carouselview.CarouselViewListener;
import com.kennyc.view.MultiStateView;

import androidx.fragment.app.FragmentManager;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentMainBinding binding;
    private SPService sharedPreferenceService;
    private GoogleMap rpkMap;
    private RpkMapUtil rpkMapUtil;

    private GPSTracker gpsTracker;
    private GPSTracker.LocationResult locationResult;

    private OnFragmentInteractionListener mListener;
    private Location location;

    private final Gson gson = new Gson();

    private List<SmallPromo> promoList = new ArrayList<>();

    //TODO: Remove this after retrieving data from API
    private ArrayList<DataSmallProduct> mockData = new ArrayList<>();
    private ArrayList<DataSmallProduct> products = new ArrayList<>();

    private MainMenuTabAdapter pageAdapter;

    public FragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Hide the view before loading it inside success api listener
        binding.promoCarouselview.setVisibility(View.GONE);
        binding.seeMoreMapBtn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), RpkFullActivity.class);
            startActivity(i);
        });

        sharedPreferenceService = new SPService(getContext());

        UserModel user = sharedPreferenceService.getUserFromSp();

        if (user == null) {
            binding.userNameOrLoginButton.setText(R.string.login);
            binding.userNameOrLoginButton.setOnClickListener(v -> {
                Intent i = new Intent(getActivity(), AuthActivity.class);
                startActivity(i);
            });
        } else {
            binding.userNameOrLoginButton.setText(user.getFullname());
        }

        if (rpkMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.rpk_main_menu_map);
            mapFragment.getMapAsync(FragmentMain.this);
        }

        locationResult = new GPSTracker.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (location == null) {
                    Log.e("onGotLocation", "gotLocation: ?");
                    return;
                }
                LatLng userPos = new LatLng(location.getLatitude(), location.getLongitude());
                rpkMapUtil = new RpkMapUtil(rpkMap, userPos, getActivity());
                rpkMapUtil.getRpkMapFromServer();
                FragmentMain.this.location = location;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rpkMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPos, 15));

                    }
                });
            }
        };

        gpsTracker = new GPSTracker();

        ApiService service = ApiCall.getClient().create(ApiService.class);
        Call<JsonObject> promoCall = service.getPromo();
        promoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    binding.promoShimmerlayout.stopShimmer();
                    Toasty.error(getContext(), R.string.err_general_api_error);
                    return;
                }

                binding.promoShimmerlayout.stopShimmer();
                binding.promoShimmerlayout.setVisibility(View.GONE);

                JsonElement json = response.body().get("data");
                promoList = gson.fromJson(json, new TypeToken<List<SmallPromo>>() {
                }.getType());

                binding.promoCarouselview.setVisibility(View.VISIBLE);
                binding.promoCarouselview.setSize(promoList.size());
                binding.promoCarouselview.setResource(R.layout.promo_recyclerview_layout);
                binding.promoCarouselview.setCarouselViewListener(new CarouselViewListener() {
                    @Override
                    public void onBindView(View view, int i) {
                        ImageView imageView = view.findViewById(R.id.promo_layout_imageview);
                        Glide.with(getContext()).load(Constant.IMAGE_PROMO_BASE_URL + promoList.get(i).getImage()).fitCenter().into(imageView);
                        //TODO: ADD A CLICK LISTENER INTO THE IMAGE VIEW
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO: Go to product detail activity/fragment, passing the serializable object
                                Toasty.info(getContext(), "clicked: " + promoList.get(i).getPromoTitle()).show();
                            }
                        });
                    }
                });
                binding.promoCarouselview.show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                binding.promoShimmerlayout.stopShimmer();
                Toasty.error(getContext(), R.string.err_general_api_error);
                t.printStackTrace();
            }
        });

        binding.seeMoreProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductListFragment productListFragment = new ProductListFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.mainFrameLayout, productListFragment).addToBackStack(null).commit();
            }
        });

        ApiService productService = ApiCall.getClient().create(ApiService.class);
        Call<JsonObject> productCall = productService.getProduct();
        productCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Toasty.error(getContext(), R.string.err_general_api_error).show();
                    return;
                }
                JsonElement resp = response.body().get("data");
                products = gson.fromJson(resp, new TypeToken<List<DataSmallProduct>>() {}.getType());

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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void checkForLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gpsTracker.getLocation(getActivity(), locationResult);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            }
        } else {
            gpsTracker.getLocation(getActivity(), locationResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gpsTracker.getLocation(getActivity(), locationResult);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogBuilder.setTitle(getString(R.string.permission_required));
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setMessage(getString(R.string.permission_required_message));
                    dialogBuilder.setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getContext().getPackageName(), null));
                            startActivityForResult(i, 1001);
                        }
                    });
                    dialogBuilder.create().show();
                }

                binding.mapMultistateview.setViewState(MultiStateView.ViewState.ERROR);
            }
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

        switch (requestCode) {
            case 1001:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        gpsTracker.getLocation(getActivity(), locationResult);
                    }

                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
                }
                break;
            default:
                break;
        }
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
    public void onMapReady(GoogleMap googleMap) {
        rpkMap = googleMap;

        LatLng pos = new LatLng(0.7893, 113.9213);
        rpkMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 11));
        checkForLocationPermission();

        UiSettings configs = rpkMap.getUiSettings();
        configs.setMapToolbarEnabled(false);
        configs.setZoomControlsEnabled(false);
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
