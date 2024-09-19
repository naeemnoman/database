package com.example.database

import androidx.room.Delete
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface StudentDao {
    @Query("SELECT * FROM student_table")
    fun getStudents(): List<Student>

    @Query("SELECT * FROM student_table WHERE roll_no = :rollNo")
    suspend fun findStudentByRollNo(rollNo: Int): Student

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("DELETE FROM student_table")
    suspend fun deleteAll()

@Query("SELECT (SELECT COUNT(*) FROM student_table) == 0")
fun isEmpty(): Boolean
}