package com.example.database

import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.database.StudentDataBase.Companion.getDatabase
import com.example.database.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding 
    private lateinit var StudentDataBase: StudentDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        StudentDataBase = StudentDataBase.getDatabase(this)
       
        
        binding.saveBtn.setOnClickListener {
            saveData()
        }

        binding.searchBtn.setOnClickListener {
            searchData()
        }

        binding.deleteBtn.setOnClickListener {
            GlobalScope.launch { 
                StudentDataBase.studentDao().deleteAll()
            }
        }
    }

    private fun searchData() {
        val rollNo = binding.searchET.text.toString()
        if (rollNo.isNotEmpty()) {
            lateinit var student: Student
            GlobalScope.launch {
                student = StudentDataBase.studentDao().findStudentByRollNo(rollNo.toInt())
                if (StudentDataBase.studentDao().isEmpty()) {
                    Handler(Looper.getMainLooper()).post() {
                        Toast.makeText(this@MainActivity, "Database Have no data", Toast.LENGTH_SHORT).show()
                    }else{
                        displayData(student)
                } 
                } 
            }
    }
    }
    
    private suspend fun displayData(Student: Student) {
        withContext(Dispatchers.Main){
            binding.fristNameET.setText(Student.fristName.toString())
            binding.lastNameET.setText(Student.lastName.toString())
            binding.rollNoET.setText(Student.rollNo.toString())
        }
    }
    
    private fun saveData() {
        val fristName = binding.fristNameET.text.toString()
        val lastName = binding.lastNameET.text.toString()
        val rollNo = binding.rollNoET.text.toString()
        if (fristName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            val student = Student(0, fristName, lastName, rollNo.toInt())
            GlobalScope.launch {
                StudentDataBase.studentDao().insert(student)
            }
            binding.fristNameET.text.clear()
            binding.lastNameET.text.clear()
            binding.rollNoET.text.clear()
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}