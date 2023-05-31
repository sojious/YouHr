package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.YouHrTheme

@Composable
fun ConnectionErrorScreen(modifier: Modifier=Modifier,description:String,resolution:String){
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                color = Color.Black
            )

            Text(
                text =resolution,
                fontSize = 16.sp,
                //fontWeight = FontWeight.Bold,
                modifier = Modifier,
                color = Color.Black.copy(alpha = 0.5f)
            )
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
                resolution = "Keep calm and pull to refresh to try again"
            )
        }
    }
}