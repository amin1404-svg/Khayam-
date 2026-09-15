package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    fun getOrderById(id: Long): Flow<Order?>

    @Query("SELECT * FROM orders WHERE orderNumber = :orderNumber LIMIT 1")
    fun getOrderByNumber(orderNumber: String): Flow<Order?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus)

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrdersCount(): Int

    @Query("SELECT SUM(totalPayableTomans) FROM orders WHERE status != 'CANCELLED'")
    suspend fun getTotalSalesTomans(): Long?
}
