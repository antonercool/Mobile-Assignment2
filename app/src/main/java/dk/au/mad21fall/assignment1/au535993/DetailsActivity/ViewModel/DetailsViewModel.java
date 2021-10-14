package dk.au.mad21fall.assignment1.au535993.DetailsActivity.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.Repository.MovieRepository;


public class DetailsViewModel extends ViewModel {

    private MutableLiveData<MovieEntity> movieData;
    private MovieRepository movieRepository;

    public void createMovieData(Context context, int uid){
        if (movieData == null) {
            movieRepository = new MovieRepository(context);
            MovieEntity movie = movieRepository.findMovieById(uid);
            movieData = new MutableLiveData<MovieEntity>(movie);
        }
    }

    public LiveData<MovieEntity> getMovieData()
    {
        return movieData;
    }

    public void movieDataUpdated(){
        MovieEntity updatedMovieData = movieRepository.findMovieById(this.movieData.getValue().uid);
        this.movieData.setValue(updatedMovieData);
    }

    public void updateMovieDataUserRating(String userRating)
    {
        movieData.getValue().setUserRating(userRating);
    }

    public void updateMovieDataNotes(String notes)
    {
        movieData.getValue().setNotes(notes);
    }

    public String getUserRating(){
        return movieData.getValue().getUserRating();
    }

    public void persist(){
        movieRepository.updateMovie(movieData.getValue());
    }

}
