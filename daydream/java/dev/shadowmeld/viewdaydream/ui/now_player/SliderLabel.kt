package dev.shadowmeld.viewdaydream.ui.now_player

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 音乐播放进度条
 */
@Composable
fun SliderWithLabel(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    finiteEnd: Boolean,
    labelMinWidth: Dp = 24.dp,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    /*@IntRange(from = 0)*/
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors()
) {
    Column {
        BoxWithConstraints(
            modifier = modifier
        ) {
            val offset = getSliderOffset(
                value = value,
                valueRange = valueRange,
                boxWidth = maxWidth,
                labelWidth = labelMinWidth + 8.dp
            )

            val endValueText = if (!finiteEnd && value >= valueRange.endInclusive)
                "${value.toInt()} +" else value.toInt().toString()

            SliderTextLabel(label = valueRange.start.toInt().toString(), minWidth = labelMinWidth)

            if (value > valueRange.start) {
                SliderLabel(
                    label = endValueText,
                    minWidth = labelMinWidth,
                    modifier = Modifier
                        .padding(start = offset)
                )
            }
        }

        Slider(
            modifier= modifier,
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            enabled = enabled,
            steps = steps,
            onValueChangeFinished = onValueChangeFinished,
            interactionSource = interactionSource,
            colors = colors
        )
    }
}

@Composable
fun SliderLabel(
    label: String,
    minWidth: Dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .defaultMinSize(minWidth = minWidth)
    )
}

private fun getSliderOffset(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    boxWidth: Dp,
    labelWidth: Dp
): Dp {
    val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val positionFraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)

    return (boxWidth - labelWidth) * positionFraction
}

@Composable
fun SliderTextLabel(
    label: String,
    minWidth: Dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(4.dp)
            .defaultMinSize(minWidth = minWidth)
    )
}

private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f,1f)
