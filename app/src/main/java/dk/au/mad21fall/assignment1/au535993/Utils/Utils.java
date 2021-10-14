package dk.au.mad21fall.assignment1.au535993.Utils;

import android.content.Context;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {
    public static void hideBlueBar(AppCompatActivity app){
        app.requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        app.getSupportActionBar().hide(); // hide the title bar
    }
}
