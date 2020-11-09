package by.supruniuk.alisa.movie.repository

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.supruniuk.alisa.movie.R
import by.supruniuk.alisa.movie.model.Movie
import by.supruniuk.alisa.movie.service.POSTER_BASE_URL
import by.supruniuk.alisa.movie.ui.single_movie.SingleMovieActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviePagedListAdapter(private val context: Context): PagedListAdapter<Movie, RecyclerView.ViewHolder>(
    MovieDiffCallback()
) {


    //создание держателя представления
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        view = layoutInflater.inflate(R.layout.movie_item, parent, false)
        return MovieItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { //
        (holder as MovieItemViewHolder).bind(getItem(position), context)
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) :RecyclerView.ViewHolder(view){

        fun bind(movie: Movie?, context: Context){

            itemView.cv_tv_movie_title.text = movie?.title
            itemView.cv_tv_release_date.text = movie?.releaseDate

            val moviePosterUrl:String = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .into(itemView.cv_iv_movie_poster)

            //здесь же реализовать переход на просмотр деталей фильма по нажатию на картинку
            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovieActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }
}