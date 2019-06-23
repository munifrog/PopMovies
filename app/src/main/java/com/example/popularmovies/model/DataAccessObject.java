package com.example.popularmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DataAccessObject {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long [] insertMovies(List<Movie> movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Delete
    void deleteMovies(List<Movie> movies);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> loadById(long id);
    @Query("SELECT * FROM movie WHERE id = :id")
    Movie loadByIdImmediately(long id);

    @Query("SELECT * FROM movie ORDER BY sort_index ASC")
    LiveData<List<Movie>> loadAll();
    @Query("SELECT * FROM movie ORDER BY sort_index ASC")
    List<Movie> loadAllImmediately();
}
