package dev.hellofro

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class TodoListTests {
    @ParameterizedTest(name = "{0}")
    @MethodSource("todoListTestCases")
    fun testTodoList(test: Test) {
        var todoList: TodoList? = null
        val actual = mutableListOf<Any?>()
        for (i in test.given.functions.indices) {
            val currentFunction = test.given.functions[i]
            when (currentFunction) {
                Functions.InitTodoList -> {
                    todoList = TodoList()
                    actual.add(null)
                }
                is Functions.AddTask -> {
                    val task = currentFunction
                    val result = todoList!!.addTask(
                        task.userId,
                        task.description,
                        task.dueDate,
                        task.tags,
                    )
                    actual.add(result)
                }
                is Functions.GetAllTasks -> {
                    val userId = currentFunction.userId
                    val result = todoList!!.getAllTasks(userId)
                    actual.add(result)
                }
                is Functions.GetTasksForTag -> {
                    val userTaskByTag = currentFunction
                    val result = todoList!!.getTasksForTag(userTaskByTag.userId, userTaskByTag.tag)
                    actual.add(result)
                }
                is Functions.CompleteTask -> {
                    val userTask = currentFunction
                    todoList!!.completeTask(userTask.userId, userTask.taskId)
                    actual.add(null)
                }
            }
        }
        assertEquals(test.expected.output, actual)
    }

    // Add additional tests here
    companion object {
        @JvmStatic
        fun todoListTestCases() = listOf(
            Test(
                name = "Create TodoList",
                given = Given(
                    functions = listOf(Functions.InitTodoList),
                ),
                expected = Expected(output = mutableListOf(null))
            ),
            Test(
                name = "Create TodoList then addTask",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, emptyList())
                    )
                ),
                expected = Expected(output = mutableListOf(null, 1))
            ),
            Test(
                name = "addTask multiple times for user",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, emptyList()),
                        Functions.AddTask(1, "Task2", 100, listOf("P1"))
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2
                    )
                )
            ),
            Test(
                name = "getAllTasks for user with two tasks",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, emptyList()),
                        Functions.AddTask(1, "Task2", 100, listOf("P1")),
                        Functions.GetAllTasks(1)
                    ),
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2, listOf("Task1", "Task2")
                    )
                )
            ),
            Test(
                name = "getTasksForTag for a user with multiple tasks with tag",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, emptyList()),
                        Functions.AddTask(1, "Task2", 100, listOf("P1")),
                        Functions.AddTask(1, "Task3", 30, listOf("P1")),
                        Functions.GetTasksForTag(1,"P1")
                    ),
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2, 3, listOf("Task3", "Task2")
                    )
                )
            ),
            Test(
                name = "getTasksForTag when different users have the same tag",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, listOf("P1")),
                        Functions.AddTask(2, "Task2", 100, listOf("P1")),
                        Functions.AddTask(2, "Task3", 42, listOf("P1")),
                        Functions.AddTask(3, "Task4", 30, listOf("P1")),
                        Functions.GetTasksForTag(2,"P1")
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 1, 2, 1, listOf("Task3", "Task2")
                    )
                )
            ),
            Test(
                name = "getTasksForTag when user has no tasks with tag",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, listOf("P0")),
                        Functions.AddTask(1, "Task2", 100, listOf("P1")),
                        Functions.AddTask(1, "Task3", 30, listOf("P3")),
                        Functions.GetTasksForTag(1,"P2")
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2, 3, emptyList<String>()
                    )
                )
            ),
            Test(
                name = "completeTask for existing user and task",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, listOf("P0")),
                        Functions.AddTask(1, "Task2", 100, listOf("P1")),
                        Functions.AddTask(1, "Task3", 30, listOf("P2")),
                        Functions.CompleteTask(1,2),
                        Functions.GetAllTasks(1)
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2, 3, null, listOf("Task3", "Task1")
                    )
                )
            ),
            Test(
                name = "completeTask for existing user but task is already completed",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, listOf("P0")),
                        Functions.AddTask(1, "Task2", 100, listOf("P1")),
                        Functions.AddTask(1, "Task3", 30, listOf("P2")),
                        Functions.CompleteTask(1,2),
                        Functions.GetAllTasks(1),
                        Functions.CompleteTask(1,2),
                        Functions.GetAllTasks(1),
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2, 3, null, listOf("Task3", "Task1"), null, listOf("Task3", "Task1")
                    )
                )
            ),
            Test(
                name = "completeTask for existing user but task doesn't exist",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask(1, "Task1", 50, listOf("P0")),
                        Functions.AddTask(1, "Task2", 100, listOf("P1")),
                        Functions.AddTask(1, "Task3", 30, listOf("P2")),
                        Functions.CompleteTask(1,7),
                        Functions.GetAllTasks(1)
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2, 3, null, listOf("Task3", "Task1", "Task2")
                    )
                )
            ),
        )
    }

    data class Test(val name: String, val given: Given, val expected: Expected){
        override fun toString(): String {
            return "$name should return $expected"
        }
    }

    data class Given(val functions: List<Functions>)

    data class Expected(val output: MutableList<Any?>){
        override fun toString(): String {
            return "expected = $output"
        }
    }

    sealed class Functions {
        data object InitTodoList : Functions()
        data class AddTask(
            val userId: Int = 0,
            val description: String = "",
            val dueDate: Int,
            val tags: List<String> = emptyList()
        ) : Functions()
        data class GetAllTasks(
            val userId: Int = 0
        ) : Functions()
        data class GetTasksForTag(
            val userId: Int,
            val tag: String
        ) : Functions()
        data class CompleteTask(
            val userId: Int,
            val taskId: Int
        ) : Functions()
    }
} 