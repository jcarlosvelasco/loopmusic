import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShimmerButtonSkeleton(
    modifier: Modifier = Modifier,
    width: Dp = 120.dp,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
    shape: Shape = RoundedCornerShape(20.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    val density = LocalDensity.current

    val buttonHeight = remember(textStyle, contentPadding) {
        with(density) {
            val fontSize = textStyle.fontSize
            val lineHeight = textStyle.lineHeight.takeIf { it != TextUnit.Unspecified } ?: (fontSize * 1.2f)
            val textHeight = lineHeight.toDp()

            val verticalPadding = contentPadding.calculateTopPadding() + contentPadding.calculateBottomPadding()

            textHeight + verticalPadding
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE0E0E0),
            Color(0xFFF5F5F5),
            Color(0xFFE0E0E0)
        ),
        start = Offset(shimmerTranslate, 0f),
        end = Offset(shimmerTranslate + 200f, 0f)
    )

    Box(
        modifier = modifier
            .width(width)
            .height(buttonHeight)
            .background(
                brush = shimmerBrush,
                shape = shape
            )
    )
}