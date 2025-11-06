package com.kuzepa.mydates.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.di.FakeLabelRepository
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class LabelRepositoryTest {
    private fun getTestLabel(
        id: String = UUID.randomUUID().toString(),
        name: String = "Test Label",
        color: Int = LabelColor.LIGHT_BLUE.id,
        notificationState: NotificationFilterState = NotificationFilterState.FILTER_STATE_ON,
        iconId: Int = LabelIcon.SATISFIED.id
    ): Label {
        return Label(
            id = id,
            name = name,
            color = color,
            notificationState = notificationState,
            iconId = iconId
        )
    }

    @Test
    fun upsertLabelShouldAddNewLabelWhenNotExists() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val newLabel = getTestLabel(name = "Friends")

        // When
        repository.upsertLabel(newLabel)

        // Then
        val result = repository.getAllLabels().first().firstOrNull()
        assertEquals(newLabel, result)
    }

    @Test
    fun upsertLabelShouldUpdateExistingLabelWhenExists() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val labelId = UUID.randomUUID().toString()
        val initialLabel = getTestLabel(id = labelId, name = "Old Name")
        val updatedLabel = getTestLabel(id = labelId, name = "New Name")
        repository.addInitialLabes(initialLabel)

        // When
        repository.upsertLabel(updatedLabel)

        // Then
        val result = repository.getAllLabels().first()
        assertEquals(1, result.size)
        assertEquals("New Name", result[0].name)
        assertEquals(updatedLabel, result[0])
    }

    @Test
    fun deleteLabelByIdShouldRemoveExistingLabel() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val label1 = getTestLabel(id = "1", name = "Label 1")
        val label2 = getTestLabel(id = "2", name = "Label 2")
        repository.addInitialLabes(label1, label2)

        // When
        repository.deleteLabelById("1")

        // Then
        val result = repository.getAllLabels().first()
        assertEquals(1, result.size)
        assertEquals(label2, result[0])
        assertFalse(result.any { it.id == "1" })
    }

    @Test
    fun deleteLabelByIdShouldDoNothingWhenLabelNotFound() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val label = getTestLabel(id = "1", name = "Label 1")
        repository.addInitialLabes(label)
        val initialCount = repository.getAllLabels().first().size

        // When
        repository.deleteLabelById("999") // Non-existent ID

        // Then
        val result = repository.getAllLabels().first()
        assertEquals(initialCount, result.size)
        assertTrue(result.contains(label))
    }

    @Test
    fun getLabelByIdShouldReturnCorrectLabel() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val targetLabel = getTestLabel(id = "target", name = "Target Label")
        val otherLabel = getTestLabel(id = "other", name = "Other Label")
        repository.addInitialLabes(targetLabel, otherLabel)

        // When
        val result = repository.getLabelById("target")

        // Then
        assertEquals(targetLabel, result)
        assertEquals("Target Label", result?.name)
    }

    @Test
    fun getLabelByIdShouldReturnNullWhenLabelNotFound() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val label = getTestLabel(id = "1")
        repository.addInitialLabes(label)

        // When
        val result = repository.getLabelById("999")

        // Then
        assertNull(result)
    }

    @Test
    fun getAllLabelsShouldReturnAllLabelsSortedByName() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val label1 = getTestLabel(id = "1", name = "Charlie")
        val label2 = getTestLabel(id = "2", name = "Alpha")
        val label3 = getTestLabel(id = "3", name = "Bravo")
        repository.addInitialLabes(label1, label2, label3)

        // When
        val result = repository.getAllLabels().first()

        // Then
        assertEquals(3, result.size)
        assertEquals("Alpha", result[0].name)
        assertEquals("Bravo", result[1].name)
        assertEquals("Charlie", result[2].name)
    }

    @Test
    fun getAllLabelsShouldReturnEmptyListWhenNoLabels() = runTest {
        // Given
        val repository = FakeLabelRepository()

        // When
        val result = repository.getAllLabels().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun labelsFlowShouldUpdateWhenLabelsChange() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val initialLabel = getTestLabel(id = "1", name = "Initial")
        repository.addInitialLabes(initialLabel)

        // Get initial state
        val initialResult = repository.getAllLabels().first()
        assertEquals(1, initialResult.size)

        // When - add new label
        val newLabel = getTestLabel(id = "2", name = "New Label")
        repository.upsertLabel(newLabel)

        // Then - get updated state
        val updatedResult = repository.getAllLabels().first()
        assertEquals(2, updatedResult.size)
        assertTrue(updatedResult.any { it.id == "2" })
    }

    @Test
    fun upsertLabelShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val label = getTestLabel()
        val testError = RuntimeException("Test upsert error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.upsertLabel(label)
        }
    }

    @Test
    fun deleteLabelByIdShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val label = getTestLabel()
        repository.addInitialLabes(label)
        val testError = RuntimeException("Test delete error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.deleteLabelById("1")
        }
    }

    @Test
    fun getLabelByIdShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val testError = RuntimeException("Test get by id error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.getLabelById("1")
        }
    }

    @Test
    fun getAllLabelsShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeLabelRepository()
        val testError = RuntimeException("Test get all error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.getAllLabels().first()
        }
    }
}