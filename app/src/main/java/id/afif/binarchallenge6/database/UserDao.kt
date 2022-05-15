package id.afif.binarchallenge6.database

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUser() : List<User>

    @Query("SELECT * FROM User WHERE username = :usernameCheck AND password = :passwordCheck")
    fun getUser(usernameCheck:String,passwordCheck:String) : List<User>

    @Query("SELECT * FROM User WHERE username = :userName AND password = :passWord")
    fun getDataById(userName:String,passWord:String) : User


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User) : Int

    @Delete
    fun deleteUser(user: User) : Int
}