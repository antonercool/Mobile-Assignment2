package dk.au.mad21fall.assignment1.au535993.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MovieEntity.class}, version=9, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase{

    // mandatory Data acces object
    public abstract IMovieDAO movieDAO();
    // singleton
    private static MovieDatabase instance;

    // TODO remove remove AllowMainThread
    public static MovieDatabase getInstance(final Context context){
        if (instance == null){
            synchronized (MovieDatabase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class,
                            "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return instance;
    }
}
