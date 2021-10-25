package dk.au.mad21fall.assignment1.au535993.DetailsActivity.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.Repository.MovieRepository;


public class DetailsViewModel extends ViewModel {

    private LiveData<MovieEntity> movieData;
    private MovieRepository movieRepository;

    public void initViewModel(Context context, int uid){
        if (movieData == null) {
            movieRepository = MovieRepository.getInstance(context);
            movieData = movieRepository.findMovieByIdAsLive(uid);
        }
    }

    public LiveData<MovieEntity> getMovieData()
    {
        return movieData;
    }

    public void updateMovieDataUserRating(String userRating)
    {
        MovieEntity entry = movieData.getValue();
        entry.setUserRating(userRating);
        movieRepository.updateMovie(entry);
    }

    public void updateMovieDataNotes(String notes)
    {
        MovieEntity entry = movieData.getValue();
        entry.setNotes(notes);
        movieRepository.updateMovie(entry);
    }

    public String getUserRating(){
        return movieData.getValue().getUserRating();
    }

    public void deleteMovie(){
        movieRepository.deleteMovieAsync(movieData.getValue());
    }

}
