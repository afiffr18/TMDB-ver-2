package id.afif.binarchallenge6.Activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import id.afif.binarchallenge6.Helper.DataStoreManager
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.Model.MovieDetail
import id.afif.binarchallenge6.Model.Status
import id.afif.binarchallenge6.database.Favorite
import id.afif.binarchallenge6.databinding.FragmentDetailBinding
import id.afif.binarchallenge6.viewmodel.MoviesViewModel
import id.afif.binarchallenge6.viewmodel.UserViewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!


    private val userRepo: UserRepo by lazy { UserRepo(requireContext()) }
    private val moviesViewModel: MoviesViewModel by viewModelsFactory { MoviesViewModel(userRepo) }

    private val dataStoreManager: DataStoreManager by lazy { DataStoreManager(requireContext()) }
    private val userViewModel: UserViewModel by lazy { UserViewModel(dataStoreManager) }

    private lateinit var fabColor: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        getDetailFromNetwork(id!!)
    }

    private fun getDetailFromNetwork(id:Int){
        moviesViewModel.getMoviesDetail(id).observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                    binding.pbLoading.isVisible = true
                }

                Status.SUCCESS -> {
                    binding.pbLoading.isVisible = false
                    it.data?.let { it1 ->
                        addData(it1)
                    }

                }

                Status.ERROR -> {
                    binding.pbLoading.isVisible = false
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                }

            }

        }

    }



    private fun addData(movieDetail: MovieDetail){
        var text1 = ""

        Glide.with(requireContext()).load("https://www.themoviedb.org/t/p/w220_and_h330_face/${movieDetail.posterPath}")
            .into(binding.ivMoviesDetail)
        binding.tvMovieDetailTitle.text = movieDetail.title
        binding.tvReleaseDate.text = "(${ movieDetail.releaseDate.take(4) })"
        binding.tvRelease.text = movieDetail.releaseDate
        for(i in 0 until movieDetail.genres.size){
            text1  = TextUtils.concat(text1, "${movieDetail.genres[i].name}, ").toString()
        }

        binding.tvGenre.text = text1

        binding.progressDetail.progress = (movieDetail.voteAverage*10).toInt()
        binding.tvPercentageDetail.text = "${movieDetail.voteAverage*10}%"
        if (movieDetail.tagline.isNotBlank()) {
            binding.tvTagline.isVisible = true
        }
        val jam = movieDetail.runtime / 60
        val menit = movieDetail.runtime % 60
        binding.tvRuntime.text = "${jam}h ${menit}m"
        binding.tvTagline.text = movieDetail.tagline
        binding.tvOverview.text = movieDetail.overview

        binding.btnFavorites.setOnClickListener {
            userViewModel.getLoginId().observe(viewLifecycleOwner) {
                val favorite = Favorite(
                    movieDetail.id,
                    it,
                    movieDetail.title,
                    movieDetail.releaseDate,
                    movieDetail.posterPath,
                    movieDetail.voteAverage
                )
                moviesViewModel.insertFavorite(favorite)

            }
            fabColor = "#EA2D42"
            binding.btnFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor(fabColor))
            binding.btnFavorites.supportImageTintList =
                ColorStateList.valueOf(Color.parseColor(fabColor))
        }

    }

}