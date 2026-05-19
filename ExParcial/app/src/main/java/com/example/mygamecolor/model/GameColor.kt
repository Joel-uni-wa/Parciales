package com.example.mygamecolor.model

import androidx.compose.ui.graphics.Color

enum class GameColor(val displayName: String, val color: Color) {
    RED("Rojo", Color(0xFFF44336)),
    GREEN("Verde", Color(0xFF4CAF50)),
    BLUE("Azul", Color(0xFF2196F3)),
    YELLOW("Amarillo", Color(0xFFFFEB3B)),
    PURPLE("Morado", Color(0xFF9C27B0)),
    ORANGE("Naranja", Color(0xFFFF7A22))
}