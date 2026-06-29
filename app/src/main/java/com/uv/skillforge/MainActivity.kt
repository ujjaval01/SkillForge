package com.uv.skillforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.uv.skillforge.ui.SkillforgeViewModel
import com.uv.skillforge.ui.navigation.SkillforgeNavHost
import com.uv.skillforge.ui.theme.SkillforgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillforgeTheme {
                val navController = rememberNavController()
                val viewModel: SkillforgeViewModel = viewModel()
                SkillforgeNavHost(navController = navController, viewModel = viewModel)
            }
        }
    }
}
