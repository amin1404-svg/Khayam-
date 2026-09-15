package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY id ASC")
    fun getAllActiveProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProductsForAdmin(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: Long): Flow<Product?>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductByIdSync(id: Long): Product?

    @Query("SELECT * FROM products WHERE isFavorite = 1")
    fun getFavoriteProducts(): Flow<List<Product>>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("UPDATE products SET isFavorite = :isFav WHERE id = :productId")
    suspend fun setFavorite(productId: Long, isFav: Boolean)

    @Query("UPDATE products SET stockQuantity = :newStock, basePriceTomans = :newPrice, discountPercent = :discount, description = :description WHERE id = :productId")
    suspend fun updateProductDetails(productId: Long, newStock: Int, newPrice: Long, discount: Int, description: String)
}
