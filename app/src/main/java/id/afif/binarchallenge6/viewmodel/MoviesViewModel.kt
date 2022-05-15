package id.afif.binarchallenge6.viewmodel



import androidx.lifecycle.*
import id.afif.binarchallenge6.API.TMDBService
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Model.MovieDetail
import id.afif.binarchallenge6.Model.Movies
import id.afif.binarchallenge6.Model.Resource
import id.afif.binarchallenge6.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MoviesViewModel(private val userRepo: UserRepo) : ViewModel() {


    private val _dataUser = MutableLiveData<User>()
    val dataUser : LiveData<User> get() = _dataUser


    fun getAllMovies() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.succes(userRepo.getAllMovies("c548b9c05e09ed4c22de8c8eed87a602")))
        }catch (e : Exception){
            emit(Resource.error(null,e.message ?: "Error ocured"))
        }


    }

    fun getMoviesDetail(movieId : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.succes(userRepo.getMovieDetail(movieId,"c548b9c05e09ed4c22de8c8eed87a602")))
        }catch (ex : Exception ){
            emit(Resource.error(null,ex.message ?: "Error ocured"))
        }
    }



   fun saveToDb(user : User){
       viewModelScope.launch (Dispatchers.IO) {
           userRepo.saveRegister(user)
       }
   }

   fun getDataById(username : String, password:String){
       viewModelScope.launch{
          val user = userRepo.getDataById(username,password)!!
           _dataUser.postValue(user)
       }

   }

    fun updateData(user: User){
        viewModelScope.launch{
            userRepo.updateData(user)!!
        }

    }

}