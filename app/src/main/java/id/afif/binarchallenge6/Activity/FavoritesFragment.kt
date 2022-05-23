package id.afif.binarchallenge6.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.afif.binarchallenge6.Adapter.FavoriteAdapter
import id.afif.binarchallenge6.Helper.DataStoreManager
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.R
import id.afif.binarchallenge6.databinding.FragmentFavoritesBinding
import id.afif.binarchallenge6.viewmodel.MoviesViewModel
import id.afif.binarchallenge6.viewmodel.UserViewModel


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoritesAdapter: FavoriteAdapter

    private val userRepo: UserRepo by lazy { UserRepo(requireContext()) }
    private val viewModel: MoviesViewModel by viewModelsFactory { MoviesViewModel(userRepo) }

    private val dataStoreManager: DataStoreManager by lazy { DataStoreManager(requireContext()) }
    private val userViewModel: UserViewModel by lazy { UserViewModel(dataStoreManager) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        getDataFromRoom()
    }

    private fun initRecycler() {
        favoritesAdapter = FavoriteAdapter { id: Int ->
            val bundle = Bundle()
            bundle.putInt("id", id)
            findNavController().navigate(R.id.action_favoritesFragment_to_detailFragment, bundle)
        }
        binding.apply {
            rvFavorites.adapter = favoritesAdapter
            rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun getDataFromRoom() {
        userViewModel.getLoginId().observe(viewLifecycleOwner) {
            viewModel.getFavorite(it)
            viewModel.dataFavorite.observe(viewLifecycleOwner) { data ->
                favoritesAdapter.updateData(data)
            }
        }


    }


}