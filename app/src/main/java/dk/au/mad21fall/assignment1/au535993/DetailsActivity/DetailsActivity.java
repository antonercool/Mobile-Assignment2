package dk.au.mad21fall.assignment1.au535993.DetailsActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.EditActivity.EditActivity;
import dk.au.mad21fall.assignment1.au535993.DetailsActivity.ViewModel.DetailsViewModel;
import dk.au.mad21fall.assignment1.au535993.Utils.IntentConstants;
import dk.au.mad21fall.assignment1.au535993.R;
import dk.au.mad21fall.assignment1.au535993.Utils.Utils;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    private static final String SAVED = "SAVED";

    TextView detailsName, detailsYear, detailsGenre, detailsPlot, detailsIBDMValue, detailsUserNotesValue,detailsUserRating;
    ImageView detailsIcon;

    Button detailsBackButton, detailsRatingButton;

    private DetailsViewModel vm;

    ActivityResultLauncher<Intent> editActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultMovieEdit());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideBlueBar(this);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int uid = Integer.parseInt(intent.getStringExtra(IntentConstants.DETAILS));

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        vm = new ViewModelProvider(this).get(DetailsViewModel.class);
        //  only if not created already
        vm.createMovieData(this, uid);

        // observe any changes to the MovieDataObject
        vm.getMovieData().observe(this, new Observer<MovieEntity>() {
            @Override
            public void onChanged(MovieEntity movieEntity) {
                updateUi();
            }
        });

        setUpUIElements();
    }

    private void updateUi(){
        detailsYear.setText(vm.getMovieData().getValue().getYear());
        detailsGenre.setText(vm.getMovieData().getValue().getGenre());
        detailsPlot.setText(vm.getMovieData().getValue().getPlot());
        detailsIBDMValue.setText(vm.getMovieData().getValue().getRating());
        detailsName.setText(vm.getMovieData().getValue().getName());
        detailsUserRating.setText(vm.getMovieData().getValue().getUserRating());
        detailsUserNotesValue.setText(vm.getMovieData().getValue().getNotes());

        detailsIcon.setImageResource(vm.getMovieData().getValue().mapGenreToId());
    }


    private void setUpUIElements() {
        detailsYear = findViewById(R.id.detailsYear);
        detailsGenre = findViewById(R.id.detailsGenre);
        detailsPlot = findViewById(R.id.detailsPlotText);
        detailsIcon = findViewById(R.id.detailsIcon);
        detailsIBDMValue = findViewById(R.id.detailsIMDBValue);
        detailsName = findViewById(R.id.detailsName);

        detailsUserRating = findViewById(R.id.detailsUserRatingValue);

        detailsUserNotesValue = findViewById(R.id.detailsNotesValue);

        detailsRatingButton = findViewById(R.id.detailsRatingBttn);

        detailsBackButton = findViewById(R.id.detailsBackBttn);
        detailsRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uid = vm.getMovieData().getValue().uid;

                Intent editIntent = new Intent(getApplicationContext(), EditActivity.class);
                editIntent.putExtra(IntentConstants.EDIT, String.valueOf(uid));

                Log.d(TAG, "Edit button clicked ");
                editActivityLauncher.launch(editIntent);
            }
        });

        detailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private class ActivityResultMovieEdit implements ActivityResultCallback<ActivityResult> {

        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                    // update the movieData with data from edit view
                    vm.movieDataUpdated();
            }
            else if(result.getResultCode() == Activity.RESULT_CANCELED){
                // do nothing since we dont wanna save the data
            }
        }
    }

}