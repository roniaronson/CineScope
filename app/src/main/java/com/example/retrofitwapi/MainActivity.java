package com.example.retrofitwapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Movie> movieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = VolleySingelton.getmIstance(this).getRequestQueue();

        movieList = new ArrayList<>();

        fetchMovies();
    }

    private void fetchMovies() {

        String url = "https://api.themoviedb.org/3/movie/top_rated?api_key=89fe25dcd392210a4aa966b819f67155";

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