package by.supruniuk.alisa.movie.repository


import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import by.supruniuk.alisa.movie.model.Movie
import by.supruniuk.alisa.movie.service.POST_PER_PAGE
import by.supruniuk.alisa.movie.service.TMDBInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception


class MovieRepository(private val apiService : TMDBInterface) {

//    private val searchText = MutableLiveData<String>()
 //   private val searchJob: Job? = null
    private val moviesDataSourceFactory = MovieDataSourceFactory(apiService)

    val moviesPagedList : LiveData<PagedList<Movie>> by lazy {

        val config : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE) //задаем размер страницы
            .build()

        LivePagedListBuilder(moviesDataSourceFactory, config).build()
    }

//    fun onSearchText(query: String){
//        searchJob?.cancel()
//        if (query.length > 2){
//            searchText.value = query
//        }
//    }

}