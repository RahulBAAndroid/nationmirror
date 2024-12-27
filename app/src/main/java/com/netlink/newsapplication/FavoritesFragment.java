package com.netlink.newsapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlink.newsapplication.adapters.FavoritesAdapter;
import com.netlink.newsapplication.FullRecentNewsActivity.FavoriteItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private static final String SHARED_PREF = "favoritesPrefs";
    private static final String FAVORITES_KEY = "favoritesKey";
    private List<FavoriteItem> favoritesList;
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private TextView emptyMessage; // TextView for empty message

    public static FavoritesFragment newInstance(){
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        emptyMessage = view.findViewById(R.id.tvEmptyMessage); // TextView for the message
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load favorites from SharedPreferences
        favoritesList = getFavorites();
        if (favoritesList.isEmpty()) {
            // Show the empty message and hide the RecyclerView
            emptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            // Hide the empty message and show the RecyclerView
            emptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Set up the adapter
            adapter = new FavoritesAdapter(favoritesList, getContext());
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private List<FavoriteItem> getFavorites() {
        String json = requireContext().getSharedPreferences(SHARED_PREF, requireContext().MODE_PRIVATE).getString(FAVORITES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FavoriteItem>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }
}
