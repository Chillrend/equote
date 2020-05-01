package com.bulog.equote;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.bulog.equote.databinding.ActivityMainBinding;
import com.bulog.equote.databinding.ActivityRpkFullBinding;
import com.bulog.equote.model.RPKMap;
import com.bulog.equote.utils.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.kennyc.view.MultiStateView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class RpkFullActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap rpkMap;
    private GPSTracker gpsTracker;
    private GPSTracker.LocationResult locationResult;
    private PermissionUtil permission;
    private ActivityRpkFullBinding binding;
    private RpkMapUtil rpkMapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRpkFullBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.searchMapBtn.setOnClickListener(v -> {
            LatLng coord = rpkMap.getCameraPosition().target;
            rpkMap.clear();
            rpkMapUtil.updateLocation(coord);
            rpkMapUtil.getRpkMapFromServer();
        });

        binding.searchMapBtn.setVisibility(View.GONE);

        permission = new PermissionUtil(this);

        locationResult = new GPSTracker.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (location == null) {
                    Log.e("onGotLocation", "gotLocation: ?");
                    return;
                }

                Toasty.info(RpkFullActivity.this, "Location : " + location.getLongitude() + ", " + location.getLatitude(), Toasty.LENGTH_LONG).show();
                LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());
                runOnUiThread(() -> {
                    rpkMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 13));
                });
                rpkMapUtil = new RpkMapUtil(rpkMap, coord, RpkFullActivity.this);
                rpkMapUtil.getRpkMapFromServer();
            }
        };

        gpsTracker = new GPSTracker();

        if (permission.isLocationPermissionGranted()) {
            gpsTracker.getLocation(this, locationResult);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gpsTracker.getLocation(this, locationResult);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle(getString(R.string.permission_required));
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setMessage(getString(R.string.permission_required_message));
                    dialogBuilder.setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                            startActivityForResult(i, 1001);
                        }
                    });
                    dialogBuilder.create().show();
                }
            }
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

        switch (requestCode) {
            case 1001:
                if(permission.isLocationPermissionGranted()){
                    gpsTracker.getLocation(RpkFullActivity.this, locationResult);
                }else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        rpkMap = googleMap;
        binding.searchMapBtn.setVisibility(View.VISIBLE);

        LatLng pos = new LatLng(0.7893, 113.9213);
        rpkMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 11));

        UiSettings configs = rpkMap.getUiSettings();
        configs.setMapToolbarEnabled(false);
        configs.setZoomControlsEnabled(false);
    }
}
