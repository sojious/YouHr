package co.youverify.youhr.presentation.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.primaryColor
import co.youverify.youhr.presentation.ui.theme.yvColor
import co.youverify.youhr.presentation.ui.theme.yvColor1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(modifier: Modifier = Modifier, onSplashDisplayed: () -> Unit){


    //Show Splash Screen for 1.5s
    LaunchedEffect(key1 = true ){
        delay(1500L)
        onSplashDisplayed()
    }


    Column(
        modifier= modifier
            .fillMaxSize()
            .background(color = primaryColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier= Modifier
                .size(120.dp, 68.64.dp)
                .padding(bottom = 16.dp),
            painter = painterResource(id = R.drawable.youhr_logo),
            contentDescription ="YHLogo",
            //contentScale = ContentScale.Fit
        )

        Image(painter = painterResource(id = R.drawable.component_1), contentDescription ="LoadingBar" )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier=Modifier){
    Box(modifier=modifier.fillMaxSize().background(color = Color.White)) {}
}

@Preview
@Composable
fun SplashScreenPreview(){
    SplashScreen { }
}