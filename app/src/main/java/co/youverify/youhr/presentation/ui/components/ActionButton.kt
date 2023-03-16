package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.youverify.youhr.presentation.ui.theme.yvColor

@Composable
fun ActionButton(modifier:Modifier=Modifier,text:String,onButtonClicked:()-> Unit){
    Button(
        onClick = onButtonClicked,
        shape= RoundedCornerShape(10.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = yvColor)
    ) {
        Text(text = text, color = Color.White)
    }

}