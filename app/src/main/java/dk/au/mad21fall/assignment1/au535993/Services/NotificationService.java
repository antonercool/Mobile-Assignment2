package dk.au.mad21fall.assignment1.au535993.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.R;
import dk.au.mad21fall.assignment1.au535993.Repository.MovieRepository;


public class NotificationService extends Service {

    private static final int NOTIFICATION_ID = 238;
    private static final String CHANNEL = "movieService";
    private ExecutorService backgroundExecutor;
    private MovieRepository movieRepository;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        movieRepository = MovieRepository.getInstance(this);
        backgroundExecutor = Executors.newSingleThreadExecutor();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chan = new NotificationChannel(CHANNEL, "Foreground Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chan);
        }

        // Build the notification layout
        Notification notification = new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle("Movie Finder")
                .setContentText("Suggesting existing movies")
                .setSmallIcon(R.drawable.ic_horror)
                .build();

        // promote as foreground
        startForeground(NOTIFICATION_ID, notification);

        suggestMovies();
        // restart if gets killed.
        return START_NOT_STICKY;
    }

    private void suggestMovies() {
        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                doWork();
            }

            private void doWork() {
                try {
                    Thread.sleep(1000*60);
                    Random random = new Random();
                    int rNum = random.nextInt(movieRepository.getAll().size()-1);
                    MovieEntity movieEntity = movieRepository.getAll().get(rNum);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Suggestion: try the movie : "+ movieEntity.name +"!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    doWork();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("MovieService", "Error");
                }
            }
        });
    }


    // Dont need this, since we are not bound service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
