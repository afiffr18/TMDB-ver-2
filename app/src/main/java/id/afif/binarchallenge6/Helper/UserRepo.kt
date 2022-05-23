package id.afif.binarchallenge6.Helper

import android.content.Context
import id.afif.binarchallenge6.API.TMDBClient
import id.afif.binarchallenge6.database.Favorite
import id.afif.binarchallenge6.database.User
import id.afif.binarchallenge6.database.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo(context: Context) {
    private val mDb = UserDatabase.getInstance(context)
    private val tmdbService = TMDBClient.instance

    //user
    suspend fun saveRegister(user: User) = withContext(Dispatchers.IO) {
        mDb?.userDao()?.insertUser(user)
    }


    suspend fun getDataById(username: String, password: String) = withContext(Dispatchers.IO) {
        mDb?.userDao()?.getDataById(username, password)
    }

    suspend fun updateData(user: User) = withContext(Dispatchers.IO) {
        mDb?.userDao()?.updateUser(user)
    }

    //    favorite
    suspend fun getFavorite(userId: Int) = withContext(Dispatchers.IO) {
        mDb?.favoriteDao()?.getAllFavorite(userId)
    }

    suspend fun insertFavorite(favorite: Favorite) =
        mDb?.favoriteDao()?.insertFavorite(favorite)


    suspend fun getAllMovies(key: String) = tmdbService.getAllMovie(key)

    suspend fun getMovieDetail(movieId: Int, key: String) = tmdbService.getMovieDetail(movieId, key)

}