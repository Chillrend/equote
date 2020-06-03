package com.bulog.equote;

import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bulog.equote.databinding.ActivityPromoDetailBinding;
import com.bulog.equote.model.Promo;
import com.bulog.equote.utils.ApiCall;
import com.bulog.equote.utils.ApiService;
import com.bulog.equote.utils.Constant;
import com.bumptech.glide.Glide;
import com.kennyc.view.MultiStateView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PromoDetailActivity extends AppCompatActivity {
    private ActivityPromoDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPromoDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent i = getIntent();
        int promo_id = i.getIntExtra("promo_id", 0);

        if(promo_id == 0){
            this.finish();
            return;
        }

        ApiService service = ApiCall.getClient().create(ApiService.class);
        Call<Promo> call = service.getPromoDetailById(promo_id);

        call.enqueue(new Callback<Promo>() {
            @Override
            public void onResponse(Call<Promo> call, Response<Promo> response) {
                if(!response.isSuccessful()){
                    binding.promoMultistate.setViewState(MultiStateView.ViewState.ERROR);
                }

                Promo obj = response.body();
                Glide.with(PromoDetailActivity.this)
                        .load(Constant.IMAGE_PROMO_BASE_URL + obj.getImage())
                        .fitCenter()
                        .into(binding.promoHeader);

                binding.promoTitle.setText(obj.getPromoTitle());

                //TODO: Determine date format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                try {
                    Date startDate = sdf.parse(obj.getPromoStartDate());
                    Date endDate = sdf.parse(obj.getPromoEndDate());

                    SimpleDateFormat sdf_final = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault());
                    binding.promoTime.setText(sdf_final.format(startDate) + " - " + sdf_final.format(endDate));
                }catch (ParseException e){
                    e.printStackTrace();
                    binding.promoTime.setText(obj.getPromoStartDate() + " - " + obj.getPromoEndDate());
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.promoDesc.setText(Html.fromHtml(obj.getPromoDesc(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.promoDesc.setText(Html.fromHtml(obj.getPromoDesc()));
                }
            }

            @Override
            public void onFailure(Call<Promo> call, Throwable t) {
                binding.promoMultistate.setViewState(MultiStateView.ViewState.ERROR);
                t.printStackTrace();
            }
        });
    }
}
