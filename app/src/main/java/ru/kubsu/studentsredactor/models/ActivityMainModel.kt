package ru.kubsu.studentsredactor.models

import androidx.lifecycle.ViewModel
import ru.kubsu.studentsredactor.data.Student

class ActivityMainModel : ViewModel() {
    private val studentList: MutableList<Student> = mutableListOf()

    init {
        addStudent(Student(0, "Иван", "Иванов", "Иванович",
            "13", 1))
        addStudent(Student(1, "Петр", "Петров", "Петрович",
            "23", 2))
    }

    private var currentIndex = 0

    fun addStudent(student: Student) {
        studentList.add(student)
    }

    fun getCount(): Int {
        return studentList.size
    }

    fun removeCurrentStudent() {
        studentList.removeAt(currentIndex)
    }

    fun getCurrentStudent(): Student? {
        return studentList[currentIndex]
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % studentList.size
    }

    fun moveToPrev() {
        currentIndex = (studentList.size + currentIndex - 1) % studentList.size
    }

    fun getMaxId(): Int {
        var _max = -1
        for (student in studentList) {
            if (student.studentId > _max)
                _max = student.studentId
        }
        return _max
    }

    fun printCurrentStudentInfo(): String {
        if (getCount() == 0)
            return "No students"
        val student = studentList[currentIndex]
        return if (student != null) {
            student.toString()
        } else
            "No student at current index"
    }



}
