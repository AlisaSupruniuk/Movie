package by.supruniuk.alisa.movie.ui.single_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import by.supruniuk.alisa.movie.model.Movie
import by.supruniuk.alisa.movie.model.MovieDetails
import by.supruniuk.alisa.movie.repository.MovieDetailsRepository
import by.supruniuk.alisa.movie.repository.MovieRepository
import by.supruniuk.alisa.movie.repository.NetworkState
import kotlinx.coroutines.*
import java.lang.Exception

class SingleMovieViewModel (private  val repository: MovieDetailsRepository, movieId: Int ) : ViewModel() {



    val movieDetails : LiveData<MovieDetails> by lazy {
        repository.fetchMovieDetails(movieId)
    }




//    val networkState : LiveData<NetworkState> by lazy {
//        movieRepository.getMovieDetailsNetworkState()
//    }

    //вызывается когда активити или фрагмент уничтожен
    //добавляем сюда compositeDisposable, чтобы небыло никаких утечек памяти
    override fun onCleared() {
        super.onCleared()
        //compositeDisposable.dispose()
        //scope.cancel()
    }
}