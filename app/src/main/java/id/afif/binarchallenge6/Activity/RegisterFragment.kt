package id.afif.binarchallenge6.Activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Helper.viewModelsFactory
import id.afif.binarchallenge6.database.User
import id.afif.binarchallenge6.database.UserDatabase
import id.afif.binarchallenge6.databinding.FragmentRegisterBinding
import id.afif.binarchallenge6.viewmodel.MoviesViewModel

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var mDb : UserDatabase?=null
    private val userRepo : UserRepo by lazy { UserRepo(requireContext()) }
    private val moviesViewModel : MoviesViewModel by viewModelsFactory { MoviesViewModel(userRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = UserDatabase.getInstance(requireContext())
        addData()
    }


    private fun addData(){
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(isAllFieldChecked(username,email,password)){
                val data = User(null,username,email,password,"","12/12/12","")
                moviesViewModel.saveToDb(data)
                findNavController().popBackStack()
            }else{
                if(isPasswordTheSame()){
                    Toast.makeText(requireContext(),"Password Tidak Sama!!", Toast.LENGTH_SHORT).show()
                    binding.etConfirmPassword.text.clear()
                }else{
                    Toast.makeText(requireContext(),"Data harus Diisi!!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }




    private fun isAllFieldChecked(username : String,email:String,password: String) : Boolean{
        binding.apply {
            if(username.isBlank() || username == " "){
                etUsername.error = "Username harus diisi"
                return false
            }
            if (email.isBlank()||email ==" " ||!email.contains("@")||!email.contains("mail.")){
                etEmail.error = "Email tidak valid"
                return false
            }
            if(password.isBlank()||password==""){
                etPassword.error = "Password Harus Diisi"
                return false
            }
        }
        return true
    }

    private fun isPasswordTheSame() : Boolean{
        binding.apply {
            return etPassword.text.toString() != etConfirmPassword.text.toString()
        }
    }



}