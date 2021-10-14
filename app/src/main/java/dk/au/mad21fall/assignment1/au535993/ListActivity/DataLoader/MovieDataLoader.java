package dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dk.au.mad21fall.assignment1.au535993.ListActivity.Models.MovieData;

public class MovieDataLoader {

    public static MovieData loadDataFromIntent(Intent intent, String contantId){
        JSONObject movieDataInJson = null;
        MovieData movieData = null;
        try {
            movieDataInJson = new JSONObject(intent.getStringExtra(contantId));
            movieData = MovieDataJsonWriter.convertJsonToMovieData(movieDataInJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieData;
    }

    public ArrayList<MovieData> loadMovieData(Context context) {
        // load csv files and parse
        ArrayList<MovieData> movieDataList = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(context.getAssets()
                    .open("movie_data.csv"));

            BufferedReader reader = new BufferedReader(is);
            // the first line is collum values, which we don't need
            String line = reader.readLine();

            // parse .csv string to objects
            while ((line = reader.readLine()) != null) {
                String[] lineContents = line.split(",");
                String name = lineContents[0].trim();
                String genre = lineContents[1].trim();
                String year = lineContents[2].trim();
                String rating = lineContents[3].trim();
                String plot = "";

                // If plot has a ',' within the text it will have '"' around the text. This takes care of both scenario's
                if(lineContents[4].contains("\""))
                {
                    plot = line.split("\"")[1].trim();
                }
                else {
                    plot = lineContents[4].substring(0,lineContents[4].length()-2).trim();
                }

                MovieData movieData = new MovieData(name, genre, plot, year, rating);
                movieDataList.add(movieData);
            }
        } catch (IOException e) {
            Log.d("DataLoader", "Failed to load .csv file " + e.getStackTrace());
        }
        return movieDataList;
    }
}
