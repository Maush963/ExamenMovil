package com.example.examen3.presentation.screens.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.examen3.domain.model.Difficulty

@Composable
fun DifficultySelector(
    currentDifficulty: Difficulty,
    onDifficultySelected: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DifficultyOption(
            text = "Fácil",
            selected = currentDifficulty == Difficulty.EASY,
            onClick = { onDifficultySelected(Difficulty.EASY) }
        )

        Spacer(modifier = Modifier.width(8.dp))

        DifficultyOption(
            text = "Medio",
            selected = currentDifficulty == Difficulty.MEDIUM,
            onClick = { onDifficultySelected(Difficulty.MEDIUM) }
        )

        Spacer(modifier = Modifier.width(8.dp))

        DifficultyOption(
            text = "Difícil",
            selected = currentDifficulty == Difficulty.HARD,
            onClick = { onDifficultySelected(Difficulty.HARD) }
        )
    }
}

@Composable
private fun DifficultyOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}