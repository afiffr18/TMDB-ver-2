package id.afif.binarchallenge6.Activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import id.afif.binarchallenge6.Helper.DataStoreManager
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.R
import id.afif.binarchallenge6.databinding.FragmentSplashBinding
import id.afif.binarchallenge6.viewmodel.UserViewModel

class SplashFragment : Fragment() {
    private var _binding : FragmentSplashBinding?=null
    private val binding get() = _binding!!

    private val dataStoreManager : DataStoreManager by lazy { DataStoreManager(requireContext()) }
    private val userViewModel : UserViewModel by viewModelsFactory { UserViewModel(dataStoreManager) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            userViewModel.isLoggin().observe(viewLifecycleOwner){
                if (it != true){
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }else{
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }
            }

        },3000)
    }


}