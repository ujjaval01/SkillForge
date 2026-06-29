package com.uv.skillforge.ui.screens

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
import com.uv.skillforge.data.model.Lesson
import com.uv.skillforge.ui.SkillforgeUiState
import com.uv.skillforge.ui.SkillforgeViewModel
import com.uv.skillforge.ui.theme.StarYellow
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Course content",
                        fontWeight = FontWeight.ExtraBold,
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(TealPrimary, Color(0xFF008E76))
                )
            )
    ) {
        // Mocking the mesh background pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f)
                .background(Color.White) // Placeholder for pattern
        )

        // Large background text
        Text(
            text = course.title,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .alpha(0.2f),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 60.sp
            ),
            maxLines = 2
        )
        
        // Tags row overlay
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, top = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            course.tags.take(3).forEach { tag ->
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
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
                    Icons.Default.BookmarkBorder, 
                    contentDescription = "Bookmark",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CourseInfoSection(course: Course) {
    Column(modifier = Modifier.padding(16.dp)) {
        Surface(
            color = TealPrimary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "// ${course.tags.firstOrNull() ?: "kotlin"}",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = TealPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
        Text(
            text = course.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            ),
            color = Color.Black
        )
        Text(
            text = course.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = course.rating.toString(), fontWeight = FontWeight.ExtraBold, color = Color.Black)
            }
            Text(text = "18,420", color = TextGray) 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Schedule, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${course.durationHours}h", color = TextGray)
            }
            Surface(
                color = TealPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = course.level,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = TealPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun InstructorCard(instructor: com.uv.skillforge.data.model.Instructor) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(TealPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AS", // Mocked initials
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = instructor.name, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                Text(text = instructor.title, style = MaterialTheme.typography.bodySmall, color = TextGray)
            }
            Text(
                text = "Follow",
                color = TealPrimary,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
}

@Composable
fun LessonItem(lesson: Lesson, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(enabled = lesson.isFree) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (lesson.isFree) TealPrimary.copy(alpha = 0.1f) 
                        else Color(0xFFF5F5F5)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (lesson.isFree) Icons.Default.PlayArrow else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (lesson.isFree) TealPrimary else Color(0xFFCCCCCC),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title, 
                    fontWeight = FontWeight.ExtraBold,
                    color = if (lesson.isFree) Color.Black else Color(0xFF999999)
                )
                Text(
                    text = "${lesson.durationMinutes} min", 
                    style = MaterialTheme.typography.bodySmall, 
                    color = TextGray
                )
            }
            if (lesson.isFree) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = TealPrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "FREE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = TealPrimary,
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
        shadowElevation = 20.dp,
        color = Color.White
    ) {
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
                    style = MaterialTheme.typography.bodySmall, 
                    color = TextGray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isFree) "Free" else "$99.99",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = TealPrimary
                )
            }
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(56.dp)
                    .width(220.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Enroll now", 
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
