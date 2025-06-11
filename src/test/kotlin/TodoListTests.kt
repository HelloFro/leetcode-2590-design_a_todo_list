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
            when (test.given.functions[i]) {
                Functions.InitTodoList -> {
                    todoList = TodoList()
                    actual.add(null)
                }
                Functions.AddTask -> {
                    val task = test.given.params[i] as Task
                    val result = todoList!!.addTask(
                        task.userId,
                        task.description,
                        task.dueDate,
                        task.tags,
                    )
                    actual.add(result)
                }
                Functions.GetAllTasks -> {
                    val userId = test.given.params[i] as Int
                    val result = todoList!!.getAllTasks(userId)
                    actual.add(result)
                }
                Functions.GetTasksForTag -> {
                    val userTaskByTag = test.given.params[i] as UserTaskByTag
                    val result = todoList!!.getTasksForTag(userTaskByTag.userId, userTaskByTag.tag)
                    actual.add(result)
                }
                Functions.CompleteTask -> {
                    val userTask = test.given.params[i] as UserTask
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
                    params = listOf<Any?>(null)
                ),
                expected = Expected(output = mutableListOf(null))
            ),
            Test(
                name = "Create TodoList then addTask",
                given = Given(
                    functions = listOf(Functions.InitTodoList, Functions.AddTask),
                    params = listOf<Any?>(null, Task(1, "Task1", 50, emptyList()))
                ),
                expected = Expected(output = mutableListOf(null, 1))
            ),
            Test(
                name = "Create TodoList then addTask two times",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask,
                        Functions.AddTask
                    ),
                    params = listOf<Any?>(
                        null,
                        Task(1, "Task1", 50, emptyList()),
                        Task(1, "Task2", 100, listOf("P1"))
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2
                    )
                )
            ),
            Test(
                name = "Create TodoList, 2x addTask, and getAllTasks",
                given = Given(
                    functions = listOf(
                        Functions.InitTodoList,
                        Functions.AddTask,
                        Functions.AddTask,
                        Functions.GetAllTasks
                    ),
                    params = listOf(
                        null,
                        Task(1, "Task1", 50, emptyList()),
                        Task(1, "Task2", 100, listOf("P1")),
                        1
                    )
                ),
                expected = Expected(
                    output = mutableListOf(
                        null, 1, 2, listOf("Task1", "Task2")
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

    data class Given(val functions: List<Functions>, val params: List<Any?>)

    data class Expected(val output: MutableList<Any?>){
        override fun toString(): String {
            return "expected = $output"
        }
    }

    sealed class Functions {
        data object InitTodoList : Functions()
        data object AddTask : Functions()
        data object GetAllTasks : Functions()
        data object GetTasksForTag : Functions()
        data object CompleteTask : Functions()
    }

    private data class Task(
        val userId: Int = 0,
        val description: String = "",
        val dueDate: Int,
        val tags: List<String> = emptyList()
    )

    private data class UserTask(val userId: Int, val taskId: Int)

    private data class UserTaskByTag(val userId: Int, val tag: String)
} 