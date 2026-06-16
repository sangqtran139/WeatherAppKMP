package com.sangtq.weatherappkmp.ui.astronomy

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.absoluteValue
import kotlin.random.Random

private val moonLitGradient = listOf(
    Color(0xFFFFF7DA),
    Color(0xFFEDE0AC),
    Color(0xFFD9C77E)
)
private val moonDarkColor = Color(0xFF1B1B3A)
private val moonHaloColor = Color(0xFFFFE9A0).copy(alpha = 0.18f)

@Composable
fun MoonIllustration(
    phaseName: String,
    illumination: Int,
    modifier: Modifier = Modifier
) {
    val isWaxing = phaseName.contains("waxing", ignoreCase = true) ||
        phaseName.contains("first quarter", ignoreCase = true) ||
        phaseName.contains("new", ignoreCase = true)
    val frac = (illumination.coerceIn(0, 100)) / 100f

    Canvas(modifier = modifier) {
        val r = size.minDimension / 2f * 0.78f
        val cx = size.width / 2f
        val cy = size.height / 2f

        // Soft glow halo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(moonHaloColor, Color.Transparent),
                center = Offset(cx, cy),
                radius = r * 1.9f
            ),
            radius = r * 1.9f,
            center = Offset(cx, cy)
        )

        // Dark moon base (the unlit side, always drawn)
        drawCircle(
            color = moonDarkColor,
            radius = r,
            center = Offset(cx, cy)
        )

        if (frac <= 0.005f) {
            drawCraters(cx, cy, r, alpha = 0.15f)
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = r,
                center = Offset(cx, cy),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = r * 0.02f)
            )
            return@Canvas
        }

        val circleRect = Rect(cx - r, cy - r, cx + r, cy + r)
        val rx = r * (1f - 2f * frac).absoluteValue
        val ellipseRect = Rect(cx - rx, cy - r, cx + rx, cy + r)

        val litPath = Path().apply {
            moveTo(cx, cy - r)
            if (frac <= 0.5f) {
                if (isWaxing) {
                    arcTo(circleRect, 270f, 180f, false)
                    arcTo(ellipseRect, 90f, -180f, false)
                } else {
                    arcTo(circleRect, 270f, -180f, false)
                    arcTo(ellipseRect, 90f, 180f, false)
                }
            } else {
                if (isWaxing) {
                    arcTo(circleRect, 270f, 180f, false)
                    arcTo(ellipseRect, 90f, 180f, false)
                } else {
                    arcTo(circleRect, 270f, -180f, false)
                    arcTo(ellipseRect, 90f, -180f, false)
                }
            }
            close()
        }

        drawPath(
            path = litPath,
            brush = Brush.radialGradient(
                colors = moonLitGradient,
                center = Offset(cx, cy),
                radius = r * 1.1f
            )
        )

        drawCraters(cx, cy, r, alpha = 0.18f)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCraters(
    cx: Float,
    cy: Float,
    r: Float,
    alpha: Float
) {
    val rng = Random(42)
    repeat(8) {
        val angle = rng.nextDouble(0.0, 2 * kotlin.math.PI)
        val dist = rng.nextDouble(0.0, 0.78) * r
        val cr = (rng.nextDouble(0.04, 0.14) * r).toFloat()
        val x = cx + (kotlin.math.cos(angle) * dist).toFloat()
        val y = cy + (kotlin.math.sin(angle) * dist).toFloat()
        drawCircle(
            color = Color.Black.copy(alpha = alpha),
            radius = cr,
            center = Offset(x, y)
        )
    }
}

@Composable
fun StarrySky(modifier: Modifier = Modifier, starCount: Int = 60) {
    Canvas(modifier = modifier) {
        val rng = Random(7)
        repeat(starCount) {
            val x = rng.nextFloat() * size.width
            val y = rng.nextFloat() * size.height * 0.85f
            val sr = (rng.nextFloat() * 1.4f + 0.4f)
            val a = rng.nextFloat() * 0.7f + 0.3f
            drawCircle(
                color = Color.White.copy(alpha = a),
                radius = sr,
                center = Offset(x, y)
            )
        }
    }
}
