package com.example.learnaejmovielist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.example.learnaejmovielist.adapter.AdapterMovie;
import com.example.learnaejmovielist.model.MovieModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "AEJFAN";
    private RecyclerView recyclerView;
    private AdapterMovie adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm_movie = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm_movie);
        recyclerView.setAdapter(adapter);

        AndroidNetworking.initialize(this);

        AndroidNetworking.get("https://api.themoviedb.org/3/discover/movie?api_key=1557853c72890ec559e5ce537b9adb24&language=en-US")
                .setPriority(Priority.LOW)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        Log.e(TAG, "onResponse: "+response );
                        try {
                            //Cara looping per index
//                            ArrayList<MovieModel> movieModelArrayList = new ArrayList<>();
//
//                            JSONObject json = new JSONObject(response.toString());
//                            JSONArray results = json.getJSONArray("results");
//                            for (int i = 0; i< results.length(); i++){
//                                JSONObject jsonObject = results.getJSONObject(i);
//                                String title = jsonObject.getString("title");
//                                String poster_path = jsonObject.getString("poster_path");
//                                String release_date = jsonObject.getString("release_date");
//                                String overview = jsonObject.getString("overview");
//
//                                MovieModel movieModel = new MovieModel();
//                                movieModel.setOverview(overview);
//                                movieModel.setTitle(title);
//                                movieModel.setPosterPath(poster_path);
//                                movieModel.setReleaseDate(release_date);
//                                movieModelArrayList.add(movieModel);
//                                // masukin datanya ke recycler view.
//
//                            }
                            //End cara looping per index

                            //Cara gson from json
                            Type listType = new TypeToken<List<MovieModel>>() {}.getType();

                            Gson gson = new Gson();
                            ArrayList<MovieModel> listMovie = gson.fromJson(response.getJSONArray("results").toString(), listType);

                            Log.d("coba", "target2list : " + listMovie.get(0).getTitle());

                            adapter = new AdapterMovie(MainActivity.this, listMovie);

                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                            //END cara gson from json

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
