package by.supruniuk.alisa.movie.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import by.supruniuk.alisa.movie.model.Movie
import by.supruniuk.alisa.movie.service.FIRST_PAGE
import by.supruniuk.alisa.movie.service.TMDBInterface
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieDataSource(private val apiService: TMDBInterface)
    : PageKeyedDataSource<Int, Movie>(), CoroutineScope{

    private val page = FIRST_PAGE
    private var searchJob: Job? = null
    override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val movieNetworkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        movieNetworkState.postValue(NetworkState.LOADING)

//        searchJob?.cancel()
//        searchJob = async {
//            if (search.toString().isNotEmpty()) {
//                searchJob = launch(Dispatchers.IO) {
//                    try {
//                        val response =
//                            apiService.getMovieByQuery(search.toString(), page)
//                                .movieList
//                        callback.onResult(response, null, page + 1)
//                        movieNetworkState.postValue(NetworkState.LOADED)
//                    } catch (e: Exception) {
//                        movieNetworkState.postValue(NetworkState.ERROR)
//                        Log.e("MovieDataSource", e.message)
//                    }
//                }
//            } else {
                searchJob = launch(Dispatchers.IO) {
                    try {

                        val response = apiService.getPopularMovies(page)
                        val popularMovies = response.movieList
                        callback.onResult(popularMovies, null, page + 1)

                        movieNetworkState.postValue(NetworkState.LOADED)
                    } catch (e: Exception) {
                        movieNetworkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", e.message)
                    }
                }
         //   }
       // }
    }



    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

        movieNetworkState.postValue(NetworkState.LOADING)

//        searchJob?.cancel()
//        searchJob = async {
//            if (search.toString().isNotEmpty()) {
//                searchJob = launch(Dispatchers.IO) {
//                    try {
//                        val response = apiService.getMovieByQuery(search.toString(), params.key)
//                        //если общая страница больше или равна, значит необходимо загрузить больше страниц
//                        // загружаем следующую страницу и устанавливаем состояние сети "загружено"
//                        if (response!!.totalPages >= params.key) {
//                            callback.onResult(response.movieList, params.key + 1)
//                            movieNetworkState.postValue(NetworkState.LOADED)
//                        } else {
//                            //если достигли конца списка(страниц), устанавливаем состояние сети "достигли конца"
//                            movieNetworkState.postValue(NetworkState.ENDOFLIST)
//                        }
//                    } catch (e: Exception) {
//                        movieNetworkState.postValue(NetworkState.ERROR)
//                    }
//                }
//            } else {
                searchJob = launch {
                    try {
                        val response = apiService.getPopularMovies(params.key)
                        if (response!!.totalPages >= params.key) {
                            callback.onResult(response.movieList, params.key + 1)
                            movieNetworkState.postValue(NetworkState.LOADED)
                        } else {
                            movieNetworkState.postValue(NetworkState.ENDOFLIST)
                        }
                    } catch (e: Exception) {
                        movieNetworkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", e.message)
                    }
                }
        //    }
      //  }
    }
}