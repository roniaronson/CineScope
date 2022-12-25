package com.example.retrofitwapi;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingelton {

    private RequestQueue requestQueue;
    private static VolleySingelton mIstance;

    private VolleySingelton(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingelton getmIstance(Context context){

        if(mIstance == null)
            mIstance = new VolleySingelton(context);

        return mIstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
