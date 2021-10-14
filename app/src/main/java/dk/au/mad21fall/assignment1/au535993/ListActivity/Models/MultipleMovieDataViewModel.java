package dk.au.mad21fall.assignment1.au535993.ListActivity.Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

import dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader.MovieDataLoader;


public class MultipleMovieDataViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MovieData>> movieDataList;
    private MovieDataLoader movieDataLoader;

    public void createMovieData(Context context){
        if (movieDataList == null) {
            movieDataLoader = new MovieDataLoader();
            ArrayList<MovieData> movieArrayList = movieDataLoader.loadMovieData(context);
            movieDataList = new MutableLiveData<ArrayList<MovieData>>();
            movieDataList.setValue(movieArrayList);
        }
    }

    public LiveData<ArrayList<MovieData>> getMovieData()
    {
        return movieDataList;
    }

    public void updateMovieDataElement(MovieData newMovieDataElement){
        int position = Integer.parseInt(newMovieDataElement.position);
        movieDataList.getValue().set(position, newMovieDataElement);
        movieDataList.setValue(movieDataList.getValue());
    }
}
