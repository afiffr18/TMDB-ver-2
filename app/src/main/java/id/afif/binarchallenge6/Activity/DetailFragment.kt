package id.afif.binarchallenge6.Activity

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.Model.MovieDetail
import id.afif.binarchallenge6.Model.Status
import id.afif.binarchallenge6.R
import id.afif.binarchallenge6.databinding.FragmentDetailBinding
import id.afif.binarchallenge6.viewmodel.MoviesViewModel

class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!


    private val userRepo : UserRepo by lazy { UserRepo(requireContext()) }
    private val moviesViewModel : MoviesViewModel by viewModelsFactory { MoviesViewModel(userRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
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
        favoritesClicked()

    }

    private fun getDetailFromNetwork(id:Int){
        moviesViewModel.getMoviesDetail(id).observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                    binding.pbLoading.isVisible = true
                }

                Status.SUCCESS -> {
                    binding.pbLoading.isVisible = false
                    it.data?.let { it1 -> addData(it1) }
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
        if(movieDetail.tagline.isNotBlank()){
            binding.tvTagline.isVisible = true
        }
        val jam = movieDetail.runtime/60
        val menit = movieDetail.runtime%60
        binding.tvRuntime.text = "${jam}h ${menit}m"
        binding.tvTagline.text = movieDetail.tagline
        binding.tvOverview.text = movieDetail.overview

    }

    private fun favoritesClicked(){
        binding.btnFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_favoritesFragment)
        }
    }

}