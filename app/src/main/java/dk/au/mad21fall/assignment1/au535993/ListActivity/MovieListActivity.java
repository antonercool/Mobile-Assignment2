package dk.au.mad21fall.assignment1.au535993.ListActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.EditText;

import java.util.List;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.DetailsActivity.DetailsActivity;
import dk.au.mad21fall.assignment1.au535993.ListActivity.ViewModel.ListViewViewModel;
import dk.au.mad21fall.assignment1.au535993.Services.NotificationService;
import dk.au.mad21fall.assignment1.au535993.Utils.IntentConstants;
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
    private Button addButton;
    private EditText searchField;

    private ListViewViewModel vm;

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

        startService(new Intent(this, NotificationService.class));
        // only created once, until destroyed
        vm = new ViewModelProvider(this).get(ListViewViewModel.class);
        // Only load data from .csv once
        vm.initViewModel(this);
        vm.getMovieData().observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(List<MovieEntity> movieEntities) {
                movieAdaptor.updateMovieList(movieEntities);
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

        addButton = findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieTitle = searchField.getText().toString().trim();
                vm.addNewMovieIfExits(movieTitle, getApplicationContext());
            }
        });

        searchField = findViewById(R.id.editTextTitle);
        movieAdaptor = new MovieAdaptor(this);
        recyclerView = findViewById(R.id.movieRcvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdaptor);
    }


    @Override
    public void onMovieItemClicked(int index) {
        // open new details activity
        MovieEntity clickedObject = vm.getMovieData().getValue().get(index);
        clickedObject.position = String.valueOf(index);

        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(IntentConstants.DETAILS, String.valueOf(clickedObject.uid));

        Log.d(LogTag, "onMovieItemClicked");
        detailActivityLauncher.launch(detailsIntent);
    }

    private class ActivityResultMovieDetails implements ActivityResultCallback<ActivityResult> {

        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // update movieData in list
                //vm.dataUpdated();
            }
        }
    }
}