package dev.hellofro

class TodoList {
    private val _userTasks = mutableMapOf<Int, MutableMap<Int, Task>>()
    private val _tagIndex = mutableMapOf<String, MutableSet<UserTask>>()

    fun addTask(userId: Int, taskDescription: String, dueDate: Int, tags: List<String>): Int {
        // Create entry for user if not already in db
        if(!_userTasks.containsKey(userId)){
            _userTasks.put(userId, mutableMapOf())
        }

        // Generate the task id
        val taskId = _userTasks[userId]!!.size + 1

        // Create a new task for the user
        val newTask = Task(
            id = taskId,
            description = taskDescription,
            dueDate = dueDate,
            tags = tags,
        )

        // Add the task to the users tasks by id
        _userTasks[userId]!![taskId] = newTask

        // If it has tags add it to the tag index
        if(tags.isNotEmpty()){
            indexTaskByTags(userId, taskId, tags)
        }

        return taskId
    }

    fun getAllTasks(userId: Int): List<String> {
        if(!_userTasks.containsKey(userId)){
            return emptyList()
        }

        // Filter user tasks for incompleted
        val incompleteTasks = _userTasks[userId]!!
            .filter { (_, value) -> !value.isCompleted }
            .values

        // Return early if all user tasks are complete
        if(incompleteTasks.isEmpty()){
            return emptyList()
        }

        // Map incompleted user task descriptions to list ordered by dueDate (asc)
        val allTasks = incompleteTasks
            .sortedBy { task -> task.dueDate }
            .map { task -> task.description }

        return allTasks
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