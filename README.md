## Introduction

Android application that uses a TMDb API key to retrieve specific information about movie titles
that fit specific search criteria such as (1) most popular and (2) highest rated. Search results
are stored in the application database on the device for offline use, and movies marked as
favorites will stay there until unmarked (and the user leaves the movie details page). Details for
each movie consist of posters, foreign title (if different), reviews, trailers, popularity ratings,
and favorite status. Movies can be sorted according to the following
criteria
 1. Most popular
 1. Highest rated
 1. Favorites

# Overview

This app uses the internet to retrieve a JSON file containing information regarding the top twenty
movies matching the specified search criteria (initially most popular or highest rating; which could
change in the future). The list of twenty movies is used to populate a grid view showing the poster
for each movie. When a specific movie poster is clicked, the movie details are shown, including
title, overview, release date, rating, named trailers, movie reviews, and favorite status. Clicking
on a review or trailer will launch using another app.

## Getting started / Usage

The files for this project were built using Android Studio, so you will likely have the easiest
time duplicating the original behavior using the same.

 1. Clone this repository
    * git clone https://github.com/munifrog/PopMovies.git
 1. Open this directory using Android Studio
 1. Obtain an API key for TMDb usage
    * Mine was free as a student for this app which I will likely not get revenue from
 1. Open MovieConst.java for editing
 1. Set the value of `TMDB_API_KEY` as your API key
    * `String TMDB_API_KEY = "a1b2c3d4e5f6a7b8c9d1e2f3a4b5c6d7";`
    * Note this example 32-character hexadecimal string did not work at the time of writing
 1. Launch the app on an emulator or device that has internet access

# License
This project started as a task within the [Udacity Android Developer Nanodegree Course](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801)
and Udacity (or Google) owns the rights for the project idea.

While I personally prefer indirect sources for finding solutions to puzzles, you may learn from
what I have done, if that helps. Just be careful to avoid plagiarism if you are also taking this
course and looking for answers.

If, on the other hand, you want to build on what I have done, that is also welcome.
