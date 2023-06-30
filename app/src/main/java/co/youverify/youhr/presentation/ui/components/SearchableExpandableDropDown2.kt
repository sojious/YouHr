package co.youverify.youhr.presentation.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import co.youverify.youhr.R
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.presentation.ui.leave.LeaveType
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import co.youverify.youhr.presentation.ui.theme.primaryColor

@Composable
fun SearchableExpandableDropDownMenu2(
    modifier: Modifier = Modifier,
    listOfItems: List<FilteredUser>,
    //enable: Boolean = false,
    //placeholder: String = "Select Option",
    openedIcon: ImageVector = Icons.Outlined.KeyboardArrowUp,
    closedIcon: ImageVector = Icons.Outlined.KeyboardArrowDown,
    //parentTextFieldCornerRadius: Dp = 12.dp,
    //colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    onDropDownItemSelected: (FilteredUser) -> Unit = {},
    selectedOption:String
) {

    var selectedOptionText by rememberSaveable { mutableStateOf(selectedOption) }
    var searchedOption by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var filteredItems = mutableListOf<FilteredUser>()
    var parentTextFieldSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val borderColor=if (selectedOptionText.isEmpty()) deactivatedColorDeep else primaryColor
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .height(47.dp)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .onGloballyPositioned { coordinates ->
                    parentTextFieldSize = coordinates.size.toSize()
                },

            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedOptionText, fontSize = 12.sp, color = bodyTextDeepColor,modifier=Modifier.padding(start = 12.dp))
            Spacer(modifier = Modifier.weight(1f))
            //Image(painter = painterResource(id = R.drawable.ic_arrow_down), contentDescription =null,modifier=Modifier.padding(end=20.dp))
            Icon(imageVector = if (expanded) openedIcon else closedIcon, contentDescription = null,modifier=Modifier.padding(end=20.dp))
        }

        if (expanded) {
            Card(
                modifier = modifier.width(with(LocalDensity.current) {
                    parentTextFieldSize.width.toDp()
                })
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = searchedOption,
                        onValueChange = { selectedSport ->
                            searchedOption = selectedSport

                            filteredItems = listOfItems.filter {
                                val name="${it.firstName} ${it.lastName}"
                                name.contains(
                                    searchedOption,
                                    ignoreCase = true
                                )
                            }.toMutableList()
                        },
                        leadingIcon = {
                           Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                        },
                        placeholder = {
                            Text(text = "Search by name")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedPlaceholderColor = deactivatedColorDeep,
                            unfocusedPlaceholderColor = deactivatedColorDeep,
                            unfocusedBorderColor = primaryColor.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontSize = 12.sp, color = bodyTextDeepColor),
                    )

                    val items = if (filteredItems.isEmpty()) {
                        listOfItems
                    } else {
                        filteredItems
                    }

                    Column(
                        //modifier=Modifier.verticalScroll(rememberScrollState())
                    ) {
                        items.forEach { selectedItem ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOptionText = "${selectedItem.firstName} ${selectedItem.lastName}"
                                    onDropDownItemSelected(selectedItem)
                                    searchedOption = ""
                                    expanded = false
                                },
                                content = {
                                    Text(text = "${selectedItem.firstName} ${selectedItem.lastName}", color = bodyTextDeepColor)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}