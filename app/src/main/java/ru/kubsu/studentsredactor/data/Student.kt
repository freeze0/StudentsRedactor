package ru.kubsu.studentsredactor.data

data class Student(var studentId: Int,
                   var name: String,
                   var surname: String,
                   var patronymic: String,
                   var group: String,
                   var course: Int) {

    override fun toString(): String {
        return "Имя = '$name' \nФамилия= '$surname' \n" +
                "Отчество = '$patronymic' \nГруппа = '$group' \nКурс= $course"
    }
}
