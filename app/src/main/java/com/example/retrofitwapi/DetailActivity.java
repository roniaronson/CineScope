package com.example.retrofitwapi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.poster_image);
        TextView overView = findViewById(R.id.moverview_tv);
        TextView title = findViewById(R.id.mTitle);
        TextView rating = findViewById(R.id.mRating);

        Bundle bundle = getIntent().getExtras();
        String mTitle = bundle.getString("title");
        String mPoster = bundle.getString("poster");
        String mOverView = bundle.getString("overView");
        Double mRating = bundle.getDouble("rating");

        Movie movie = new Movie(mTitle, mPoster, mOverView, mRating);

        Glide.with(this).load(mPoster).into(imageView);
        overView.setText(mOverView.toString());
        title.setText(mTitle.toString());
        rating.setText(mRating.toString());

    }
}
