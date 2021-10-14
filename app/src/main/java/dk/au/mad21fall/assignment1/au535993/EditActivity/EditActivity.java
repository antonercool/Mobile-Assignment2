package dk.au.mad21fall.assignment1.au535993.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader.MovieDataLoader;
import dk.au.mad21fall.assignment1.au535993.ListActivity.Models.SingleMovieDataViewModel;
import dk.au.mad21fall.assignment1.au535993.ListActivity.DataLoader.MovieDataJsonWriter;
import dk.au.mad21fall.assignment1.au535993.ListActivity.Models.MovieData;
import dk.au.mad21fall.assignment1.au535993.R;
import dk.au.mad21fall.assignment1.au535993.Utils.IntentConstants;
import dk.au.mad21fall.assignment1.au535993.Utils.Utils;

public class EditActivity extends AppCompatActivity {

    TextView movieTitle, myRatingValue, myNotesValue;
    ImageView movieIcon;
    SeekBar ratingSeekBar;
    Button okButton, cancelButton;

    SingleMovieDataViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideBlueBar(this);
        setContentView(R.layout.activity_edit);

        setUpUiElements();
        // load data from intent
        MovieData movieData = MovieDataLoader.loadDataFromIntent(getIntent(), IntentConstants.EDIT);

        // only created once, until destroyed
        vm = new ViewModelProvider(this).get(SingleMovieDataViewModel.class);
        vm.createMovieData(movieData);

        vm.getMovieData().observe(this, new Observer<MovieData>() {
            @Override
            public void onChanged(MovieData movieData) {
                updateUi();
            }
        });
    }


    private void updateUi(){
        movieIcon.setImageResource(vm.getMovieData().getValue().mapGenreToId());
        movieTitle.setText(vm.getMovieData().getValue().name);
        myRatingValue.setText(vm.getMovieData().getValue().userRating);
        myNotesValue.setText(vm.getMovieData().getValue().notes);
        if (!vm.getMovieData().getValue().userRating.equals("X.X")) {
            ratingSeekBar.setProgress((int) (Double.parseDouble(vm.getMovieData().getValue().userRating) * 10));
        }
    }

    private void setUpUiElements() {
        movieTitle = findViewById(R.id.editMovieTitle);
        myRatingValue = findViewById(R.id.editMyRatingValue);
        myNotesValue = findViewById(R.id.editNotesValue);
        movieIcon = findViewById(R.id.editMovieIcon);

        ratingSeekBar = findViewById(R.id.editSeekBar);
        ratingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                double value = (double)i;
                vm.getMovieData().getValue().userRating = String.valueOf(value/10);
                myRatingValue.setText(vm.getMovieData().getValue().userRating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        okButton = findViewById(R.id.editOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent resultIntent = new Intent();
                JSONObject movieDataInJson = MovieDataJsonWriter.convertMovieDataToJson(vm.getMovieData().getValue());
                resultIntent.putExtra(IntentConstants.EDIT, movieDataInJson.toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        cancelButton = findViewById(R.id.editCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });
    }

    private void saveData(){
        vm.updateMovieDataNotes(myNotesValue.getText().toString());
        vm.updateMovieDataUserRating(myRatingValue.getText().toString());
    }
}