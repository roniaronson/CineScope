package com.example.retrofitwapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.button.MaterialButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Movie> movieList;
    private List<Movie> recentViewMoviesList;

    private MaterialButton btnBack;
    private InterstitialAd mInterstitial;

    private TextView textView;

    private String url = "";

    private Helper helper = Helper.getInstance();
    private boolean isNoAds;
    private boolean isUpcoming;
    private boolean isUltimate;

   // private MyDB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBack = findViewById(R.id.btnBackFromMainPage);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textView = findViewById(R.id.textView);

        requestQueue = VolleySingelton.getmIstance(this).getRequestQueue();

        movieList = new ArrayList<>();
        recentViewMoviesList = new ArrayList<>();
        Intent i = getIntent();
        this.url = i.getStringExtra("url");
        textView.setText(i.getStringExtra("textView"));


        fetchMovies();
        fetchDataFromHelper();

        initInterstitial();

        final Handler handler = new Handler();

        if(isNoAds || isUltimate){

        }
        else{

            final int delay =30000; // 1000 milliseconds == 1 second

            handler.postDelayed(new Runnable() {
                public void run() {
                    loadAd();
                    showInterstitial();// Do your work here
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }



        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MenuActivity.class);
            handler.removeCallbacksAndMessages(null);
            startActivity(intent);
            finish();
        });

    }

    private void fetchDataFromHelper() {
        this.isNoAds = helper.isNoAds();
        this.isUpcoming = helper.isUpcoming();
        this.isUltimate = helper.isUltimate();
    }

    private void showInterstitial() {
        if (mInterstitial != null) {
            mInterstitial.show(MainActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    private void initInterstitial() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        loadAd();
    }


    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitial = interstitialAd;
                        // Log.i(Constants.TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.d(Constants.TAG, loadAdError.toString());
                        mInterstitial = null;
                    }
                });
    }


    private void fetchMovies() {

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("original_title");
                            Double rating = jsonObject.getDouble("vote_average");
                            String overView = jsonObject.getString("overview");
                            String poster = jsonObject.getString("poster_path");
                            Movie movie = new Movie(title, poster, overView, rating);
                            movieList.add(movie);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MovieAdapter adapter = new MovieAdapter(MainActivity.this, movieList);
                    recyclerView.setAdapter(adapter);
                }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
    });
        requestQueue.add(jsonArrayRequest);
    }
}