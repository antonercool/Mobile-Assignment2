package dk.au.mad21fall.assignment1.au535993.Repository;

import android.app.Application;
import android.content.Context;
import android.graphics.Movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.net.ContentHandler;
import java.util.List;

import dk.au.mad21fall.assignment1.au535993.Database.MovieDatabase;
import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;

//TODO handle async operations
public class MovieRepository {

    private MovieDatabase db;

    public MovieRepository(Context context) {
        this.db = MovieDatabase.getInstance(context);
    }

    public LiveData<List<MovieEntity>> getAllAsLive(){
        return new MutableLiveData<>(db.movieDAO().getAll());
    }

    public List<MovieEntity> getAll(){
        return db.movieDAO().getAll();
    }

    public MovieEntity findMovieById(int id){
        return db.movieDAO().findMovie(id);
    }

    public void addMovie(MovieEntity movieEntity){
        db.movieDAO().addMovie(movieEntity);
    }

    public void updateMovie(MovieEntity movieEntity){
        db.movieDAO().updateMovie(movieEntity);
    }
    public void deleteMovie(MovieEntity movieEntity){
        db.movieDAO().deleteMovie(movieEntity);
    }

    public void deleteAll(){
        db.movieDAO().deleteAll();
    }

}
