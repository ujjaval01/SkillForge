package com.uv.skillforge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uv.skillforge.data.model.Lesson
import com.uv.skillforge.ui.SkillforgeUiState
import com.uv.skillforge.ui.SkillforgeViewModel
import com.uv.skillforge.ui.theme.TealPrimary
import com.uv.skillforge.ui.theme.TextDark
import com.uv.skillforge.ui.theme.TextGray

@Composable
fun LessonScreen(
    viewModel: SkillforgeViewModel,
    courseId: String?,
    lessonId: String?,
    onBackClick: () -> Unit
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
                val initialLesson = course?.lessons?.find { it.id == lessonId }
                
                if (course != null && initialLesson != null) {
                    var currentLesson by remember { mutableStateOf(initialLesson) }
                    LessonContent(course.title, currentLesson, course.lessons, onBackClick) { 
                        currentLesson = it
                    }
                } else {
                    Text(text = "Lesson not found", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun LessonContent(
    courseTitle: String,
    lesson: Lesson,
    allLessons: List<Lesson>,
    onBackClick: () -> Unit,
    onLessonClick: (Lesson) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        VideoPlayerPlaceholder(onBackClick)
        
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "LESSON ${allLessons.indexOf(lesson) + 1} • ${courseTitle.uppercase()}",
                color = TealPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Set up Android Studio and run your first Kotlin file.", // Mocked subtitle/content
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LessonTabs()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn {
                items(allLessons) { item ->
                    LessonListItem(
                        lesson = item,
                        isPlaying = item.id == lesson.id,
                        onClick = { if (item.isFree) onLessonClick(item) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun VideoPlayerPlaceholder(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Black)
    ) {
        // Mock video background
        AsyncImage(
            model = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?q=80&w=2070&auto=format&fit=crop",
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.5f),
            contentScale = ContentScale.Crop
        )
        
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
        ) {
            Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen", tint = Color.White)
        }
        
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(64.dp).background(Color.White.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play", modifier = Modifier.size(40.dp))
            }
        }
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "02:14", color = Color.White, fontSize = 12.sp)
            Slider(
                value = 0.3f,
                onValueChange = {},
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = TealPrimary
                )
            )
            Text(text = "06:00", color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun LessonTabs() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Lessons", "Notes", "Resources")
    
    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.Transparent,
        contentColor = TealPrimary,
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(text = title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
            )
        }
    }
}

@Composable
fun LessonListItem(lesson: Lesson, isPlaying: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = lesson.isFree) { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isPlaying) TealPrimary.copy(alpha = 0.1f) else Color.White,
        border = if (isPlaying) androidx.compose.foundation.BorderStroke(1.dp, TealPrimary) else null
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(if (isPlaying) TealPrimary else Color(0xFFF0F0F0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isPlaying) Color.White else TextGray,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    fontWeight = FontWeight.Bold,
                    color = if (isPlaying) TealPrimary else TextDark
                )
                Text(
                    text = if (isPlaying) "Now playing • ${lesson.durationMinutes} min" else "${lesson.durationMinutes} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPlaying) TealPrimary else TextGray
                )
            }
            if (!isPlaying && lesson.isFree) {
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
