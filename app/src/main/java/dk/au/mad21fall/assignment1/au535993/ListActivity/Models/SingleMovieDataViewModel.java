package dk.au.mad21fall.assignment1.au535993.ListActivity.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SingleMovieDataViewModel extends ViewModel {

    private MutableLiveData<MovieData> movieData;

    public void createMovieData(MovieData movieDataToCreate){
        if (movieData == null) {
            movieData = new MutableLiveData<MovieData>(movieDataToCreate);
        }
    }

    public LiveData<MovieData> getMovieData()
    {
        return movieData;
    }

    public void updateMovieData(MovieData movieData){
        this.movieData.setValue(movieData);
    }
    public void updateMovieDataName(String name)
    {
        movieData.getValue().name = name;
    }

    public void updateMovieDataGenre(String genre)
    {
        movieData.getValue().genre = genre;
    }

    public void updateMovieDataPlot(String plot)
    {
        movieData.getValue().plot = plot;
    }

    public void updateMovieDataYear(String year)
    {
        movieData.getValue().year = year;
    }

    public void updateMovieDataUserRating(String userRating)
    {
        movieData.getValue().userRating = userRating;
    }

    public void updateMovieDataNotes(String notes)
    {
        movieData.getValue().notes = notes;
    }

}
