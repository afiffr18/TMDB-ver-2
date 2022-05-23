package id.afif.binarchallenge6.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.afif.binarchallenge6.Helper.DataStoreManager
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.R
import id.afif.binarchallenge6.database.UserDatabase
import id.afif.binarchallenge6.databinding.FragmentLoginBinding
import id.afif.binarchallenge6.viewmodel.MoviesViewModel
import id.afif.binarchallenge6.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var mDb: UserDatabase? = null

    private val dataStoreManager: DataStoreManager by lazy { DataStoreManager(requireContext()) }
    private val userViewModel: UserViewModel by lazy { UserViewModel(dataStoreManager) }

    private val userRepo: UserRepo by lazy { UserRepo(requireContext()) }
    private val moviesViewModel: MoviesViewModel by viewModelsFactory { MoviesViewModel(userRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = UserDatabase.getInstance(requireContext())

        isRegisterClicked()
        loginClicked()
    }

    private fun isRegisterClicked(){
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
    private fun loginClicked(){
        binding.btnLogin.setOnClickListener {
            isCheckAccountReady()
        }
    }

    private fun isCheckAccountReady(){
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val result = mDb?.userDao()?.getDataById(username, password)
//            moviesViewModel.getDataById(username, password)
//            val result = moviesViewModel.dataUser.value
            if (result == null) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        requireContext(),
                        "Username atau password salah!!",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.etPassword.text.clear()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    userViewModel.saveData(username, password)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }

            }
        }
    }

}