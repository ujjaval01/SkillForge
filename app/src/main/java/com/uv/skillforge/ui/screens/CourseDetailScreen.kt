package com.uv.skillforge.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uv.skillforge.data.model.Course
import com.uv.skillforge.data.model.Instructor
import com.uv.skillforge.data.model.Lesson
import com.uv.skillforge.ui.SkillforgeUiState
import com.uv.skillforge.ui.SkillforgeViewModel
import com.uv.skillforge.ui.theme.StarYellow
import com.uv.skillforge.ui.theme.TealDark
import com.uv.skillforge.ui.theme.TealPrimary
import com.uv.skillforge.ui.theme.TextGray

@Composable
fun CourseDetailScreen(
    viewModel: SkillforgeViewModel,
    courseId: String?,
    onBackClick: () -> Unit,
    onLessonClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is SkillforgeUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is SkillforgeUiState.Error -> {
                Text(text = state.message, modifier = Modifier.align(Alignment.Center))
            }
            is SkillforgeUiState.Success -> {
                val course = state.data.categories.flatMap { it.courses }.find { it.id == courseId }
                if (course != null) {
                    CourseDetailContent(course, onBackClick, onLessonClick)
                } else {
                    Text(text = "Course not found", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun CourseDetailContent(
    course: Course,
    onBackClick: () -> Unit,
    onLessonClick: (String) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        bottomBar = {
            BottomEnrollBar(course.isFree())
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                CourseHeroSection(course, onBackClick)
            }
            item {
                CourseInfoSection(course)
            }
            item {
                InstructorCard(course.instructor)
            }
            item {
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 22.sp,
                        color = Color(0xFF4D4D4D)
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Course content",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "${course.lessons.size} lessons • ${course.lessons.sumOf { it.durationMinutes }} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(course.lessons) { lesson ->
                LessonItem(lesson, onClick = { if (lesson.isFree) onLessonClick(lesson.id) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CourseHeroSection(course: Course, onBackClick: () -> Unit) {
    val gradientColors = when (course.id) {
        "course_kotlin_101" -> listOf(Color(0xFF2DD4BF), Color(0xFF0D9488)) // Teal
        "course_compose_201" -> listOf(Color(0xFF6366F1), Color(0xFF4F46E5)) // Indigo/Purple
        "course_node_302" -> listOf(Color(0xFF10B981), Color(0xFF059669)) // Emerald/Green
        else -> {
            if (course.id.contains("android")) {
                listOf(Color(0xFF2DD4BF), Color(0xFF0D9488))
            } else if (course.id.contains("node") || course.id.contains("rest")) {
                listOf(Color(0xFF10B981), Color(0xFF059669))
            } else {
                listOf(Color(0xFFFBBF24), Color(0xFFD97706)) // Amber/Orange
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .background(
                Brush.linearGradient(
                    colors = gradientColors
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw soft circle on the right
            drawCircle(
                color = Color.White.copy(alpha = 0.07f),
                radius = 180.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.15f)
            )
            
            // Draw soft circle on the left
            drawCircle(
                color = Color.White.copy(alpha = 0.04f),
                radius = 120.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(size.width * 0.1f, size.height * 0.75f)
            )
            
            // Draw grid of dots
            val dotRadius = 1.5.dp.toPx()
            val spacingX = 14.dp.toPx()
            val spacingY = 14.dp.toPx()
            val startX = 36.dp.toPx()
            val startY = 100.dp.toPx()
            val rows = 5
            val cols = 8
            for (r in 0 until rows) {
                for (c in 0 until cols) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.15f),
                        radius = dotRadius,
                        center = androidx.compose.ui.geometry.Offset(startX + c * spacingX, startY + r * spacingY)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Top Nav Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder, 
                        contentDescription = "Bookmark",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Tag "// kotlin" inside a rounded capsule
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "// ${course.tags.firstOrNull() ?: "kotlin"}",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            // Large White Text
            Text(
                text = course.title,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 36.sp,
                    lineHeight = 42.sp
                ),
                maxLines = 2
            )
        }

        // Smooth bottom gradient fade to main screen background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFFF9F9F9))
                    )
                )
        )
    }
}

@Composable
fun CourseInfoSection(course: Course) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        // Tag Pills Row
        Row(
            modifier = Modifier.padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            course.tags.take(3).forEach { tag ->
                Surface(
                    color = TealPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50) // Pill capsule
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = TealDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Text(
            text = course.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                lineHeight = 34.sp
            ),
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = course.subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 22.sp,
                color = TextGray
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Stats Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = StarYellow,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = course.rating.toString(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
            val enrolledText = java.text.NumberFormat.getNumberInstance(java.util.Locale.US)
                .format(course.studentsEnrolled)
            Text(
                text = enrolledText,
                color = TextGray,
                fontSize = 14.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${course.durationHours}h",
                    color = TextGray,
                    fontSize = 14.sp
                )
            }
            Text(
                text = course.level,
                color = Color(0xFF00BFA5),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun InstructorCard(instructor: Instructor) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEBEBEB))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val initials = instructor.name.split(" ")
                .mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("")
                .take(2)
                .uppercase()

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00BFA5)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.ifEmpty { "AS" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = instructor.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = instructor.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
            }
            Text(
                text = "Follow",
                color = Color(0xFF00BFA5),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
}

@Composable
fun LessonItem(lesson: Lesson, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clickable(enabled = lesson.isFree) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEBEBEB))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (lesson.isFree) Color(0xFF00BFA5).copy(alpha = 0.1f) 
                        else Color(0xFFF5F5F5)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (lesson.isFree) Icons.Default.PlayArrow else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (lesson.isFree) Color(0xFF00BFA5) else Color(0xFFCCCCCC),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title, 
                    fontWeight = FontWeight.Bold,
                    color = if (lesson.isFree) Color.Black else Color(0xFF666666),
                    fontSize = 15.sp
                )
                Text(
                    text = "${lesson.durationMinutes} min", 
                    style = MaterialTheme.typography.bodySmall, 
                    color = TextGray
                )
            }
            if (lesson.isFree) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF00BFA5).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "FREE",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color(0xFF00BFA5),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BottomEnrollBar(isFree: Boolean) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            HorizontalDivider(color = Color(0xFFEBEBEB), thickness = 1.dp)
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PRICE", 
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextGray,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = if (isFree) "Free" else "$99.99",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF00BFA5)
                        )
                    )
                }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFA5)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .width(220.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Enroll now", 
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
