package se.iwoio.project.datastoredemo.data

import se.iwoio.project.datastoredemo.R


data class TodoItem(
    val id: String,
    val title:String,
    val description:String,
    val createDate:String,
    var priority:TodoPriority,
    var isDone: Boolean
)

enum class TodoPriority {
    HIGH, NORMAL;

    fun getIcon(): Int {
        // The this is the value of this enum object
        return if (this == NORMAL) R.drawable.normal else R.drawable.important
    }
}

