package com.bulog.equote.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bulog.equote.R;
import com.bulog.equote.RpkFullActivity;
import com.bulog.equote.model.RPKMap;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class RpkMapUtil {
    private GoogleMap rpkMap;
    private LatLng location;
    private Activity activityContext;

    public RpkMapUtil(GoogleMap rpkMap, LatLng location, Activity activityContext) {
        this.rpkMap = rpkMap;
        this.location = location;
        this.activityContext = activityContext;
    }

    public void updateLocation(LatLng location){
        this.location = location;
    }

    public void getRpkMapFromServer(){
        ApiService service = ApiCall.getClient().create(ApiService.class);
        Call<List<RPKMap>> call = service.searchNearestRPK(location.latitude, location.longitude);
        call.enqueue(new Callback<List<RPKMap>>() {
            @Override
            public void onResponse(Call<List<RPKMap>> call, Response<List<RPKMap>> response) {
                if (response.errorBody() != null){
                    Toasty.error(activityContext, R.string.err_general_api_error, Toasty.LENGTH_LONG).show();
                    return;
                }

                BitmapDrawable bd = (BitmapDrawable) activityContext.getResources().getDrawable(R.drawable.ic_rpk_symetric);
                Bitmap smallb = Bitmap.createScaledBitmap(bd.getBitmap(), 100, 100, false);
                BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(smallb);

                List<Marker> markers = new ArrayList<>();
                LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();

                List<RPKMap> nearestRPK = response.body();
                activityContext.runOnUiThread(() -> {
                    for (RPKMap rpk : nearestRPK) {
                        LatLng pos = new LatLng(Double.parseDouble(rpk.getLatitude()), Double.parseDouble(rpk.getLongitude()));
                        Marker marker = rpkMap.addMarker(new MarkerOptions().title(rpk.getNamaRpk()).position(pos).icon(markerIcon));
                        marker.setTag(rpk.getId());
                        boundBuilder.include(marker.getPosition());
                    }
                });
                LatLngBounds bounds = boundBuilder.build();
                CameraUpdate camera = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                rpkMap.animateCamera(camera);
            }

            @Override
            public void onFailure(Call<List<RPKMap>> call, Throwable t) {
                Toasty.error(activityContext, R.string.err_general_api_error, Toasty.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
