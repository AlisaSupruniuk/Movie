package by.supruniuk.alisa.movie.repository

import androidx.lifecycle.MutableLiveData
import by.supruniuk.alisa.movie.model.Movie
import by.supruniuk.alisa.movie.service.TMDBInterface
import androidx.paging.DataSource

class MovieDataSourceFactory(private val apiService: TMDBInterface)
    : DataSource.Factory<Int, Movie>(){

    private val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {

        val movieDataSource = MovieDataSource(apiService)

        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}