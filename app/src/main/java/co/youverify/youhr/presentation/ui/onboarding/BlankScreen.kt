package co.youverify.youhr.presentation.ui.onboarding


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun BlankScreen(modifier: Modifier = Modifier, navigate: () -> Unit){


        LaunchedEffect(key1 = true ){
            delay(300L)
            navigate()
        }

        Surface(Modifier.fillMaxSize(), color = Color.White) {}

}