package by.supruniuk.alisa.movie.ui.popular_movie

import androidx.lifecycle.*
import androidx.paging.PagedList
import by.supruniuk.alisa.movie.model.Movie
import by.supruniuk.alisa.movie.repository.MovieRepository
import by.supruniuk.alisa.movie.repository.NetworkState
import kotlinx.coroutines.*

class MainViewModel(private val repository: MovieRepository) : ViewModel() {


    private var searchJob: Job? = null

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        repository.moviesPagedList
    }

    val movieNetworkState = MutableLiveData<NetworkState>()


  //  val searchTextLiveData = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

}

