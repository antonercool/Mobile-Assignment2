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

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.DetailsActivity.ViewModel.DetailsViewModel;
import dk.au.mad21fall.assignment1.au535993.EditActivity.ViewModel.EditViewModel;
import dk.au.mad21fall.assignment1.au535993.R;
import dk.au.mad21fall.assignment1.au535993.Utils.IntentConstants;
import dk.au.mad21fall.assignment1.au535993.Utils.Utils;

public class EditActivity extends AppCompatActivity {

    TextView movieTitle, myRatingValue, myNotesValue;
    ImageView movieIcon;
    SeekBar ratingSeekBar;
    Button okButton, cancelButton;

    DetailsViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideBlueBar(this);
        setContentView(R.layout.activity_edit);

        setUpUiElements();
        // load data from intent
        int uid = Integer.parseInt(getIntent().getStringExtra(IntentConstants.EDIT));

        // only created once, until destroyed
        vm = new ViewModelProvider(this).get(EditViewModel.class);
        vm.createMovieData(this, uid);

        vm.getMovieData().observe(this, new Observer<MovieEntity>() {
            @Override
            public void onChanged(MovieEntity movieEntity) {
                updateUi();
            }
        });
    }


    private void updateUi(){
        movieIcon.setImageResource(vm.getMovieData().getValue().mapGenreToId());
        movieTitle.setText(vm.getMovieData().getValue().getName());
        myRatingValue.setText(vm.getMovieData().getValue().getUserRating());
        myNotesValue.setText(vm.getMovieData().getValue().getNotes());
        if (!vm.getMovieData().getValue().getUserRating().equals("X.X")) {
            ratingSeekBar.setProgress((int) (Double.parseDouble(vm.getMovieData().getValue().getUserRating()) * 10));
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
                vm.getMovieData().getValue().setUserRating(String.valueOf(value/10));
                myRatingValue.setText(vm.getUserRating());
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
                setResult(RESULT_OK, new Intent());
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
        vm.persist();
    }
}