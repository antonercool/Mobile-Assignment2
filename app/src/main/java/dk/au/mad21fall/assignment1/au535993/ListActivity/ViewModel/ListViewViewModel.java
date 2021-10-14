package dk.au.mad21fall.assignment1.au535993.ListActivity.ViewModel;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader.MovieDataLoader;
import dk.au.mad21fall.assignment1.au535993.Repository.MovieRepository;


public class ListViewViewModel extends ViewModel {

    private LiveData<List<MovieEntity>> movieDataList;
    private MovieDataLoader movieDataLoader;
    private MovieRepository movieRepository;

    public void createMovieData(Context context){
        if (movieDataList == null) {
            movieDataLoader = new MovieDataLoader();
            movieRepository = new MovieRepository(context);
            movieDataList = movieRepository.getAllAsLive();

            // TEMP DATA
            // if database is empty add from .csv file
            if (movieDataList.getValue().size() == 0){
                ArrayList<MovieEntity> movieArrayList = movieDataLoader.loadMovieData(context);
                for (MovieEntity movie: movieArrayList) {
                    movieRepository.addMovie(movie);
                }
            }
        }
    }

    public LiveData<List<MovieEntity>> getMovieData()
    {
        return movieRepository.getAllAsLive();
    }


    public void dataUpdated(){
        //movieDataList.setValue(movieRepository.getAllMovies().getValue());
    }
}
