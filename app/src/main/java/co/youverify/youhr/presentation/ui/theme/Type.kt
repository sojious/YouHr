package co.youverify.youhr.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R

// Set of Material typography styles to start with

val sPProTextFont= FontFamily(
    Font(R.font.sf_pro_text_regular, weight = FontWeight.Normal),
    Font(R.font.sf_pro_text_medium, weight = FontWeight.Medium),
    Font(R.font.sf_pro_text_semibold, weight = FontWeight.SemiBold)
)


val sPProRoundedFont= FontFamily(
    Font(R.font.sf_pro_rounded_regular, weight = FontWeight.Normal),
    Font(R.font.sf_pro_rounded_bold, weight = FontWeight.Bold),
    Font(R.font.sf_pro_rounded_semibold, weight = FontWeight.SemiBold),
    Font(R.font.sf_pro_rounded_heavy, weight = FontWeight.ExtraBold)
)

val brSonomaFont= FontFamily(

    //Font(R.font.br_sonoma_light_italic,FontWeight.Light, FontStyle.Italic),
    //Font(R.font.br_sonoma_bold,FontWeight.Bold),
    //Font(R.font.br_sonoma_bold_italic,FontWeight.Bold, FontStyle.Italic),
    //Font(R.font.br_sonoma_extra_light,FontWeight.ExtraLight),
    //Font(R.font.br_sonoma_extra_light_italic,FontWeight.ExtraLight, FontStyle.Italic),
    //Font(R.font.br_sonoma_light,FontWeight.Light),
    //Font(R.font.br_sonoma_light_italic,FontWeight.Light, FontStyle.Italic),
    //Font(R.font.br_sonoma_medium,FontWeight.Medium),
    //Font(R.font.br_sonoma_medium_italic,FontWeight.Medium, FontStyle.Italic),
    Font(R.font.br_sonoma_regular,FontWeight.Normal),
    //Font(R.font.br_sonoma_regular_italic,FontWeight.Normal, FontStyle.Italic),
    //Font(R.font.br_sonoma_semi_bold,FontWeight.SemiBold),
    //Font(R.font.br_sonoma_semi_bold_italic,FontWeight.SemiBold, FontStyle.Italic),
    //Font(R.font.br_sonoma_thin,FontWeight.Thin),
    //Font(R.font.br_sonoma_thin_italic,FontWeight.Thin, FontStyle.Italic),
)


val Typography = Typography(

    displayLarge = TextStyle(
        fontFamily = brSonomaFont,
    ),

    displayMedium = TextStyle(
        fontFamily = brSonomaFont,
    ),
    displaySmall = TextStyle(
        fontFamily = brSonomaFont,
    ),
    headlineLarge = TextStyle(
        fontFamily = brSonomaFont,
    ),
    headlineMedium = TextStyle(
        fontFamily = brSonomaFont,
    ),
    headlineSmall = TextStyle(
        fontFamily = brSonomaFont,
    ),

    titleLarge = TextStyle(
        fontFamily = brSonomaFont,
    ),
    titleMedium = TextStyle(
        fontFamily = brSonomaFont,
    ),

    titleSmall = TextStyle(
        fontFamily = brSonomaFont,
    ),
    bodyLarge = TextStyle(
        fontFamily = brSonomaFont,
    ),
    bodyMedium = TextStyle(
        fontFamily = brSonomaFont,
    ),
    bodySmall = TextStyle(
        fontFamily = brSonomaFont,
    ),
    labelLarge = TextStyle(
        fontFamily = brSonomaFont,
    ),
    labelMedium = TextStyle(
        fontFamily = brSonomaFont,
    ),
    labelSmall = TextStyle(
        fontFamily = brSonomaFont,
    )
)