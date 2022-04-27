package com.hero.notepad.ui.screens.notes_list_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hero.notepad.common.Constants
import com.hero.notepad.common.UiState
import com.hero.notepad.ui.navigation.Screens
import com.hero.notepad.ui.screens.notes_list_screen.components.NoteItem
import com.hero.notepad.ui.screens.notes_list_screen.ui_events.UiEvent

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
            MyAppBar()
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
                    items(uiState.data!!) { item ->
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
fun MyAppBar() {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onBackground,
        title = {
            Text(text = "Notes", style = MaterialTheme.typography.h5)
        },
        elevation = 16.dp,
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