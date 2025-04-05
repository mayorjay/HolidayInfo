package com.josycom.mayorjay.holidayinfo.overview

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.josycom.mayorjay.holidayinfo.R
import com.josycom.mayorjay.holidayinfo.data.model.Country
import com.josycom.mayorjay.holidayinfo.ui.ErrorScreen
import com.josycom.mayorjay.holidayinfo.ui.LoadingScreen
import com.josycom.mayorjay.holidayinfo.ui.TopAppBar
import com.josycom.mayorjay.holidayinfo.util.Resource
import com.toptoche.searchablespinnerlibrary.SearchableSpinner

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel = viewModel(),
    onProceedClicked: (String, String) -> Unit = { _, _ -> }
) {
    val countryResource by viewModel.uiData.collectAsStateWithLifecycle()
    val showPopup by viewModel.showPopup.collectAsStateWithLifecycle()
    val country by viewModel.country.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = { TopAppBar(title = stringResource(R.string.countries)) },
        containerColor = Color.White
    ) { padding ->
        OverviewContent(
            modifier = Modifier.padding(padding),
            countryResource = countryResource,
            yearList = viewModel.populateYearList(),
            showPopup = showPopup,
            selectedCountry = country,
            onItemClicked = { country -> viewModel.updateCountry(country) },
            onErrorClicked = { viewModel.getCountries() },
            onProceedClicked = onProceedClicked,
            onPopupDismissed = { viewModel.updatePopup(false) }
        )
    }
}

@Composable
fun OverviewContent(
    modifier: Modifier = Modifier,
    countryResource: Resource<List<Country>>,
    yearList: List<String>,
    showPopup: Boolean,
    selectedCountry: String,
    onItemClicked: (String) -> Unit,
    onErrorClicked: () -> Unit,
    onProceedClicked: (String, String) -> Unit,
    onPopupDismissed: () -> Unit
) {
    when {
        countryResource is Resource.Loading && countryResource.data == null -> LoadingScreen(modifier)
        countryResource.data != null -> CountryList(
            countryList = countryResource.data.orEmpty(),
            modifier = modifier,
            onItemClicked = onItemClicked,
            showPopup = showPopup,
            yearList = yearList,
            selectedCountry = selectedCountry,
            onProceedClicked = onProceedClicked,
            onPopupDismissed = onPopupDismissed

        )
        countryResource is Resource.Error && countryResource.data == null -> ErrorScreen(
            modifier = modifier,
            onErrorClicked = onErrorClicked
        )
    }
}

@Composable
private fun CountryList(
    countryList: List<Country>,
    modifier: Modifier = Modifier,
    showPopup: Boolean,
    yearList: List<String>,
    selectedCountry: String,
    onItemClicked: (String) -> Unit,
    onProceedClicked: (String, String) -> Unit,
    onPopupDismissed: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        state = rememberLazyListState()
    ) {
        items(countryList) { countryItem ->
            CountryItem(
                modifier = Modifier.fillParentMaxWidth(),
                country = countryItem,
                onItemClicked = onItemClicked
            )
        }
    }
    if (showPopup) {
        YearPopupDialog(
            yearList = yearList,
            country = selectedCountry,
            onProceedClicked = onProceedClicked,
            onPopupDismissed = onPopupDismissed
        )
    }
}

@Composable
fun CountryItem(
    modifier: Modifier = Modifier,
    country: Country,
    onItemClicked: (String) -> Unit,
) {
    ElevatedCard(
        onClick = { onItemClicked("${country.name}:${country.code}") },
        modifier = modifier
            .height(height = 80.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(7.dp),
        colors = CardDefaults.elevatedCardColors().copy(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(5.dp),
                text = stringResource(R.string.name, country.name),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontSize = TextUnit(18f, TextUnitType.Sp)
                )
            )
            Text(
                modifier = Modifier.padding(5.dp),
                text = stringResource(R.string.code, country.code),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontSize = TextUnit(18f, TextUnitType.Sp)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearPopupDialog(
    yearList: List<String>,
    country: String,
    onProceedClicked: (String, String) -> Unit,
    onPopupDismissed: () -> Unit
) {
    var yearSelected by remember { mutableStateOf(yearList.first()) }
    BasicAlertDialog(
        onDismissRequest = onPopupDismissed,
        modifier = Modifier
            .background(color = Color.White)
            .height(200.dp)
            .padding(15.dp)
    ) {
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
                    onProceedClicked(yearSelected, country)
                    onPopupDismissed()
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

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun OverviewScreenPreview() {
    OverviewScreen()
}