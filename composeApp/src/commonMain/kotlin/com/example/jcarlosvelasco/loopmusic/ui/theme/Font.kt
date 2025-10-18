package com.example.jcarlosvelasco.loopmusic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import loopmusic.composeapp.generated.resources.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font


@OptIn(ExperimentalResourceApi::class)
@Composable
fun urbanistFontFamily() = FontFamily(
    Font(Res.font.Urbanist_Light, weight = FontWeight.Light),
    Font(Res.font.Urbanist_Regular, weight = FontWeight.Normal),
    Font(Res.font.Urbanist_Medium, weight = FontWeight.Medium),
    Font(Res.font.Urbanist_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.Urbanist_Bold, weight = FontWeight.Bold)
)

@Composable
fun appTypography(): Typography {
    val urbanistFont = urbanistFontFamily()

    return Typography(
        headlineLarge = TextStyle(
            fontFamily = urbanistFont,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = urbanistFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = urbanistFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = urbanistFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodySmall = TextStyle(
            fontFamily = urbanistFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
    )
}
