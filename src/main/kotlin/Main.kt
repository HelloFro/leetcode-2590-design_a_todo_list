package dev.hellofro

fun main(){
    val actual = mutableListOf<Any?>()

    val todoList = TodoList()
    actual.add(null)

    actual.add(
        todoList.addTask(1, "Task1", 50, emptyList())
    ) // return 1. This adds a new task for the user with id 1.
    actual.add(
        todoList.addTask(1, "Task2", 100, listOf("P1"))
    ) // return 2. This adds another task for the user with id 1.
    actual.add(
        todoList.getAllTasks(1)
    ) // return ["Task1", "Task2"]. User 1 has two uncompleted tasks so far.
    actual.add(
        todoList.getAllTasks(5)
    ) // return []. User 5 does not have any tasks so far.
    actual.add(
        todoList.addTask(1, "Task3", 30, listOf("P1"))
    ) // return 3. This adds another task for the user with id 1.
    actual.add(
        todoList.getTasksForTag(1, "P1")
    ) // return ["Task3", "Task2"]. This returns the uncompleted tasks that have the tag "P1" for the user with id 1.

    todoList.completeTask(5, 1)
    // This does nothing, since task 1 does not belong to user 5.
    actual.add(null)

    todoList.completeTask(1, 2)
    // This marks task 2 as completed.
    actual.add(null)

    actual.add(
        todoList.getTasksForTag(1, "P1")
    )  // return ["Task3"]. This returns the uncompleted tasks that have the tag "P1" for the user with id 1.
       // Notice that we did not include "Task2" because it is completed now.
    actual.add(
        todoList.getAllTasks(1)
    ) // return ["Task3", "Task1"]. User 1 now has 2 uncompleted tasks.

    println(actual)
}