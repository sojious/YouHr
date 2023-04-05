package co.youverify.youhr.presentation.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.primaryColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    prefViewModel: PreferencesViewModel,

    ){


    //Show Splash Screen for 1.5s
    LaunchedEffect(key1 = true ){
        delay(1500L)
        prefViewModel.navigateToAppropriateScreen()
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
    Box(modifier= modifier
        .fillMaxSize()
        .background(color = Color.White)) {}
}

@Preview
@Composable
fun SplashScreenPreview(){
    SplashScreen(
        prefViewModel = hiltViewModel()
    )
}

