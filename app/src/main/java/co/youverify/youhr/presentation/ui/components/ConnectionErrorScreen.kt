package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.yvColor
import co.youverify.youhr.presentation.ui.theme.yvColor1

@Composable
fun ConnectionErrorScreen(
    modifier: Modifier = Modifier,
    description: String,
    //resolution: String,
    onRetryButtonClicked:()->Unit

){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            Image(
                painter = painterResource(id = R.drawable.ic_no_internet) ,
                contentDescription = null,

                )
            Text(
                text =description,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                color = Color.Black
            )

            /*Text(
                text =resolution,
                fontSize = 16.sp,
                //fontWeight = FontWeight.Bold,
                modifier = Modifier,
                color = Color.Black.copy(alpha = 0.5f)
            )*/
            androidx.compose.material3.OutlinedButton(
                onClick = { onRetryButtonClicked() },
                border = BorderStroke(width = 1.dp, color = yvColor),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = yvColor)
            ) {
                Text(
                text ="Retry",
                fontSize = 20.sp,
                //fontWeight = FontWeight.Bold,
                //modifier = Modifier,
                //color = yvColor1
                )
            }
        }

    }
}

@Preview
@Composable
fun ConnectionErrorScreenPreview(){
    YouHrTheme {
        Surface{
            ConnectionErrorScreen(
                description = "Ooops, your connections seems off...",
                //resolution = "Keep calm and pull to refresh to try again",
            onRetryButtonClicked = {}
            )
        }
    }
}