package com.example.mygamecolor.ui.screens

import com.example.mygamecolor.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygamecolor.model.GameColor
import com.example.mygamecolor.viewmodel.Feedback
import com.example.mygamecolor.viewmodel.GameState
import com.example.mygamecolor.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel, onGameFinished: () -> Unit) {
    val currentColor by viewModel.currentColor.collectAsState()
    val score by viewModel.score.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val gameState by viewModel.gameState.collectAsState()
    val feedback by viewModel.feedback.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startGame()
    }

    LaunchedEffect(gameState) {
        if (gameState == GameState.FINISHED) {
            onGameFinished()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoCard(label = stringResource(R.string.score_label), value = "$score")
                TimerCard(timeLeft = timeLeft)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(currentColor.color, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentColor.displayName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            val (feedbackText, feedbackColor) = when (feedback) {
                Feedback.CORRECT -> "¡Correcto!" to Color(0xFF4CAF50)
                Feedback.WRONG -> "¡Incorrecto!" to Color(0xFFF44336)
                Feedback.NONE -> "" to Color.Transparent
            }

            Text(
                text = feedbackText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = feedbackColor
            )

            ColorButtonGrid(
                colors = GameColor.values().toList(),
                onColorSelected = { viewModel.onColorSelected(it) }
            )
        }
    }
}

@Composable
fun InfoCard(label: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 13.sp)
            Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TimerCard(timeLeft: Int) {
    val bgColor = when {
        timeLeft > 15 -> MaterialTheme.colorScheme.primaryContainer
        timeLeft > 5 -> Color(0xFFFFF9C4)
        else -> Color(0xFFFFCDD2)
    }
    Card(colors = CardDefaults.cardColors(containerColor = bgColor)) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "⏱ Tiempo", fontSize = 13.sp)
            Text(text = "${timeLeft}s", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ColorButtonGrid(colors: List<GameColor>, onColorSelected: (GameColor) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        colors.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { gameColor ->
                    Button(
                        onClick = { onColorSelected(gameColor) },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = gameColor.color),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = gameColor.displayName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}