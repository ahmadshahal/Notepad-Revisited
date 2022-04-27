package com.hero.notepad.ui.screens.notes_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hero.notepad.common.Constants
import com.hero.notepad.common.UiState
import com.hero.notepad.ui.navigation.Screens
import com.hero.notepad.ui.screens.notes_list_screen.components.NoteItem
import com.hero.notepad.common.UiEvent

@Composable
fun NotesListScreen(navController: NavController, viewModel: NotesListViewModel = hiltViewModel()) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackBar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                        actionLabel = uiEvent.action,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.undoDeleteNote()
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { state ->
            SnackbarHost(state) { data ->
                Snackbar(
                    actionColor = MaterialTheme.colors.onBackground,
                    contentColor = MaterialTheme.colors.onBackground,
                    snackbarData = data
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            MyAppBar(viewModel)
        },
        floatingActionButton = {
            FAB(navController = navController)
        }
    ) {
        when (val uiState = viewModel.screenState.value) {
            is UiState.Initial -> Unit
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colors.onBackground,
                    )
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = uiState.message!!,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 12.dp,
                        bottom = 84.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    val list = uiState.data!!.toMutableList()
                    when(viewModel.sortIdx.value) {
                        0 -> list.sortBy { note -> note.title}
                        1 -> list.sortBy { note -> note.color}
                    }
                    items(list) { item ->
                        NoteItem(
                            item,
                            onClick = {
                                navController.navigate(Screens.AddEditNoteScreen + "?${Constants.NOTE_ID_KEY}=${item.id}")
                            },
                            onDeleteClicked = {
                                viewModel.deleteNote(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyAppBar(viewModel: NotesListViewModel) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onBackground,
        title = {
            Text(text = "Notes", style = MaterialTheme.typography.h5)
        },
        elevation = 16.dp,
        actions = {
            IconButton(onClick = { viewModel.dropDownMenuState.value = true }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onBackground
                )
            }
            SortDropDownMenu(viewModel = viewModel)
        }
    )
}

@Composable
fun FAB(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(Screens.AddEditNoteScreen) },
        backgroundColor = MaterialTheme.colors.onBackground,
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "",
            tint = MaterialTheme.colors.primary
        )
    }
}


@Composable
fun SortDropDownMenu(viewModel: NotesListViewModel) {
    DropdownMenu(
        expanded = viewModel.dropDownMenuState.value,
        onDismissRequest = {
            viewModel.dropDownMenuState.value = false
        },
    ) {
        SortDropDownMenuItem(
            sortIdx = 0,
            sortText = "Sort alphabetically",
            viewModel = viewModel
        )
        SortDropDownMenuItem(
            sortIdx = 1,
            sortText = "Sort by color",
            viewModel = viewModel
        )
    }
}

@Composable
fun SortDropDownMenuItem(
    sortIdx: Int,
    sortText: String,
    viewModel: NotesListViewModel
) {
    DropdownMenuItem(
        onClick = {
            viewModel.sortIdx.value = sortIdx
            viewModel.dropDownMenuState.value = false
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colors.onBackground)
            ) {
                if (viewModel.sortIdx.value == sortIdx) {
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colors.primary)
                            .align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = sortText,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}