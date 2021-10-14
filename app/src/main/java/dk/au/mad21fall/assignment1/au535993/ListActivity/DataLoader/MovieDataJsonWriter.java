package dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader;

import android.graphics.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import dk.au.mad21fall.assignment1.au535993.ListActivity.Models.MovieData;

public class MovieDataJsonWriter {

    public static JSONObject convertMovieDataToJson(MovieData movieData){
        JSONObject obj = new JSONObject();
        //public String name,genre, plot, year, rating, notes, userRating;
        try {
            obj.put("name", movieData.name);
            obj.put("genre", movieData.genre);
            obj.put("plot", movieData.plot);
            obj.put("year", movieData.year);
            obj.put("rating", movieData.rating);
            obj.put("notes", movieData.notes);
            obj.put("userRating", movieData.userRating);
            obj.put("position", movieData.position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static MovieData convertJsonToMovieData(JSONObject jsonObject){
        MovieData movieData = null;
        try {
            movieData = new MovieData(
                    jsonObject.get("name").toString(),
                    jsonObject.get("genre").toString(),
                    jsonObject.get("plot").toString(),
                    jsonObject.get("year").toString(),
                    jsonObject.get("rating").toString());

            movieData.userRating = jsonObject.get("userRating").toString();
            movieData.notes = jsonObject.get("notes").toString();
            movieData.position = jsonObject.get("position").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieData;
    }
}
