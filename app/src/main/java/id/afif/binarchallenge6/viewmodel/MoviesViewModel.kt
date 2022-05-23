package id.afif.binarchallenge6.viewmodel




import androidx.lifecycle.*
import id.afif.binarchallenge6.Helper.UserRepo
import id.afif.binarchallenge6.Model.Resource
import id.afif.binarchallenge6.database.Favorite
import id.afif.binarchallenge6.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(private val userRepo: UserRepo) : ViewModel() {


    private val _dataUser = MutableLiveData<User>()
    val dataUser: LiveData<User> get() = _dataUser

    private val _dataFavorit = MutableLiveData<List<Favorite>>()
    val dataFavorite: LiveData<List<Favorite>> get() = _dataFavorit

    //API
    fun getAllMovies() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.succes(userRepo.getAllMovies("c548b9c05e09ed4c22de8c8eed87a602")))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error ocured"))
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

    //Room

   fun saveToDb(user : User){
       viewModelScope.launch {
           userRepo.saveRegister(user)
       }
   }

   fun getDataById(username : String, password:String){
       viewModelScope.launch{
          val user = userRepo.getDataById(username,password)!!
           _dataUser.postValue(user)
       }

   }

    fun updateData(user: User) {
        viewModelScope.launch {
            userRepo.updateData(user)!!
        }

    }

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            userRepo.insertFavorite(favorite)
        }
    }

    fun getFavorite(userId: Int) {
        viewModelScope.launch {
            val favorite = userRepo.getFavorite(userId)
            _dataFavorit.postValue(favorite!!)
        }
    }


}