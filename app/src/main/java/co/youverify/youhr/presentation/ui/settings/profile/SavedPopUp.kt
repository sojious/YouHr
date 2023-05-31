package co.youverify.youhr.presentation.ui.settings.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor

@Composable
fun SavedPopUp(modifier: Modifier=Modifier){
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier= Modifier
                    .background(color = Color(0XFFC2E2E9), shape = RoundedCornerShape(4.dp))
                    .height(44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mask_group) ,
                    contentDescription = null,
                    modifier=Modifier.padding(start = 38.dp, end = 7.13.dp)
                )

                Text(
                    text = "Saved!",
                    modifier=Modifier.padding(end = 70.dp),
                    color = bodyTextDeepColor,
                    fontSize = 16.sp
                )
            }
        }
    }
}
@Preview
@Composable
fun SavedPopUpPreview(){
    SavedPopUp()
}