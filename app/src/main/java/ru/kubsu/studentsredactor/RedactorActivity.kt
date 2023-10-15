package ru.kubsu.studentsredactor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
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
    private lateinit var viewModel: ActivityMainModel
    private lateinit var myList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redactor)

        var myList = ArrayList<String>()

        viewModel = ViewModelProvider(this).get(ActivityMainModel::class.java)

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
            if (editTextName.text.toString().isBlank()) {
                editTextName.error = "Введите имя!"
            } else {
                if (editTextSurname.text.toString().isBlank()) {
                    editTextSurname.error = "Введите Фамилию!"
                    editTextName.error = null
                } else {
                    editTextSurname.error = null
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

                        myList.add(updatedName)
                        myList.add(updatedSurname)
                        myList.add(updatedPatronymic)
                        myList.add(updatedGroup)
                        myList.add(updatedCourse.toString())
                        myList.add(newId.toString())
                        val intent = Intent()
                        intent.putExtra("List", myList)
                        setResult(RESULT_OK, intent);
                    }

                    else {
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

                        val intent = Intent()
                        intent.putExtra("updatedName", updatedName)
                        intent.putExtra("updatedSurname", updatedSurname)
                        intent.putExtra("updatedPatronymic", updatedPatronymic)
                        intent.putExtra("updatedGroup",updatedGroup)
                        intent.putExtra("updatedCourse", updatedCourse)
                        setResult(RESULT_OK, intent);
                    }
                }
            }
        }

    }

}
