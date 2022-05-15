package id.afif.binarchallenge6.Activity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import id.afif.binarchallenge6.Helper.DataStoreManager
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.R
import id.afif.binarchallenge6.database.User
import id.afif.binarchallenge6.databinding.FragmentProfileBinding
import id.afif.binarchallenge6.viewmodel.MoviesViewModel
import id.afif.binarchallenge6.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userRepo : UserRepo by lazy { UserRepo(requireContext()) }
    private val viewModel : MoviesViewModel by viewModelsFactory { MoviesViewModel(userRepo) }

    private val dataStoreManager : DataStoreManager by lazy { DataStoreManager(requireContext()) }
    private val userViewModel : UserViewModel by viewModelsFactory { UserViewModel(dataStoreManager) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        updateData()
        binding.btnLogout.setOnClickListener{
            userViewModel.logoutSession()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }


    private fun bindData(){
        userViewModel.getUserLogin().observe(viewLifecycleOwner){
            val username = it[0]
            val password = it[1]
            viewModel.getDataById(username, password)
        }

        viewModel.dataUser.observe(viewLifecycleOwner){
            binding.etUsername.setText(it.username)
            binding.etEmail.setText(it.email)
            binding.etFullname.setText(it.fullname)
            binding.etTanggal.setText(it.tanggal)
            binding.etAlamat.setText(it.alamat)
        }
    }

    private fun updateData(){
        binding.btnUpdate.setOnClickListener{
            binding.etUsername.setBackgroundColor(Color.GRAY)
            val email = binding.etEmail.text.toString()
            val fullname = binding.etFullname.text.toString()
            val tanggal = binding.etTanggal.text.toString()
            val alamat = binding.etAlamat.text.toString()

            viewModel.dataUser.observe(viewLifecycleOwner){
                val newUser = User(it.id,it.username,email, it.password,fullname,tanggal,alamat)
                viewModel.updateData(newUser)
                Toast.makeText(requireContext(),"Data Berhasil di Update", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }








}