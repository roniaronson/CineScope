package com.example.retrofitwapi;

public class Helper {
    private boolean isNoAds;
    private boolean isUpcoming;
    private boolean isUltimate;

    public static Helper instance;

    public static Helper getInstance()
    {
        if (instance == null)
        {
            instance = new Helper();
        }
        return instance;
    }

    public Helper() {
        isNoAds = false;
        isUpcoming = false;
        isUltimate = false;
    }

    public boolean isNoAds() {
        return isNoAds;
    }

    public void setNoAds(boolean noAds) {
        isNoAds = noAds;
    }

    public boolean isUpcoming() {
        return isUpcoming;
    }

    public void setUpcoming(boolean upcoming) {
        isUpcoming = upcoming;
    }

    public boolean isUltimate() {
        return isUltimate;
    }

    public void setUltimate(boolean ultimate) {
        isUltimate = ultimate;
    }
}
