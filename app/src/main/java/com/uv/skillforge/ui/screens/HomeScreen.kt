package com.uv.skillforge.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.uv.skillforge.data.model.Category
import com.uv.skillforge.data.model.Course
import com.uv.skillforge.ui.SkillforgeUiState
import com.uv.skillforge.ui.SkillforgeViewModel
import com.uv.skillforge.ui.theme.BeginnerGreen
import com.uv.skillforge.ui.theme.IntermediateOrange
import com.uv.skillforge.ui.theme.StarYellow
import com.uv.skillforge.ui.theme.TealPrimary
import com.uv.skillforge.ui.theme.TextGray

@Composable
fun HomeScreen(
    viewModel: SkillforgeViewModel,
    onCourseClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { HomeTopBar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is SkillforgeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SkillforgeUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                is SkillforgeUiState.Success -> {
                    HomeContent(state.data.categories, onCourseClick)
                }
            }
        }
    }
}

@Composable
fun HomeTopBar() {
    Column(modifier = Modifier.padding(top = 28.dp, start = 16.dp, end = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome back", 
                    style = MaterialTheme.typography.bodyMedium, 
                    color = TextGray
                )
                Text(
                    text = "Find your next skill", 
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.Black
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    onClick = { /* TODO */ },
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.Notifications, 
                            contentDescription = "Notifications",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TealPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AS", 
                        color = Color.White, 
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search courses, topics...", color = TextGray) },
            leadingIcon = { 
                Icon(
                    Icons.Default.Search, 
                    contentDescription = "Search", 
                    tint = TextGray,
                    modifier = Modifier.size(20.dp)
                ) 
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun HomeContent(categories: List<Category>, onCourseClick: (String) -> Unit) {
    val allCourses = categories.flatMap { it.courses }.distinctBy { it.id }
    val categoryLazyListState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "Categories", 
                onSeeAllClick = {}, 
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                state = categoryLazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(category)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomScrollIndicator(
                state = categoryLazyListState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SectionHeader(
                title = "Popular courses", 
                onSeeAllClick = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(allCourses) { course ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                CourseItem(course, onClick = { onCourseClick(course.id) })
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CustomScrollIndicator(state: LazyListState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    
    val scrollProgress by remember(state) {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                0f
            } else {
                val totalItemsCount = layoutInfo.totalItemsCount
                val firstVisibleItem = visibleItemsInfo.first()
                val itemSize = firstVisibleItem.size.toFloat()
                if (itemSize == 0f) {
                    0f
                } else {
                    val spacing = if (visibleItemsInfo.size > 1) {
                        (visibleItemsInfo[1].offset - visibleItemsInfo[0].offset - firstVisibleItem.size).toFloat()
                    } else {
                        0f
                    }
                    val beforePadding = layoutInfo.beforeContentPadding.toFloat()
                    val afterPadding = layoutInfo.afterContentPadding.toFloat()
                    val viewportWidth = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset).toFloat()
                    
                    val totalContentWidth = totalItemsCount * itemSize + 
                            (totalItemsCount - 1).coerceAtLeast(0) * spacing + 
                            beforePadding + afterPadding
                    
                    val maxScrollRange = totalContentWidth - viewportWidth
                    if (maxScrollRange <= 0f) {
                        0f
                    } else {
                        val currentScroll = firstVisibleItem.index * (itemSize + spacing) + (beforePadding - firstVisibleItem.offset)
                        (currentScroll / maxScrollRange).coerceIn(0f, 1f)
                    }
                }
            }
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                coroutineScope.launch {
                    val targetIndex = (state.firstVisibleItemIndex - 1).coerceAtLeast(0)
                    state.animateScrollToItem(targetIndex)
                }
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowLeft, 
                contentDescription = null, 
                tint = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(CircleShape)
                .background(Color(0xFFEEEEEE))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.4f) // Thumb width
                    .align(Alignment.CenterStart)
                    .graphicsLayer {
                        translationX = (size.width / 0.4f) * 0.6f * scrollProgress
                    }
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                coroutineScope.launch {
                    val targetIndex = (state.firstVisibleItemIndex + 1).coerceAtMost(state.layoutInfo.totalItemsCount - 1)
                    state.animateScrollToItem(targetIndex)
                }
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowRight, 
                contentDescription = null, 
                tint = Color.Gray
            )
        }
    }
}
@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, 
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            ),
            color = Color.Black
        )
        TextButton(onClick = onSeeAllClick) {
            Text(
                text = "See all", 
                color = TealPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(android.graphics.Color.parseColor(category.iconColor)).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(android.graphics.Color.parseColor(category.iconColor)))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = category.name, 
                fontWeight = FontWeight.ExtraBold, 
                maxLines = 2,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${category.courseCount} courses", 
                style = MaterialTheme.typography.bodySmall, 
                color = TextGray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CourseThumbnail(course: Course, modifier: Modifier = Modifier) {
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
        modifier = modifier
            .background(Brush.linearGradient(colors = gradientColors))
            .padding(10.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = size.minDimension * 0.7f,
                center = Offset(size.width * 0.9f, size.height * 0.3f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.08f),
                radius = size.minDimension * 0.4f,
                center = Offset(size.width * 0.1f, size.height * 0.8f)
            )
        }

        Text(
            text = course.title,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 13.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.95f)
        )
    }
}

@Composable
fun CourseItem(course: Course, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            CourseThumbnail(
                course = course,
                modifier = Modifier
                    .width(130.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                val levelColor = when (course.level.lowercase()) {
                    "beginner" -> Color(0xFF2DD4BF)
                    "intermediate" -> Color(0xFFF59E0B)
                    else -> Color(0xFF2DD4BF)
                }
                Text(
                    text = course.level.uppercase(),
                    color = levelColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = course.title,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    color = Color.Black
                )
                Text(
                    text = course.instructor.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFAAAAAA),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star, 
                        contentDescription = null, 
                        tint = StarYellow, 
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = course.rating.toString(), 
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null, 
                        tint = Color(0xFFAAAAAA), 
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${course.durationHours}h", 
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
