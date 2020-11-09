package by.supruniuk.alisa.movie.ui.single_movie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import by.supruniuk.alisa.movie.R
import by.supruniuk.alisa.movie.model.MovieDetails
import by.supruniuk.alisa.movie.repository.MovieDetailsRepository
import by.supruniuk.alisa.movie.service.POSTER_BASE_URL
import by.supruniuk.alisa.movie.service.TMDBClient
import by.supruniuk.alisa.movie.service.TMDBInterface
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovieActivity : AppCompatActivity() {

    private  lateinit var viewModel: SingleMovieViewModel
    private  lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: TMDBInterface = TMDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
         })


    }

    fun bindUI(it: MovieDetails){
        tv_movie_title.text = it.title
        tv_movie_tagline.text = it.tagline
        tv_movie_release_date.text = it.releaseDate
        tv_movie_rating.text = it.rating.toString()
        tv_movie_runtime.text = it.runtime.toString() + " мин."
        tv_movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US) //формат валюты для бюджета и сборов
        tv_movie_budget.text = formatCurrency.format(it.budget)
        tv_movie_revenue.text = formatCurrency.format(it.revenue)

        //объединяем пути POSTER_BASE_URL с базовым url для получения полного url адреса
        val moviePosterURL: String = POSTER_BASE_URL + it.posterPath
        // библиотека Glide предназначена для асинхронной подгрузки изображений из сети, ресурсов или файловой системы, их кэширования и отображения
        Glide.with(this)
            .load(moviePosterURL) //откуда
            .into(iv_movie_poster); //куда
    }

    private  fun getViewModel(movieId: Int): SingleMovieViewModel {

        //возвращаем ViewModel
        return  ViewModelProviders.of(this, object  : ViewModelProvider.Factory { //provider - поставщик
            //переопределяем create с универсальным типом
            //в которой возвращаем наш SingleMovieViewModel
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress ("UNCHECKED_CAST") //предепреждение (незарегистрированный каст)
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java] //ссылка на класс
    }
}