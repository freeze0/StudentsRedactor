package ru.kubsu.studentsredactor

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import ru.kubsu.studentsredactor.data.Student
import ru.kubsu.studentsredactor.models.ActivityMainModel

class RedactorActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextPatronymic: EditText
    private lateinit var editTextGroup: EditText
    private lateinit var editTextCourse: EditText
    private lateinit var btnSave: Button

    private val viewModel: ActivityMainModel by lazy {
        val provider = ViewModelProvider(this)
        provider.get(ActivityMainModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redactor)


        editTextName = findViewById(R.id.edtName)
        editTextSurname = findViewById(R.id.edtSurname)
        editTextPatronymic = findViewById(R.id.edtPatronymic)
        editTextGroup = findViewById(R.id.edtGroup)
        editTextCourse = findViewById(R.id.edtCourse)
        btnSave = findViewById(R.id.btSave)

        if (!intent.getBooleanExtra("add_intent", false)) {
            val name = intent.getStringExtra("name")
            val surname = intent.getStringExtra("surname")
            val patronymic = intent.getStringExtra("patronymic")
            val group = intent.getStringExtra("group")
            val course = intent.getIntExtra("course", 0)
            editTextName.setText(name)
            editTextSurname.setText(surname)
            editTextPatronymic.setText(patronymic)
            editTextGroup.setText(group)
            editTextCourse.setText(course.toString())
        }

        btnSave.setOnClickListener {
            if (editTextName.text.toString().isBlank()){
                editTextName.error="Введите имя!"
            }
            else {
                if (editTextSurname.text.toString().isBlank()) {
                    editTextSurname.error="Введите Фамилию!"
                    editTextName.error=null
                }
                else {
                    editTextSurname.error=null
                    if (intent.getBooleanExtra("add_intent", true)) {

                        val updatedName = editTextName.text.toString()
                        val updatedSurname = editTextSurname.text.toString()
                        val updatedPatronymic = editTextPatronymic.text.toString()
                        val updatedGroup = editTextGroup.text.toString()

                        val stroka = editTextCourse.text.toString()
                        var updatedCourse = 0
                        if (stroka.isNotEmpty()) {
                            updatedCourse = editTextCourse.text.toString().toInt()
                        } else {
                            updatedCourse = 0
                        }

                        val newId = viewModel.getMaxId() + 1
                        viewModel.addStudent(
                            Student(
                                newId, updatedName, updatedSurname, updatedPatronymic,
                                updatedGroup, updatedCourse
                            )
                        )
                       // MainActivity.updateStudent()
                    }
                    var flag = false
                    if (intent.getBooleanExtra("add_intent", false) && flag == false) {
                        flag = true
                        val updatedName = editTextName.text.toString()
                        val updatedSurname = editTextSurname.text.toString()
                        val updatedPatronymic = editTextPatronymic.text.toString()
                        val updatedGroup = editTextGroup.text.toString()
                        val stroka = editTextCourse.text.toString()
                        var updatedCourse = 0
                        if (stroka.isNotEmpty()) {
                            updatedCourse = editTextCourse.text.toString().toInt()
                        } else {
                            updatedCourse = 0
                        }

                        val currentStudent = viewModel.getCurrentStudent()
                            currentStudent?.let {
                                it.name = updatedName
                                it.surname = updatedSurname
                                it.patronymic = updatedPatronymic
                                it.group = updatedGroup
                                it.course = updatedCourse
                            }
                        //updateStudent()
                    }


//                    val resultIntent = Intent()
//                    resultIntent.putExtra("updatedName", updatedName)
//                    resultIntent.putExtra("updatedSurname", updatedSurname)
//                    resultIntent.putExtra("updatedPatronymic", updatedPatronymic)
//                    resultIntent.putExtra("updatedGroup", updatedGroup)
//                    resultIntent.putExtra("updatedCourse", updatedCourse)
//
//                    setResult(Activity.RESULT_OK, resultIntent)
//                    finish()

                }
            }
        }


    }

}
