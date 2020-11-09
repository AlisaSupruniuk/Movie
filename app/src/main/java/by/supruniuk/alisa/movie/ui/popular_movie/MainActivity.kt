package by.supruniuk.alisa.movie.ui.popular_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import by.supruniuk.alisa.movie.R
import by.supruniuk.alisa.movie.repository.MovieRepository
import by.supruniuk.alisa.movie.repository.NetworkState
import by.supruniuk.alisa.movie.service.TMDBClient
import by.supruniuk.alisa.movie.service.TMDBInterface
import by.supruniuk.alisa.movie.repository.MoviePagedListAdapter
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private val apiService: TMDBInterface = TMDBClient.getClient()
    private lateinit var mainViewModel: MainViewModel
    private var moviePagedListAdapter = MoviePagedListAdapter(this)
    private val repository = MovieRepository(apiService)


//    private val searchTextWatcher = object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            // Start the search
//            repository.onSearchText(editable!!.toString())
//            if (editable.isNotEmpty()) {
//                mainViewModel.moviePagedList.value?.dataSource?.invalidate()
//            }
//        }
//
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//        }
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = getViewModel()

        //макет сетки и параметры - это количество промежутков
        val gridLayoutManager = GridLayoutManager(this, 2) //spanCount: 3 //было так


        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = moviePagedListAdapter


        mainViewModel.moviePagedList.observe(this, Observer {
            moviePagedListAdapter.submitList(it)
        })

        mainViewModel.movieNetworkState.observe(this, Observer {
            onMovieNetworkStateChanged(it)
        })

      //  initialiseUIElements()
    }


//    private fun initialiseUIElements() {
//
//        et_search.addTextChangedListener(searchTextWatcher)
//        moviePagedListAdapter = MoviePagedListAdapter(this)
//        rv_movie_list.apply {
//            adapter = moviePagedListAdapter
//            hasFixedSize()
//        }
//    }



    private fun onMovieNetworkStateChanged(state: NetworkState) {
        when (state) {
            NetworkState.LOADING -> {
                rv_movie_list.visibility = View.GONE
                progress_bar_popular.visibility = View.VISIBLE
            }
            NetworkState.LOADED -> {
                rv_movie_list.visibility = View.VISIBLE
                progress_bar_popular.visibility = View.GONE
            }
            NetworkState.ERROR -> {
                tv_error_popular.visibility = View.VISIBLE
                tv_error_popular.text = "Ошибка"
                rv_movie_list.visibility = View.GONE
                progress_bar_popular.visibility = View.GONE
            }
            NetworkState.ENDOFLIST -> {
                tv_error_popular.visibility = View.VISIBLE
                tv_error_popular.text = "Конец списка"
                rv_movie_list.visibility = View.GONE
                progress_bar_popular.visibility = View.GONE
            }
        }
    }

    private fun getViewModel(): MainViewModel {
        //Создает ViewModelProvider, который будет создавать ViewModels через данную Factory
        //и сохранять их в хранилище данного ViewModelStoreOwner - this.
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            //Возвращаем существующую ViewModel или создаем новую
            //modelClass - Класс модели ViewModel для создания ее экземпляра, если он отсутствует
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                //возвращаем ViewModel, кот. явл. экземпляром данного типа T.
                return MainViewModel(repository) as T //repository = MovieRepository(apiService = TMDBClient.getClient()
            }
        })[MainViewModel::class.java]  //ссылка на класс
    }
}


