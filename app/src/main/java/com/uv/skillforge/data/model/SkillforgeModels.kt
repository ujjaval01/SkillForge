package com.uv.skillforge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SkillforgeResponse(
    val meta: Meta,
    val categories: List<Category>
)

@Serializable
data class Meta(
    val app: String,
    val version: String,
    val generatedAt: String
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val description: String,
    val iconColor: String,
    val courseCount: Int,
    val courses: List<Course>
)

@Serializable
data class Course(
    val id: String,
    val title: String,
    val subtitle: String,
    val thumbnailUrl: String,
    val level: String,
    val durationHours: Double,
    val rating: Double,
    val studentsEnrolled: Int,
    val language: String,
    val lastUpdated: String,
    val tags: List<String>,
    val instructor: Instructor,
    val description: String,
    val lessons: List<Lesson>
) {
    fun isFree() = lessons.any { it.isFree }
}

@Serializable
data class Instructor(
    val id: String,
    val name: String,
    val title: String,
    val avatarUrl: String,
    val bio: String
)

@Serializable
data class Lesson(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val isFree: Boolean,
    val videoUrl: String,
    val content: String
)
