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
import com.bulog.equote.DrawerActivity;
import com.bulog.equote.MainActivity;
import com.bulog.equote.R;
import com.bulog.equote.databinding.DoneFragmentBinding;

public class FragmentDone extends Fragment {
    DoneFragmentBinding binding;

    public static final String FRAGMENT_TAG = "FRAGMENT_DONE";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DoneFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DrawerActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
