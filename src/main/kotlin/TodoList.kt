package dev.hellofro

class TodoList {
    private val _userTasks = mutableMapOf<Int, MutableMap<Int, Task>>().withDefault { mutableMapOf() }
    private val _tagIndex = mutableMapOf<String, MutableSet<UserTask>>().withDefault { mutableSetOf() }

    fun addTask(userId: Int, taskDescription: String, dueDate: Int, tags: List<String>): Int {
        // Generate the task id
        val taskId = (_userTasks[userId]?.size ?: 0) + 1

        // Create a new task for the user
        val newTask = Task(
            id = taskId,
            description = taskDescription,
            dueDate = dueDate,
            tags = tags,
        )

        // Add the task to the users tasks by id
        _userTasks[userId]?.put(taskId, newTask)

        // If it has tags add it to the tag index
        if(tags.isNotEmpty()){
            indexTaskByTags(userId, taskId, tags)
        }

        return taskId
    }

    fun getAllTasks(userId: Int): List<String> {
        return emptyList()
    }

    fun getTasksForTag(userId: Int, tag: String): List<String> {
        return emptyList()
    }

    fun completeTask(userId: Int, taskId: Int) {

    }

    private fun indexTaskByTags(userId: Int, taskId: Int, tags: List<String>) {
        for(tag in tags) {
            _tagIndex[tag]?.add(UserTask(userId, taskId))
        }
    }

    private data class Task(
        val id: Int = 0,
        val description: String = "",
        val dueDate: Int,
        val isCompleted: Boolean = false,
        val tags: List<String> = emptyList()
    )

    private data class UserTask(val userId: Int, val taskId: Int)
}