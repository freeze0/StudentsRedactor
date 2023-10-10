package ru.kubsu.studentsredactor

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RedactorActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextPatronymic: EditText
    private lateinit var editTextGroup: EditText
    private lateinit var editTextCourse: EditText
    private lateinit var btnSave: Button

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
                    val updatedName = editTextName.text.toString()
                    val updatedSurname = editTextSurname.text.toString()
                    val updatedPatronymic = editTextPatronymic.text.toString()
                    val updatedGroup = editTextGroup.text.toString()
                    val updatedCourse = editTextCourse.text.toString().toInt()

                    val resultIntent = Intent()
                    resultIntent.putExtra("updatedName", updatedName)
                    resultIntent.putExtra("updatedSurname", updatedSurname)
                    resultIntent.putExtra("updatedPatronymic", updatedPatronymic)
                    resultIntent.putExtra("updatedGroup", updatedGroup)
                    resultIntent.putExtra("updatedCourse", updatedCourse)

                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }


    }

}