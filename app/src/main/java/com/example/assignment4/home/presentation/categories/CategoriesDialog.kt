package com.example.assignment4.home.presentation.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.data.models.Artwork
import com.example.assignment4.home.data.models.GeneCategory
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDialog(
    onDismiss: () -> Unit,
    artwork: Artwork,
    sharedViewModel: SharedViewModel
) {
    var (loader, setLoader) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        setLoader(true)
        try {
            val response = RetrofitInstance.artsyApi.getArtistGenes(artwork.id)
            if (response.isSuccessful) {
                sharedViewModel.artworkCategories.value = response.body()!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            setLoader(false)
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        text = {
            if (loader) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 100.dp)
                    )
                }
            } else if (sharedViewModel.artworkCategories.value.isEmpty()) {
                Text(
                    text = "No categories available",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                LoopingCarousel(sharedViewModel.artworkCategories.value)
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                )
            ) {
                Text("Close", color = MaterialTheme.colorScheme.onTertiary)
            }
        }
    )
}

@Composable
fun CarouselCard(
    title: String,
    description: String,
    imageUrl: String
) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .height(450.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun LoopingCarousel(geneList: List<GeneCategory>) {
    var startIndex = 0

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            items(
                count = geneList.size,
            ) { index ->
                val gene = geneList[index]
                CarouselCard(
                    title = gene.name,
                    description = gene.description,
                    imageUrl = gene.links.thumbnail.href
                )
            }
        }

        IconButton(
            onClick = {
                coroutineScope.launch {
                    startIndex = (startIndex - 1)
                    if (startIndex == -1)
                        startIndex = geneList.lastIndex
                    listState.animateScrollToItem(startIndex)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-35).dp)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Scroll Left",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }


        IconButton(
            onClick = {
                coroutineScope.launch {
                    startIndex = (startIndex + 1)
                    if (startIndex > geneList.lastIndex)
                        startIndex = 0
                    listState.animateScrollToItem(startIndex)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 35.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Scroll Right",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


