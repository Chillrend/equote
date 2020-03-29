package com.bulog.equote.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bulog.equote.AuthActivity;
import com.bulog.equote.DrawerActivity;
import com.bulog.equote.MainActivity;
import com.bulog.equote.databinding.LoginFragmentBinding;
import com.bulog.equote.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bulog.equote.utils.ApiCall;
import com.bulog.equote.utils.ApiService;
import com.bulog.equote.utils.SPService;
import com.google.gson.JsonObject;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends Fragment {

    private LoginFragmentBinding binding;
    private ApiService service;
    private SPService sharedPreferenceService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sharedPreferenceService = new SPService(getActivity());

        if(sharedPreferenceService.isUserLoggedIn()){
            Intent i = new Intent(getActivity(), DrawerActivity.class);
            startActivity(i);
            getActivity().finish();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        binding.signUpTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity) getActivity()).changeFragment(new FragmentRegister(), FragmentRegister.FRAGMENT_TAG);
            }
        });

        binding.btnSignIn.setOnClickListener(v -> {

            String email = binding.loginEmail.getText().toString().trim();
            String password = binding.pwdSignin.getText().toString();

            if(email.length() < 1){
                binding.emailInputLayout.setError(getString(R.string.email_cannot_be_empty));
                return;
            }else if(password.length() < 1){
                binding.pwdSigninLayout.setError(getString(R.string.password_cannot_be_empty));
                return;
            }

            service = ApiCall.getClient().create(ApiService.class);
            Call<JsonObject> call = service.login(email, password);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.errorBody() != null){
                        binding.emailInputLayout.setError("");
                        binding.pwdSigninLayout.setError(getString(R.string.wrong_email_or_password));
                        return;
                    }

                    JsonObject parsed_response = response.body();
                    JsonObject success_obj = parsed_response.getAsJsonObject("success");

                    String storeToSp = success_obj.toString();

                    sharedPreferenceService.setUserToSp(storeToSp);
                    Toasty.success(getContext(), "Successfully Logged in").show();

                    Intent i = new Intent(getActivity(), DrawerActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toasty.error(getContext(), "Failed to contact server, please check your internet connection").show();
                }
            });
        });
    }

}
