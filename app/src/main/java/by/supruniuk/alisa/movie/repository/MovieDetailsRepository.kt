package by.supruniuk.alisa.movie.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.supruniuk.alisa.movie.model.MovieDetails
import by.supruniuk.alisa.movie.service.TMDBInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsRepository(private val apiService : TMDBInterface) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _movieDetails = MutableLiveData<MovieDetails>()

    fun fetchMovieDetails(id : Int) : LiveData<MovieDetails> {
        val movieNetworkState = MutableLiveData<NetworkState>()

        movieNetworkState.postValue(NetworkState.LOADING)

        scope.launch(Dispatchers.IO) {
            val movieDetailsRequest = apiService.getMovieDetails(id)
            try {
                val response =movieDetailsRequest.await()
                val movieResponse = response.body()

                _movieDetails.postValue(movieResponse)

                movieNetworkState.postValue(NetworkState.LOADED)
            } catch (e: java.lang.Exception){
                movieNetworkState.postValue(NetworkState.ERROR)
            }
        }
        return _movieDetails
    }
}