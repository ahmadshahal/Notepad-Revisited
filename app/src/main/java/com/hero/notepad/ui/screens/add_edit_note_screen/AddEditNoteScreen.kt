package com.hero.notepad.ui.screens.add_edit_note_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hero.notepad.ui.screens.add_edit_note_screen.components.HintTextField
import com.hero.notepad.ui.screens.notes_list_screen.NotesListViewModel

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    addEditNoteViewModel: AddEditNoteViewModel = hiltViewModel(),
    notesListViewModel: NotesListViewModel
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = 16.dp,
                content = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!addEditNoteViewModel.addEditState.value.isLoading &&
                        (addEditNoteViewModel.titleFieldState.value.isNotEmpty() || addEditNoteViewModel.descriptionFieldState.value.isNotEmpty())
                    ) {
                        addEditNoteViewModel.saveNote() {
                            notesListViewModel.getNotes()
                            navController.popBackStack()
                        }
                    }
                },
                backgroundColor = MaterialTheme.colors.onBackground,
            ) {
                if (addEditNoteViewModel.addEditState.value.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    ) {
        when {
            addEditNoteViewModel.getNoteState.value.isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            addEditNoteViewModel.getNoteState.value.error != null -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = addEditNoteViewModel.getNoteState.value.error!!,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp),
                ) {
                    HintTextField(
                        value = addEditNoteViewModel.titleFieldState.value, onChange = {
                            addEditNoteViewModel.titleFieldState.value = it
                        },
                        textStyle = MaterialTheme.typography.h5.copy(
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        hint = "Title",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    HintTextField(
                        value = addEditNoteViewModel.descriptionFieldState.value,
                        onChange = {
                            addEditNoteViewModel.descriptionFieldState.value = it
                        },
                        textStyle = MaterialTheme.typography.body1.copy(
                            color = MaterialTheme.colors.onBackground
                        ),
                        hint = "Description",
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxSize(),
                    )
                }
            }
        }
    }
}