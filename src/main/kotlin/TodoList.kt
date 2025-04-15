package dev.hellofro

class TodoList {
    fun addTask(userId: Int, taskDescription: String, dueDate: Int, tags: List<String>): Int {
        return 0
    }

    fun getAllTasks(userId: Int): List<String> {
        return emptyList()
    }

    fun getTasksForTag(userId: Int, tag: String): List<String> {
        return emptyList()
    }

    fun completeTask(userId: Int, taskId: Int) {

    }
}