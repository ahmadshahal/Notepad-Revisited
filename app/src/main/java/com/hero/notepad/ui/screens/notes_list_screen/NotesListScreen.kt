package com.hero.notepad.ui.screens.notes_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.ui.navigation.Screens
import com.hero.notepad.ui.screens.notes_list_screen.components.NoteItem

@Composable
fun NotesListScreen(navController: NavController, viewModel: NotesListViewModel = hiltViewModel()) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onBackground,
                title = {
                    Text(text = "Notes", style = MaterialTheme.typography.h5)
                },
                elevation = 16.dp,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.AddEditNoteScreen + "/-1") },
                backgroundColor = MaterialTheme.colors.onBackground,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    ) {
        if (viewModel.state.value.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }
        if (viewModel.state.value.error != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = viewModel.state.value.error!!,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 84.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(viewModel.state.value.list) { item ->
                NoteItem(
                    item,
                    onClick = {
                        navController.navigate(Screens.AddEditNoteScreen + "/${item.id}")
                    },
                    onDeleteClicked = {
                        viewModel.deleteNote(item)
                    }
                )
            }
        }
    }
}