package com.uv.skillforge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.uv.skillforge.ui.SkillforgeViewModel
import com.uv.skillforge.ui.screens.CourseDetailScreen
import com.uv.skillforge.ui.screens.HomeScreen
import com.uv.skillforge.ui.screens.LessonScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: String) = "course_detail/$courseId"
    }
    object Lesson : Screen("lesson/{courseId}/{lessonId}") {
        fun createRoute(courseId: String, lessonId: String) = "lesson/$courseId/$lessonId"
    }
}

@Composable
fun SkillforgeNavHost(
    navController: NavHostController,
    viewModel: SkillforgeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel, onCourseClick = { courseId ->
                navController.navigate(Screen.CourseDetail.createRoute(courseId))
            })
        }
        composable(Screen.CourseDetail.route) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            CourseDetailScreen(viewModel, courseId, 
                onBackClick = { navController.popBackStack() },
                onLessonClick = { lessonId ->
                    navController.navigate(Screen.Lesson.createRoute(courseId!!, lessonId))
                }
            )
        }
        composable(Screen.Lesson.route) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            val lessonId = backStackEntry.arguments?.getString("lessonId")
            LessonScreen(viewModel, courseId, lessonId, 
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
