package com.netlink.newsapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {
    public static final String SHARED_PREF = "sharedPrefs";
    TextView btnLoginLogout,tvName,tvMobile,btnGeneral,btnPrivacy,btnAbout,btnFaveroite;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLoginLogout = view.findViewById(R.id.btnLogout);
        btnAbout = view.findViewById(R.id.btnAbout);
        btnPrivacy = view.findViewById(R.id.btnPrivacy);
        btnGeneral = view.findViewById(R.id.btnGeneral);
        btnFaveroite = view.findViewById(R.id.btnFavroite);
        tvName = view.findViewById(R.id.tvName);
        tvMobile = view.findViewById(R.id.tvProfileMobile);
        checkLogin();
        btnLoginLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                       if(btnLoginLogout.getText().toString().equals("Logout")){
                           SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, getContext().MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedPreferences.edit();
                           editor.putString("login","");
                           editor.apply();
                           Intent intent = new Intent(getContext(), DashBoardActivity.class);
                           startActivity(intent);
                           getActivity().finish();
                           onStop();
                       }
                if(btnLoginLogout.getText().toString().equals("Login")){
                    Intent intent = new Intent(getContext(), LoginAcctivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    onStop();
                }
            }

        });
        view.findViewById(R.id.btnAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btnPrivacyPolicy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btnDisclaimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DisclaimerActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btnFavroite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, FavoritesFragment.newInstance());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    private void checkLogin() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, getContext().MODE_PRIVATE);
        String check = sharedPreferences.getString("login","");
        if(check.equals("true"))
        {
            btnGeneral.setVisibility(View.VISIBLE);
            btnPrivacy.setVisibility(View.VISIBLE);
            btnFaveroite.setVisibility(View.VISIBLE);
            tvName.setText("Rahul");
            tvMobile.setText("123456789");
            btnLoginLogout.setText("Logout");

        }
        else
        {
            btnGeneral.setVisibility(View.GONE);
            btnPrivacy.setVisibility(View.GONE);
            btnFaveroite.setVisibility(View.GONE);
            tvName.setText("User");
            tvMobile.setText("Mobile Number");
            btnLoginLogout.setText("Login");
        }

    }
}
