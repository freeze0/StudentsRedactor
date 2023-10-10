package ru.kubsu.studentsredactor

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
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
        }

        btnDelete.setOnClickListener {
            if (viewModel.getCount() == 1){ //надо допилисть пролистывание если 0 элементов
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
    // надо пределать логику чтобы после удаления элемента в середине все равно листалось!!!
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
            val data = result.data
            data?.let {
                val newId = viewModel.getCount()
                val updatedName = it.getStringExtra("updatedName") ?: ""
                val updatedSurname = it.getStringExtra("updatedSurname") ?: ""
                val updatedPatronymic = it.getStringExtra("updatedPatronymic") ?: ""
                val updatedGroup = it.getStringExtra("updatedGroup") ?: ""
                val updatedCourse = it.getIntExtra("updatedCourse", 0)
                viewModel.addStudent(Student(newId, updatedName, updatedSurname, updatedPatronymic,
                    updatedGroup, updatedCourse))
            }
        }
    }

}