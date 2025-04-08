package com.josycom.mayorjay.holidayinfo.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.josycom.mayorjay.holidayinfo.R
import com.josycom.mayorjay.holidayinfo.data.model.Country
import com.josycom.mayorjay.holidayinfo.ui.ErrorScreen
import com.josycom.mayorjay.holidayinfo.ui.LoadingScreen
import com.josycom.mayorjay.holidayinfo.ui.TopAppBar
import com.josycom.mayorjay.holidayinfo.util.Resource

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel = hiltViewModel(),
    onItemClicked: (String) -> Unit = { _ -> },
) {
    val countryResource by viewModel.uiData.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = { TopAppBar(title = stringResource(R.string.countries)) },
        containerColor = Color.White
    ) { padding ->
        OverviewContent(
            modifier = Modifier.padding(padding),
            countryResource = countryResource,
            onItemClicked = onItemClicked,
            onErrorClicked = { viewModel.getCountries() },
        )
    }
}

@Composable
fun OverviewContent(
    modifier: Modifier = Modifier,
    countryResource: Resource<List<Country>>,
    onItemClicked: (String) -> Unit,
    onErrorClicked: () -> Unit
) {
    when {
        countryResource is Resource.Loading && countryResource.data == null -> LoadingScreen(modifier)
        countryResource.data != null -> CountryList(
            countryList = countryResource.data.orEmpty(),
            modifier = modifier,
            onItemClicked = onItemClicked

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
    onItemClicked: (String) -> Unit
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

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun OverviewScreenPreview() {
    OverviewScreen()
}