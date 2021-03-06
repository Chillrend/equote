package com.bulog.equote;

import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.MenuItem;
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

        binding.promoBackbutton.setOnClickListener(v -> {
            this.finish();
        });

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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();//finish your activity
        }
        return super.onOptionsItemSelected(item);
    }
}
