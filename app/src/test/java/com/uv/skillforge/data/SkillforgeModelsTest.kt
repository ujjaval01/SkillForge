package com.uv.skillforge.data

import com.uv.skillforge.data.model.Course
import com.uv.skillforge.data.model.Instructor
import com.uv.skillforge.data.model.Lesson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SkillforgeModelsTest {

    @Test
    fun `course isFree returns true when at least one lesson is free`() {
        val lessons = listOf(
            Lesson("1", "L1", 10, true, "", ""),
            Lesson("2", "L2", 10, false, "", "")
        )
        val course = createMockCourse(lessons)
        
        assertTrue(course.isFree())
    }

    @Test
    fun `course isFree returns false when no lessons are free`() {
        val lessons = listOf(
            Lesson("1", "L1", 10, false, "", ""),
            Lesson("2", "L2", 10, false, "", "")
        )
        val course = createMockCourse(lessons)
        
        assertFalse(course.isFree())
    }

    private fun createMockCourse(lessons: List<Lesson>): Course {
        return Course(
            id = "1",
            title = "Title",
            subtitle = "Subtitle",
            thumbnailUrl = "",
            level = "Beginner",
            durationHours = 1.0,
            rating = 4.5,
            studentsEnrolled = 100,
            language = "English",
            lastUpdated = "",
            tags = emptyList(),
            instructor = Instructor("", "", "", "", ""),
            description = "",
            lessons = lessons
        )
    }
}
