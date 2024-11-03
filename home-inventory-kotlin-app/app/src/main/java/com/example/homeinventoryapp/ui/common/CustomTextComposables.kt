package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextBold(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier
            .padding(top = 30.dp),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp
    )
}

@Composable
fun CustomText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .padding(top = 35.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    )
}
