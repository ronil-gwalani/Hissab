package org.ronil.hissab.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeHolder: String? = null,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions? = null,
    singleLine: Boolean = true,
) {
    var focus by remember { mutableStateOf(false) }
    modifier
        .fillMaxWidth()
        .border(
            width = if (focus) 1.dp else 0.dp,
            color = if (focus) AppColors.hintColor else Color.Transparent,
            shape = RoundedCornerShape(15.dp),
        )
        .onFocusChanged { focusState ->
            focus = focusState.isFocused
        }
    TextField(
        value = value,
        shape = RoundedCornerShape(15.dp),
        colors = getTextFiledColors(),
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        singleLine = singleLine,
        label = { Text(label ?: "") },
        placeholder = { Text(placeHolder ?: "") },
        modifier = modifier
    )

}

@Composable
fun getTextFiledColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledContainerColor = Color.White,
        disabledTextColor = Color.Gray,
        disabledIndicatorColor = Color.White
    )
}

