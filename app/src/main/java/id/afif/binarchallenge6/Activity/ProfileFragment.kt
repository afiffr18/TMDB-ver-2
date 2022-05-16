package id.afif.binarchallenge6.Activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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

    companion object {
        const val REQUEST_CODE_PERMISSION = 100
    }


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

        binding.btnSunting.setOnClickListener {
            checkingPermissions()
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
    private fun checkingPermissions() {
        // apakah permission sudah di setujui atau belum
        if (isGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // klau udah di tolak sebelumnya
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            }
            // klau belum pernah di tolak (request pertama kali)
            else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    // dialoag yg muncul kalau user menolak permission yg di butuhkan
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                // mengarahkan user untuk buka halaman setting
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }


    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ ->  openCamera()}
            .show()
    }




    // buat dapetin URI image gallery
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            // munculin image dari gallery ke ImageView
            binding.ivProfile.setImageURI(result)
        }

    // buat buka gallery
    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }




    // buat dapeting bitmap image dari camera
    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // dapetin data bitmap dari intent
                val bitmap = result.data!!.extras?.get("data") as Bitmap

                // load bitmap ke dalam imageView
                binding.ivProfile.setImageBitmap(bitmap)
            }
        }

    // buat open camera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }








}