package com.example.retrofitwapi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class PurchaseActivity  extends AppCompatActivity {
    private MaterialButton btnPurchaseRemoveAds;
    private MaterialButton btnPurchaseUpcoming;
    private MaterialButton btnPurchaseUltimate;
    private MaterialButton btnBack;
    private LinearLayout llRemoveAds;
    private LinearLayout llUpcoming;
    private LinearLayout llUltimate;
    private AlertDialog.Builder builder;
    private Helper helper = Helper.getInstance();
    private boolean isNoAds;
    private boolean isUpcoming;
    private boolean isUltimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);


        this.builder = new AlertDialog.Builder(this);
        fetchDataFromHelper();
        findViews();
        disableButtons();

        btnPurchaseRemoveAds.setOnClickListener(view -> {
            purchaseDialog("Remove Ads", Constants.NO_ADS);
        });

        btnPurchaseUpcoming.setOnClickListener(view -> {
            purchaseDialog("Upcoming movies", Constants.UPCOMING);
        });

        btnPurchaseUltimate.setOnClickListener(view -> {
            purchaseDialog("Ultimate Pack", Constants.ULTIMATE);
        });
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }


    private void fetchDataFromHelper() {
        this.isNoAds = helper.isNoAds();
        this.isUpcoming = helper.isUpcoming();
        this.isUltimate = helper.isUltimate();
    }

    /**
     * Buttons which the user already purchased will be disabled
     */
    private void disableButtons() {

        if (isNoAds) {
            disableNoAdsBtn();
        }
        if (isUpcoming) {
            disableUpcomingBtn();
        }
        if (isUltimate) {
            disableUltimateBtn();
            disableUpcomingBtn();
            disableNoAdsBtn();
        }
        if(isNoAds && isUpcoming)
            disableUltimateBtn();
    }

    private void disableUltimateBtn() {
        btnPurchaseUltimate.setEnabled(false);
        btnPurchaseUltimate.setAlpha(0.5f);
        llUltimate.setAlpha(0.5f);
        disableNoAdsBtn();
        disableUpcomingBtn();
    }

    private void disableNoAdsBtn() {
        btnPurchaseRemoveAds.setEnabled(false);
        btnPurchaseRemoveAds.setAlpha(0.5f);
        llRemoveAds.setAlpha(0.5f);
    }

    private void disableUpcomingBtn() {
        btnPurchaseUpcoming.setEnabled(false);
        btnPurchaseUpcoming.setAlpha(0.5f);
        llUpcoming.setAlpha(0.5f);
    }

    private void purchaseDialog(String title, String purchase_type) {
        builder.setTitle(title);
        builder.setMessage("Are you sure you want to buy the following " + purchase_type + "?");
        builder.setCancelable(true);
        builder.setNeutralButton(
                "Yes",
                (dialog, id) -> {
                    addUserPermissionAfterPurchase(purchase_type);
                    Toast.makeText(this, "Purchased completed", Toast.LENGTH_SHORT).show();
                    disableButtons();
                    reloadActivity();
                });
        builder.setPositiveButton(
                "No",
                (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void reloadActivity() {
        Intent i = new Intent(PurchaseActivity.this, PurchaseActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    private void addUserPermissionAfterPurchase(String purchase_type) {
       if(purchase_type.toString()=="remove_ads"){
           helper.setNoAds(true);
           this.isNoAds = true;
       }

       if(purchase_type.toString()=="upcoming_movies"){
           helper.setUpcoming(true);
           this.isUpcoming = true;
       }

       if(purchase_type.equals("ultimate_pack")){
           helper.setUltimate(true);
           this.isUltimate = true;
       }
    }

    private void findViews() {
        btnPurchaseRemoveAds = findViewById(R.id.btnPurchaseRemoveAds);
        btnPurchaseUpcoming = findViewById(R.id.btnPurchaseUpcoming);
        btnPurchaseUltimate = findViewById(R.id.btnPurchaseUltimate);
        btnBack = findViewById(R.id.btnBackFromPurchasePage);
        llRemoveAds = findViewById(R.id.noAds);
        llUpcoming = findViewById(R.id.upcoming);
        llUltimate = findViewById(R.id.ultimate);
    }
}

