package com.bulog.equote.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bulog.equote.MainActivity;
import com.bulog.equote.R;
import com.bulog.equote.databinding.RegisterFragmentBinding;
import com.bulog.equote.utils.ApiCall;
import com.bulog.equote.utils.ApiService;
import com.bulog.equote.utils.SPService;
import com.google.gson.JsonObject;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRegister extends Fragment {
    Button btn_done;
    public static String FRAGMENT_TAG = "REGISTER";
    private RegisterFragmentBinding binding;
    private ApiService service;
    private SPService sharedPreferenceService;
    private String fullname, email, password, c_password, address, phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RegisterFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        sharedPreferenceService = new SPService(getContext());


        //TODO: EDIT WHEN SELECTING ROLE IN REGISTER IS AVAILABLE
        final int role = 1;

        binding.btnDone.setOnClickListener(v -> {

            fullname = binding.registerFullname.getText().toString().trim();
            email = binding.registerEmail.getText().toString().trim();
            password = binding.registerPwd.getText().toString();
            c_password = password;
            address = binding.registerAddress.getText().toString().trim();
            phone = binding.registerPhone.getText().toString();

            if (fullname.length() < 1 || email.length() < 1 || password.length() < 1 || address.length() < 1 || phone.length() < 1) {
                Toasty.error(getContext(), R.string.all_field_required, Toasty.LENGTH_LONG).show();
                return;
            }

            service = ApiCall.getClient().create(ApiService.class);
            Call<JsonObject> call = service.register(fullname, email, password, c_password, address, phone, role);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.errorBody() != null){
                        Toasty.error(getContext(), R.string.all_field_required, Toasty.LENGTH_LONG).show();
                        return;
                    }

                    JsonObject parsed_response = response.body();
                    JsonObject success_obj = parsed_response.getAsJsonObject("success");
                    String token = success_obj.get("token").getAsString();

                    sharedPreferenceService.setUserToSp(token);
                    Toasty.success(getContext(), "Successfully Register").show();

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        });
    }
}
