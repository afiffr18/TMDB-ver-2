package id.afif.binarchallenge6.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.afif.binarchallenge6.Adapter.MoviesAdapter
import id.afif.binarchallenge6.Helper.DataStoreManager
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.Model.Status
import id.afif.binarchallenge6.R
import id.afif.binarchallenge6.databinding.FragmentHomeBinding
import id.afif.binarchallenge6.viewmodel.MoviesViewModel
import id.afif.binarchallenge6.viewmodel.UserViewModel

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var moviesAdapter : MoviesAdapter


    private val userRepo : UserRepo by lazy { UserRepo(requireContext()) }
    private val viewModel : MoviesViewModel by viewModelsFactory { MoviesViewModel(userRepo) }

    private val dataStoreManager : DataStoreManager by lazy { DataStoreManager(requireContext()) }
    private val userViewModel : UserViewModel by viewModelsFactory { UserViewModel(dataStoreManager) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        getDataFromNetwork()
        setUsernameLogin()
        profileClicked()
        favoritesClicked()
    }

    private fun initRecycler(){
        moviesAdapter = MoviesAdapter{ id: Int ->
            val bundle = Bundle()
            bundle.putInt("id",id)
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,bundle)
        }
        binding.apply {
            rvMovies.adapter = moviesAdapter
            rvMovies.layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    private fun getDataFromNetwork(){
        viewModel.getAllMovies().observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING->{
                    binding.pbLoading.isVisible = true
                }
                Status.SUCCESS->{
                    binding.pbLoading.isVisible = false
                   it.data?.let { data ->
                       moviesAdapter.updateData(data.results)
                   }
                }
                Status.ERROR -> {
                    binding.pbLoading.isVisible = false
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    private fun setUsernameLogin() {
        userViewModel.getUserLogin().observe(viewLifecycleOwner) {
            viewModel.getDataById(it[0], it[1])
        }
        viewModel.dataUser.observe(viewLifecycleOwner) {
            binding.tvUserLogin.text = "welcome : ${it.username}"
            it.id?.let { it1 ->
                userViewModel.saveId(it1)
            }
        }


    }

    private fun profileClicked(){
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun favoritesClicked(){
        binding.btnFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
        }
    }


}