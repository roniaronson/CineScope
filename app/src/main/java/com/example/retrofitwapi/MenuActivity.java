package com.example.retrofitwapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private LinearLayout lPopular;
    private LinearLayout lTopRated;
    private LinearLayout lUpcoming;
    private AdView mAdView;
    private MaterialButton btnSettings;
    private MaterialButton btnPremium;

    private Helper helper = Helper.getInstance();
    private boolean isNoAds;
    private boolean isUpcoming;
    private boolean isUltimate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        fetchDataFromHelper();
        findViews();
        checkPurchases();






       lPopular.setOnClickListener(view -> popularMovies());
       lTopRated.setOnClickListener(view -> topRatedMovies());
       lUpcoming.setOnClickListener(view -> upcomingMovies());
       btnSettings.setOnClickListener(view -> openSettings());
        btnPremium.setOnClickListener(view -> moveToPurchasePage());


    }

    private void checkPurchases() {
        if(isNoAds || isUltimate){
            this.mAdView.setVisibility(View.GONE);

        }
        else{
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        if(!isUpcoming){
            lUpcoming.setAlpha(0.5f);
            lUpcoming.setEnabled(false);
        }
        else{
            lUpcoming.setAlpha(1f);
            lUpcoming.setEnabled(true);
        }
        if(isUltimate){
            lUpcoming.setAlpha(1f);
            lUpcoming.setEnabled(true);
        }

    }

    private void fetchDataFromHelper() {
        this.isNoAds = helper.isNoAds();
        this.isUpcoming = helper.isUpcoming();
        this.isUltimate = helper.isUltimate();
    }

    private void moveToPurchasePage() {
        Intent i = new Intent(this, PurchaseActivity.class);
        startActivity(i);
    }

    private void openSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Settings");
        builder.setCancelable(true);
        builder.setNeutralButton(
                "Privacy Policy",
                (dialog, id) -> openUrl(getString(R.string.privacy_url)));//Toast.makeText(MainPageActivity.this, "Changed to Hebrew", Toast.LENGTH_SHORT).show());
        builder.setPositiveButton(
                "Terms & Conditions",
                (dialog, id) -> openUrl(getString(R.string.terms_url)));//Toast.makeText(MainPageActivity.this, "Changed to English", Toast.LENGTH_SHORT).show());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openUrl(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    private void popularMovies() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("url", "https://api.themoviedb.org/3/movie/popular?api_key=89fe25dcd392210a4aa966b819f67155");
        i.putExtra("textView", "Popular Movies");
        startActivity(i);
    }
    private void topRatedMovies() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("url", "https://api.themoviedb.org/3/movie/top_rated?api_key=89fe25dcd392210a4aa966b819f67155");
        i.putExtra("textView", "Top Rated Movies");
        startActivity(i);
    }
    private void upcomingMovies() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("url", "https://api.themoviedb.org/3/movie/upcoming?api_key=89fe25dcd392210a4aa966b819f67155");
        i.putExtra("textView", "Upcoming Movies");
        startActivity(i);
    }

    private void findViews(){
        mAdView = findViewById(R.id.adView);

        lPopular = findViewById(R.id.popularMovies);
        lTopRated = findViewById(R.id.topRated);
        lUpcoming = findViewById(R.id.upcoming);
        btnSettings = findViewById(R.id.btnSettings);
        btnPremium = findViewById(R.id.btnPremium);

    }


}
