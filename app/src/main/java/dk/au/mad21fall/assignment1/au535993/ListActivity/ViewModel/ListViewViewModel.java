package dk.au.mad21fall.assignment1.au535993.ListActivity.ViewModel;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader.MovieDataLoader;
import dk.au.mad21fall.assignment1.au535993.Repository.MovieRepository;


public class ListViewViewModel extends ViewModel {

    private LiveData<List<MovieEntity>> liveMovieDataList;
    private MovieDataLoader movieDataLoader;
    private MovieRepository movieRepository;

    public void initViewModel(AppCompatActivity app){
        if (liveMovieDataList == null) {
            movieDataLoader = new MovieDataLoader();
            movieRepository = MovieRepository.getInstance(app.getApplicationContext());
            liveMovieDataList = movieRepository.getAllAsLive();

            // TODO disable when not testing
            //movieRepository.deleteAllAsync();

            liveMovieDataList.observe(app, new Observer<List<MovieEntity>>() {
                @Override
                public void onChanged(@Nullable List<MovieEntity> movieEntities) {
                    // TEMP DATA -- add 3 movies not database empty
                    // if database is empty add from .csv file
                    if (movieEntities.size() == 0){
                        ArrayList<MovieEntity> movieArrayList = movieDataLoader.loadMovieData(app.getApplicationContext());
                        for (int i = 0; i<3; i++) {
                            movieRepository.addMovieAsync(movieArrayList.get(i));
                        }
                    }
                }
            });
        }
    }

    public LiveData<List<MovieEntity>> getMovieData()
    {
        return liveMovieDataList;
    }

    public void addNewMovieIfExits(String movieName, Context context){
        movieRepository.searchMovie(movieName, context);
    }

}
