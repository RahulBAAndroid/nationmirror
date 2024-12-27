package com.netlink.newsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class RecentNewsFragment extends Fragment {
    TextView txtSports,txtGlobal,txtEntertenment,txtBusiness,txtAstro,txtSearch;
    LinearLayout btnSports,btnGlobal;
    public static RecentNewsFragment newInstance() {
        RecentNewsFragment fragment = new RecentNewsFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          txtSports = view.findViewById(R.id.txtSportsNews);
          btnSports = view.findViewById(R.id.btnSport);
          btnSports.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent i = new Intent(getContext(),RecentNewsActivity.class);
                  i.putExtra("category","Sports");
                  startActivity(i);
              }
          });
          txtGlobal = view.findViewById(R.id.txtGlobalNews);
          view.findViewById(R.id.btnGlobal).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(getContext(),RecentNewsActivity.class);
                 i.putExtra("category","Global");
                 startActivity(i);
             }
         });
         txtEntertenment = view.findViewById(R.id.txtEntertenmntNews);
        view.findViewById(R.id.btnEntertenment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),RecentNewsActivity.class);
                i.putExtra("category","Entertainment");
                startActivity(i);
            }
        });
        txtAstro=view.findViewById(R.id.txtAstro);
        view.findViewById(R.id.btnAstro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),RecentNewsActivity.class);
                i.putExtra("category","Astro");
                startActivity(i);
            }
        });
        txtBusiness = view.findViewById(R.id.txtBusinessNews);
        view.findViewById(R.id.btnBusiness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),RecentNewsActivity.class);
                i.putExtra("category","Business");
                startActivity(i);
            }
        });
        txtSearch = view.findViewById(R.id.txtSarch);
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String searchQuery = txtSearch.getText().toString().trim();
                    Intent intent = new Intent(getContext(), RecentNewsActivity.class);
                    intent.putExtra("category","search");
                    intent.putExtra("SEARCH_QUERY", searchQuery);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_new_recent,container,false);
        return view;
    }
}
