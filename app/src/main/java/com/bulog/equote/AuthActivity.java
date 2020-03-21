package com.bulog.equote;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.bulog.equote.fragments.*;


public class AuthActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private static final String ROOT_FRAGMENT = "ROOT_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        addInitialFragment();
    }

    @Override
    public void onBackPressed(){
        FragmentLogin fr = (FragmentLogin) getSupportFragmentManager().findFragmentByTag(ROOT_FRAGMENT);
        FragmentManager fm = getSupportFragmentManager();

        if(fr != null && !fr.isVisible()){
            super.onBackPressed();
        }else if(fm.getBackStackEntryCount() > 1){
            fm.popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    private void addInitialFragment(){
        FragmentLogin fragment = new FragmentLogin();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment, ROOT_FRAGMENT);
        fragmentTransaction.commit();
    }

    public void changeFragment(Fragment fr, String tag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragmentContainer, fr, tag);
        ft.commit();
    }

}
