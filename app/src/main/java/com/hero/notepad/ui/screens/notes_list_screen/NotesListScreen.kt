package com.hero.notepad.ui.screens.notes_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
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

@Composable
fun NotesListScreen(navController: NavController, viewModel: NotesListViewModel = hiltViewModel()) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            MyAppBar()
        },
        floatingActionButton = {
            FAB(navController = navController)
        }
    ) {
        when(viewModel.screenState.value) {
            is UiState.Initial -> {}
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
                        text = viewModel.screenState.value.message!!,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 84.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(viewModel.screenState.value.data!!) { item ->
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