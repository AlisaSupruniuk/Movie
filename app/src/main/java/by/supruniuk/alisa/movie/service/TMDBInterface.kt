package by.supruniuk.alisa.movie.service

import by.supruniuk.alisa.movie.model.MovieDetails
import by.supruniuk.alisa.movie.model.MovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.themoviedb.org/3/movie/528085?api_key=ad16c8e0e0197a82b75db19a22aa0578
//https://api.themoviedb.org/3/movie/popular?api_key=ad16c8e0e0197a82b75db19a22aa0578&page=1
//https://api.themoviedb.org/3/

interface TMDBInterface {
    @GET("movie/popular") //get- получить //page - передаем номер страницы в параметре(страница в ссылке)
    suspend fun getPopularMovies(@Query("page") page: Int) : MovieResponse //Single<MovieResponse>

    @GET("movie/{movie_id}")  // команда на сервере
    fun getMovieDetails(@Path("movie_id") id:Int): Deferred<Response<MovieDetails>> //Single<MovieDetails>

    @GET("search/movie")
    suspend fun  getMovieByQuery(@Query("query") query: String, @Query("page") page: Int) : MovieResponse


}
