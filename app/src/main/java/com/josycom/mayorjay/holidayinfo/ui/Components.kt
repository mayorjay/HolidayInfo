package com.josycom.mayorjay.holidayinfo.ui

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.josycom.mayorjay.holidayinfo.R
import com.toptoche.searchablespinnerlibrary.SearchableSpinner

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = colorResource(R.color.colorPrimary))
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(18.dp),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.poppins)),
                fontSize = TextUnit(20f, TextUnitType.Sp)
            )
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = colorResource(R.color.colorPrimary),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onErrorClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.network_error_message),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily(Font(R.font.poppins)))
        )
        Image(
            modifier = Modifier.clickable(onClick = onErrorClicked),
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = "Error image"
        )
    }
}

@Composable
fun YearPopupDialog(
    yearList: List<String>,
    onProceedClicked: (String) -> Unit = { _ -> },
) {
    var yearSelected by remember { mutableStateOf(yearList.first()) }
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .height(200.dp)
            .padding(15.dp)
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.please_select_a_preferred_year_from_the_list),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontSize = TextUnit(18f, TextUnitType.Sp)
                )
            )
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                factory = { context ->
                    SearchableSpinner(context).apply {
                        adapter = ArrayAdapter(
                            context,
                            android.R.layout.simple_spinner_item,
                            yearList
                        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

                        onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                yearSelected = (p0?.getItemAtPosition(p2) as String?).orEmpty()
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) { }
                        }
                    }
                }
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 20.dp), color = Color.Black)
            Button(
                onClick = {
                    onProceedClicked(yearSelected)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.colorPrimary)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.proceed),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontSize = TextUnit(18f, TextUnitType.Sp)
                    )
                )
            }
        }
    }
}