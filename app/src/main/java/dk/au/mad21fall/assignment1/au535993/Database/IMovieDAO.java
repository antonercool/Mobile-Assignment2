package dk.au.mad21fall.assignment1.au535993.Database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

// TODO make operations async
@Dao
public interface IMovieDAO {


    @Query("SELECT * FROM movies")
    LiveData<List<MovieEntity>> getAllLive();

    @Query("SELECT * FROM movies")
    List<MovieEntity> getAll();

    @Query("SELECT * FROM movies WHERE uid LIKE :uid")
    LiveData<MovieEntity> findMovieLive(int uid);

    @Query("SELECT * FROM movies WHERE name LIKE :name")
    ListenableFuture<MovieEntity> findMovie(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(MovieEntity movie);

    @Delete
    void deleteMovie(MovieEntity movie);

    @Update
    void updateMovie(MovieEntity movie);

    @Query("DELETE FROM movies")
    void deleteAll();

}
