package dk.au.mad21fall.assignment1.au535993.Repository;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;

public class MovieDataRequester {

    private static final String DATA_FETCHER = "DATA_FETCHER";
    private RequestQueue queue;
    //private static final String REQUEST_URL = "https://www.omdbapi.com/?t=terminator&apikey=14edbfd2";
    private static final String REQUEST_URL = "https://www.omdbapi.com/";
    private static final String API_KEY = "14edbfd2";

    public MovieDataRequester(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void requestMovieData(String movieName, Context context, SuccesMovieExecutor executor){
        // encode spaces in movie name
        String encodeMovieName = movieName.replace(" ", "%20");
        String requestUrl = REQUEST_URL + "?t=" + encodeMovieName + "&apikey=" + API_KEY;

        StringRequest movieDataRequest = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MovieEntity movieEntity = parseResponseToFromJson(response);
                if (movieEntity.name == null){
                    Toast.makeText(context,"movie does not exits",Toast.LENGTH_SHORT).show();
                }else
                {
                    executor.ExecuteSucces(movieEntity, context);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"movie does not exits",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(movieDataRequest);
    }

    public MovieEntity parseResponseToFromJson(String response) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        MovieEntity parsedMovie = gson.fromJson(response, MovieEntity.class);
        parsedMovie.setNotes("");
        parsedMovie.setUserRating("X.X");

        return parsedMovie;
    }
}
