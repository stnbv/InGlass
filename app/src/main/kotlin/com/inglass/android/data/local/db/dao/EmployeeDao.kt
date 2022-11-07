package com.inglass.android.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inglass.android.data.local.db.entities.Employee

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(items: List<Employee>)

    @Query("select * from employees")
    suspend fun getEmployees(): List<Employee>

    @Query("delete from employees")
    suspend fun deleteAllEmployees()
}
