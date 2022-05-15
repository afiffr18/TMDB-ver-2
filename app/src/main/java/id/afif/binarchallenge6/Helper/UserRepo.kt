package id.afif.binarchallenge6.Helper

import android.content.Context
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import id.afif.binarchallenge6.API.TMDBClient
import id.afif.binarchallenge6.API.TMDBService
import id.afif.binarchallenge6.database.User
import id.afif.binarchallenge6.database.UserDao
import id.afif.binarchallenge6.database.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepo(context: Context) {
    private val mDb = UserDatabase.getInstance(context)
    private val tmdbService = TMDBClient.instance


    suspend fun saveRegister(user : User) = withContext(Dispatchers.IO){
        mDb?.userDao()?.insertUser(user)
    }

    suspend fun getDataById(username : String,password:String) = withContext(Dispatchers.IO){
        mDb?.userDao()?.getDataById(username,password)
    }

    suspend fun updateData(user : User) = withContext(Dispatchers.IO){
        mDb?.userDao()?.updateUser(user)
    }

    suspend fun getAllMovies(key : String) = tmdbService.getAllMovie(key)

    suspend fun getMovieDetail(movieId : Int,key : String) = tmdbService.getMovieDetail(movieId, key)




}