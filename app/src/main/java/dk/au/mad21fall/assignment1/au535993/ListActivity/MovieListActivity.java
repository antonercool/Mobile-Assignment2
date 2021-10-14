package dk.au.mad21fall.assignment1.au535993.ListActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import dk.au.mad21fall.assignment1.au535993.DetailsActivity.DetailsActivity;
import dk.au.mad21fall.assignment1.au535993.ListActivity.Models.MultipleMovieDataViewModel;
import dk.au.mad21fall.assignment1.au535993.ListActivity.Models.SingleMovieDataViewModel;
import dk.au.mad21fall.assignment1.au535993.Utils.IntentConstants;
import dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader.MovieDataJsonWriter;
import dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader.MovieDataLoader;
import dk.au.mad21fall.assignment1.au535993.ListActivity.Models.MovieData;
import dk.au.mad21fall.assignment1.au535993.ListActivity.MovieItemClickedListener.IMovieItemClickedListener;
import dk.au.mad21fall.assignment1.au535993.R;
import dk.au.mad21fall.assignment1.au535993.Utils.Utils;

public class MovieListActivity extends AppCompatActivity  implements IMovieItemClickedListener {

    private static final String LogTag = "LISTVIEW";
    // listview
    private RecyclerView recyclerView;
    // adaptor
    private MovieAdaptor movieAdaptor;
    private Button exitButton;

    private MovieDataLoader movieDataLoader;
    private MultipleMovieDataViewModel vm;

    // Details launcher
    ActivityResultLauncher<Intent> detailActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultMovieDetails());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideBlueBar(this);
        setContentView(R.layout.activity_list);

        setUpUiElements();


        // only created once, until destroyed
        vm = new ViewModelProvider(this).get(MultipleMovieDataViewModel.class);
        // Only load data from .csv once
        vm.createMovieData(this);
        vm.getMovieData().observe(this, new Observer<ArrayList<MovieData>>() {
            @Override
            public void onChanged(ArrayList<MovieData> movieDataArrayList) {
                movieAdaptor.updateMovieList(vm.getMovieData().getValue());
            }
        });
    }

    private void setUpUiElements() {
        exitButton = findViewById(R.id.buttonExit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        movieAdaptor = new MovieAdaptor(this);
        recyclerView = findViewById(R.id.movieRcvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdaptor);
    }


    @Override
    public void onMovieItemClicked(int index) {
        // open new details activity
        MovieData clickedObject = vm.getMovieData().getValue().get(index);
        clickedObject.position = String.valueOf(index);
        JSONObject jsonObject = MovieDataJsonWriter.convertMovieDataToJson(clickedObject);

        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(IntentConstants.DETAILS, jsonObject.toString());

        Log.d(LogTag, "onMovieItemClicked ");
        detailActivityLauncher.launch(detailsIntent);
    }

    private class ActivityResultMovieDetails implements ActivityResultCallback<ActivityResult> {

        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // Here, no request code
                Intent data = result.getData();
                try {
                    JSONObject movieDataInJson = new JSONObject(data.getStringExtra(IntentConstants.DETAILS));
                    MovieData newMovieData = MovieDataJsonWriter.convertJsonToMovieData(movieDataInJson);
                    // update movieData in list
                    vm.updateMovieDataElement(newMovieData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}