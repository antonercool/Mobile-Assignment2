package dk.au.mad21fall.assignment1.au535993.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

import dk.au.mad21fall.assignment1.au535993.R;

@Entity(tableName = "movies")
public class MovieEntity {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    private String name;
    private String genre;
    private String plot;
    private String year;
    private String rating;
    private String notes;
    private String userRating;
    public String position;

    @Ignore
    Map<String, Integer> genreMapping;

    public MovieEntity(String name, String genre, String plot, String year, String rating, String notes, String userRating) {
        this.name = name;
        this.genre = genre;
        this.plot = plot;
        this.year = year;
        this.rating = rating;
        this.notes = notes;
        this.userRating = userRating;

        loadMappingScheme();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public int getUid() {
        return uid;
    }

    private void loadMappingScheme()
    {
        genreMapping = new HashMap<String, Integer>();
        genreMapping.put("Action", R.drawable.ic_action);
        genreMapping.put("Comedy", R.drawable.ic_comedy);
        genreMapping.put("Drama", R.drawable.ic_drama);
        genreMapping.put("Horror", R.drawable.ic_horror);
        genreMapping.put("Romance", R.drawable.ic_romance);
        genreMapping.put("Western", R.drawable.ic_western);
    }

    public int mapGenreToId(){
        return genreMapping.get(this.genre);
    }
}
