package dk.au.mad21fall.assignment1.au535993.ListActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.assignment1.au535993.Database.MovieEntity;
import dk.au.mad21fall.assignment1.au535993.ListActivity.MovieItemClickedListener.IMovieItemClickedListener;
import dk.au.mad21fall.assignment1.au535993.R;

public class MovieAdaptor extends RecyclerView.Adapter<MovieAdaptor.MovieViewHolder> {

    // data for the adaptor
    private List<MovieEntity> movieDataList;

    // callback interface for user action on each movie item
    private IMovieItemClickedListener movieItemClickedListener;

    public MovieAdaptor(IMovieItemClickedListener movieItemClickedListener)
    {
        this.movieItemClickedListener = movieItemClickedListener;
    }

    public void updateMovieList(List<MovieEntity> newMovieData)
    {
        movieDataList = newMovieData;
        notifyDataSetChanged();
    }

    // Overriding this methos to create the movieviewholder object for the first time. (once)
    // Use the inflater from parrent activity viewGroup to get the view.
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(v, movieItemClickedListener);
        return viewHolder;
    }

    // When ever the user scrolls, it needs to request new data
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.movieName.setText(movieDataList.get(position).getName());
        holder.movieIcon.setImageResource(movieDataList.get(position).mapGenreToId());
        holder.movieRating.setText(movieDataList.get(position).getUserRating());
        holder.movieYear.setText(movieDataList.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        if (movieDataList == null)
            return 0;
        else
            return movieDataList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // View contents MovieItem
         ImageView movieIcon;
         TextView movieName;
         TextView movieRating;
         TextView movieYear;

        private IMovieItemClickedListener movieItemClickedListener;

        public MovieViewHolder(@NonNull View itemView, IMovieItemClickedListener listener) {
            super(itemView);

            this.movieIcon = itemView.findViewById(R.id.moiveIcon);
            this.movieName = itemView.findViewById(R.id.movieName);
            this.movieRating = itemView.findViewById(R.id.movieRating);
            this.movieYear = itemView.findViewById(R.id.movieYear);

            this.movieItemClickedListener = listener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            movieItemClickedListener.onMovieItemClicked(getAdapterPosition());
        }
    }
}
