package dk.au.mad21fall.assignment1.au535993.Database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// TODO make operations async
@Dao
public interface IMovieDAO {


    @Query("SELECT * FROM movies")
    List<MovieEntity> getAll();

    @Query("SELECT * FROM movies WHERE uid LIKE :uid")
    MovieEntity findMovie(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(MovieEntity movie);

    @Delete
    void deleteMovie(MovieEntity movie);

    @Update
    void updateMovie(MovieEntity movie);

    @Query("DELETE FROM movies")
    void deleteAll();

}
