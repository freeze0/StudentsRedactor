package ru.kubsu.studentsredactor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.kubsu.studentsredactor.data.Student
import ru.kubsu.studentsredactor.models.ActivityMainModel

class MainActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button
    private lateinit var btnChange: Button
    private lateinit var btnDelete: Button
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var studentsTv: TextView

    private val viewModel: ActivityMainModel by lazy {
        val provider = ViewModelProvider(this)
        provider.get(ActivityMainModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd = findViewById(R.id.btAdd)
        btnChange = findViewById(R.id.btChange)
        btnDelete = findViewById(R.id.btDelete)
        btnNext = findViewById(R.id.btNext)
        btnPrev = findViewById(R.id.btPrev)
        studentsTv = findViewById(R.id.tvMain)
        updateStudent()

        btnNext.setOnClickListener{if (viewModel.getCount() != 0){
            viewModel.moveToNext(); updateStudent()}
        }
        btnPrev.setOnClickListener{if (viewModel.getCount() != 0){
            viewModel.moveToPrev(); updateStudent()}
        }

        btnChange.setOnClickListener{
            if (viewModel.getCount() != 0){
                val currentStudent = viewModel.getCurrentStudent()
                currentStudent?.let { startRedactorActivity(it) }
            }
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this, RedactorActivity::class.java)
            intent.putExtra("add_intent", true)
            addStudentActivityResult.launch(intent)
            //startActivity(intent)
        }

        btnDelete.setOnClickListener {
            if (viewModel.getCount() == 1){
                viewModel.removeCurrentStudent()
                updateStudent()
            }
            if (viewModel.getCount() != 0){
                viewModel.removeCurrentStudent()
                viewModel.moveToPrev()
                updateStudent()
            }

        }
    }

    private fun updateStudent() {
        val studentInfo = viewModel.printCurrentStudentInfo()
        studentsTv.setText(studentInfo)
    }

    private fun startRedactorActivity(student: Student) {
        val intent = Intent(this, RedactorActivity::class.java)
        intent.putExtra("add_intent", false)
        intent.putExtra("studentId", student.studentId)
        intent.putExtra("name", student.name)
        intent.putExtra("surname", student.surname)
        intent.putExtra("patronymic", student.patronymic)
        intent.putExtra("group", student.group)
        intent.putExtra("course", student.course)
        editStudentActivityResult.launch(intent)
        //startActivity(intent)
    }


    private val editStudentActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.let {
                val updatedName = it.getStringExtra("updatedName") ?: ""
                val updatedSurname = it.getStringExtra("updatedSurname") ?: ""
                val updatedPatronymic = it.getStringExtra("updatedPatronymic") ?: ""
                val updatedGroup = it.getStringExtra("updatedGroup") ?: ""
                val updatedCourse = it.getIntExtra("updatedCourse", 0)

                val currentStudent = viewModel.getCurrentStudent()
                currentStudent?.let {
                    it.name = updatedName
                    it.surname = updatedSurname
                    it.patronymic = updatedPatronymic
                    it.group = updatedGroup
                    it.course = updatedCourse
                    updateStudent()
                }
            }
        }
    }

    private val addStudentActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data =result.data
            data?.let {
                val myArrayList: ArrayList<String>? = data.getStringArrayListExtra("List")
                if (myArrayList != null) {
                    val groupSize = 6
                    val arrayListSize = myArrayList.size
                    for (i in 0 until arrayListSize step groupSize) {
                        val endIndex = minOf(i + groupSize, arrayListSize)
                        val subList = myArrayList.subList(i, endIndex)
                        val updatedName = subList[0]
                        val updatedSurname = subList[1]
                        val updatedPatronymic = subList[2]
                        val updatedGroup = subList[3]
                        val updatedCourse = subList[4].toInt()
                        val updatedId = subList[5].toInt()
                        viewModel.addStudent(Student(updatedId, updatedName, updatedSurname, updatedPatronymic,
                            updatedGroup, updatedCourse))
                        updateStudent()
                    }
                }
            }
        }
        }
    }


