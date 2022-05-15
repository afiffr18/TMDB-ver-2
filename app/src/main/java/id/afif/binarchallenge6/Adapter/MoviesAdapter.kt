package id.afif.binarchallenge6.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.progressindicator.CircularProgressIndicator
import id.afif.binarchallenge6.Model.Result
import id.afif.binarchallenge6.R

class MoviesAdapter(private val onClickListener : (id: Int) -> Unit) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val difCallback = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,difCallback)

    fun updateData(results : List<Result>) = differ.submitList(results)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_movie,parent,false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MoviesViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val ivPoster = view.findViewById<ImageView>(R.id.iv_movies)
        val tvTitle = view.findViewById<TextView>(R.id.tv_movie_title)
        val tvDate = view.findViewById<TextView>(R.id.tv_movie_date)
        val rating = view.findViewById<CircularProgressIndicator>(R.id.progress_circular)
        val persen = view.findViewById<TextView>(R.id.tv_progress_percentage)
        val constLayout = view.findViewById<ConstraintLayout>(R.id.constraint1)

        val bgOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
        fun bind(result: Result){
            tvTitle.text = result.title
            tvDate.text = result.releaseDate
            Glide.with(itemView.context).load("https://www.themoviedb.org/t/p/w220_and_h330_face/${result.posterPath}")
                .apply(bgOptions).into(ivPoster)
            rating.progress = (result.voteAverage*10).toInt()
            persen.text = (result.voteAverage * 10).toInt().toString().plus("%")

            constLayout.setOnClickListener {
                onClickListener.invoke(result.id)
            }
        }
    }
}