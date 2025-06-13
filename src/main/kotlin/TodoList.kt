package dev.hellofro

class TodoList {
    private val _userTasks = mutableMapOf<Int, MutableMap<Int, Task>>()
    private var _taskCount = 0

    /*
    *  Adds a task for the user with the ID [userId] with a due date equal to [dueDate]
    *  and a list of tags attached to the task. The return value is the ID of the task.
    *  This ID starts at 1 and is sequentially increasing. That is, each user's first task's id should be 1,
    *  the second task's id should be 2, and so on.
    */
    fun addTask(userId: Int, taskDescription: String, dueDate: Int, tags: List<String>): Int {
        // Create entry for user if not already in db
        if(!_userTasks.containsKey(userId)){
            _userTasks.put(userId, mutableMapOf())
        }

        // Generate the task id
        val taskId = _taskCount + 1

        // Create a new task for the user
        val newTask = Task(
            id = taskId,
            description = taskDescription,
            dueDate = dueDate,
            tags = tags,
        )

        // Add the task to the users tasks by id
        _userTasks[userId]!![taskId] = newTask

        _taskCount = taskId

        return taskId
    }

    /*
    * Returns a list of all the tasks not marked as complete for the user with ID [userId], ordered by the due date.
    * Returns an empty list if the user has no uncompleted tasks.
    */
    fun getAllTasks(userId: Int): List<String> {
        val incompleteTasks = getIncompleteTasks(userId)

        // Return early if all user tasks are complete
        if(incompleteTasks.isEmpty()){
            return emptyList()
        }

        // Map incompleted user task descriptions to list ordered by dueDate (asc)
        val allTasks = incompleteTasks
            .sortedByDueDate()
            .map { task -> task.description }

        return allTasks
    }

    /*
    * Returns a list of all the tasks that are not marked as complete for the user with the ID [userId]
    * and have [tag] as one of their tags, ordered by their due date.
    * Returns an empty list if no such task exists.
    */
    fun getTasksForTag(userId: Int, tag: String): List<String> {
        val incompleteTasks = getIncompleteTasks(userId)

        // Return early if all user tasks are complete
        if(incompleteTasks.isEmpty()){
            return emptyList()
        }

        // Map incompleted user task descriptions to list ordered by dueDate (asc)
        val tasksWithTag = incompleteTasks
            .filter { task -> task.tags.contains(tag) }
            .sortedByDueDate()
            .map { task -> task.description }

        return tasksWithTag
    }

    /*
    * Marks the task with the ID [taskId] as completed only if the task exists
    * and the user with the ID [userId] has this task, and it is uncompleted.
    */
    fun completeTask(userId: Int, taskId: Int) {
        val incompleteTasks = getIncompleteTasks(userId)

        // Skip if all user tasks are complete
        if(incompleteTasks.isEmpty()){
            return
        }

        // Find task by id or null
        val taskToUpdate = incompleteTasks.find { task -> task.id == taskId }

        // Update task as complete
        if (taskToUpdate != null){
            _userTasks[userId]!![taskId] = taskToUpdate.copy(isCompleted = true)
        }
    }

    private fun getIncompleteTasks(userId: Int): List<Task> {
        if(!_userTasks.containsKey(userId)){
            return emptyList()
        }

        // Filter user tasks for incompleted
        return _userTasks[userId]!!
            .filter { (_, value) -> !value.isCompleted }
            .values.toList()
    }

    private fun List<Task>.sortedByDueDate(asc: Boolean = true): List<Task> {
        return if(asc) { this.sortedBy { task -> task.dueDate } }
        else { this.sortedByDescending { task -> task.dueDate } }
    }

    private data class Task(
        val id: Int = 0,
        val description: String = "",
        val dueDate: Int,
        val isCompleted: Boolean = false,
        val tags: List<String> = emptyList()
    )
}