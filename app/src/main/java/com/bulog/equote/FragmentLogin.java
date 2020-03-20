package com.bulog.equote;

import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentLogin extends Fragment {

    Button btn_sign_with_google,btn_sign_in;
    TextView sign_up_text_btn;
    CallbackFragment callbackFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);
        sign_up_text_btn = view.findViewById(R.id.sign_up_text_btn);

        sign_up_text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callbackFragment!=null){
                    callbackFragment.changeFragment();
                }
            }
        });

        return view;
    }
    public void setCallbackFragment(CallbackFragment callbackFragment){
       this.callbackFragment = callbackFragment;
    }
}
