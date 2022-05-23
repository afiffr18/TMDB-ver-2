package id.afif.binarchallenge6.database

import androidx.room.*

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM Favorite")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM Favorite where user_id = :userId")
    fun getAllFavorite(userId: Int): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    @Update
    fun updateFavorite(favorite: Favorite): Int

    @Delete
    fun deleteFavorite(favorite: Favorite): Int
}