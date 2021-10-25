package dk.au.mad21fall.assignment1.au535993.Repository;

import android.app.Application;
import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.google.common.util.concurrent.ListenableFuture;

import java.net.ContentHandler;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21fall.assignment1.au535993.Database.MovieDatabase;
import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;

public class MovieRepository implements  SuccesMovieExecutor{

    private MovieDatabase db;
    private ExecutorService asyncExecutor;
    private MovieDataRequester movieDataRequester;
    private static MovieRepository movieRepository;

    // Singleton pattern
    public static MovieRepository getInstance(Context context) {
        if (movieRepository == null) {
            movieRepository = new MovieRepository(context);
        }
        return movieRepository;
    }

    private MovieRepository(Context context){
        this.db = MovieDatabase.getInstance(context);
        this.asyncExecutor = Executors.newSingleThreadExecutor();
        this.movieDataRequester = new MovieDataRequester(context);
    }

    public LiveData<List<MovieEntity>> getAllAsLive(){
        return db.movieDAO().getAllLive();
    }

    public List<MovieEntity> getAll(){
        return db.movieDAO().getAll();
    }

    public LiveData<MovieEntity> findMovieByIdAsLive(int id){
        return db.movieDAO().findMovieLive(id);
    }

    public ListenableFuture<MovieEntity> findMovieByNameAsync(String name){
        return db.movieDAO().findMovie(name);
    }

    // One-Shot aync write
    public void addMovieAsync(MovieEntity movieEntity){
       asyncExecutor.execute(new Runnable() {
           @Override
           public void run() {
               db.movieDAO().addMovie(movieEntity);
           }
       });
    }

    // One-Shot aync update
    public void updateMovie(MovieEntity movieEntity){
        asyncExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().updateMovie(movieEntity);
            }
        });
    }

    // One-Shot aync delete
    public void deleteMovieAsync(MovieEntity movieEntity){
        asyncExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().deleteMovie(movieEntity);
            }
        });
    }

    // One-Shot delete
    public void deleteAllAsync(){
        asyncExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().deleteAll();
            }
        });
    }

    public void searchMovie(String movieName, Context context){
        this.movieDataRequester.requestMovieData(movieName, context, this);
    }


    @Override
    public void ExecuteSucces(MovieEntity entity, Context context) {
        ListenableFuture<MovieEntity> future = this.db.movieDAO().findMovie(entity.getName());
        future.addListener(new Runnable() {
            @Override
            public void run() {
                try{
                    if(future.get() == null){
                        addMovieAsync(entity);
                    }
                }catch (Exception e)
                {
                    Log.d("REPO", "does not exits");
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }

}
