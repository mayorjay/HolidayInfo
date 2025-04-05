package com.josycom.mayorjay.holidayinfo.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.josycom.mayorjay.holidayinfo.R
import com.josycom.mayorjay.holidayinfo.data.model.Holiday
import com.josycom.mayorjay.holidayinfo.data.remote.models.HolidayRequest
import com.josycom.mayorjay.holidayinfo.ui.ErrorScreen
import com.josycom.mayorjay.holidayinfo.ui.LoadingScreen
import com.josycom.mayorjay.holidayinfo.ui.TopAppBar
import com.josycom.mayorjay.holidayinfo.util.Resource
import com.josycom.mayorjay.holidayinfo.util.getFormattedDate
import com.josycom.mayorjay.holidayinfo.util.getJoinedString
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = viewModel(),
    country: String,
    year: String
) {
    val holidayResource by viewModel.uiData.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getHolidays(HolidayRequest(country.substringAfter(":"), year))
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = { TopAppBar(title = stringResource(R.string.holidays, country.substringBefore(":"))) },
        containerColor = Color.White
    ) { padding ->
        DetailsContent(
            modifier = Modifier.padding(padding),
            holidayResource = holidayResource,
            onErrorClicked = { viewModel.getHolidays(HolidayRequest(country.substringAfter(":"), year)) }
        )
    }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    holidayResource: Resource<List<Holiday>>,
    onErrorClicked: () -> Unit,
) {
    when (holidayResource) {
        is Resource.Loading -> LoadingScreen(modifier)
        is Resource.Success -> HolidayList(
            holidayList = holidayResource.data.orEmpty(),
            modifier = modifier
        )
        is Resource.Error -> ErrorScreen(
            modifier = modifier,
            onErrorClicked = onErrorClicked
        )
    }
}

@Composable
private fun HolidayList(
    holidayList: List<Holiday>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        state = rememberLazyListState()
    ) {
        items(holidayList) { holidayItem ->
            HolidayItem(
                modifier = Modifier.fillParentMaxWidth(),
                holiday = holidayItem
            )
        }
    }
}

@Composable
fun HolidayItem(
    modifier: Modifier = Modifier,
    holiday: Holiday,
) {
    ElevatedCard(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .wrapContentHeight()
            .padding(4.dp),
        shape = RoundedCornerShape(7.dp),
        colors = CardDefaults.elevatedCardColors().copy(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(5.dp),
                text = stringResource(R.string.name, holiday.name),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontSize = TextUnit(18f, TextUnitType.Sp)
                )
            )

            val date = holiday.date.getFormattedDate(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()), SimpleDateFormat("MMM dd yyyy", Locale.getDefault()))
            Text(
                modifier = Modifier.padding(5.dp),
                text = stringResource(R.string.date, date),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontSize = TextUnit(18f, TextUnitType.Sp)
                )
            )

            val types = holiday.types.getJoinedString()
            if (types.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = stringResource(R.string.type, types),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontSize = TextUnit(18f, TextUnitType.Sp)
                    )
                )
            }

            val regions = holiday.regions.getJoinedString()
            if (regions.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = stringResource(R.string.region, regions),
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
fun DetailsScreenPreview() {
    DetailsScreen(year = "", country = "")
}