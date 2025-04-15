package dev.hellofro

fun main(){
    val todoList = TodoList()
    todoList.addTask(1, "Task1", 50, emptyList())
    todoList.addTask(1, "Task2", 100, listOf("P1"))
    todoList.getAllTasks(1)
    todoList.getAllTasks(5)
    todoList.addTask(1, "Task3", 30, listOf("P1"))
    todoList.getTasksForTag(1, "P1")
    todoList.completeTask(5, 1)
    todoList.completeTask(1, 2)
    todoList.getTasksForTag(1, "P1")
    todoList.getAllTasks(1)
}