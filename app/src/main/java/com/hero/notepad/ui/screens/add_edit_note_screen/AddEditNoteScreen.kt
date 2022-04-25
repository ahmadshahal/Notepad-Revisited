package com.hero.notepad.ui.screens.add_edit_note_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hero.notepad.common.Constants
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
            AppBar(navController = navController, addEditNoteViewModel = addEditNoteViewModel)
        },
        floatingActionButton = {
            FAB(
                addEditNoteViewModel = addEditNoteViewModel,
                notesListViewModel = notesListViewModel,
                navController = navController
            )
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

@Composable
fun AppBar(navController: NavController, addEditNoteViewModel: AddEditNoteViewModel) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 16.dp,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onBackground,
                )
            }
        },
        actions = {
            IconButton(onClick = { addEditNoteViewModel.dropDownMenuState.value = true }) {
                Icon(
                    imageVector = Icons.Rounded.ColorLens,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(22.dp)
                )
            }
            DropdownMenu(
                expanded = addEditNoteViewModel.dropDownMenuState.value,
                onDismissRequest = {
                    addEditNoteViewModel.dropDownMenuState.value = false
                },
            ) {
                ColorsDropDownMenuItem(
                    colorIdx = 0,
                    colorText = "Red",
                    addEditNoteViewModel = addEditNoteViewModel
                )
                ColorsDropDownMenuItem(
                    colorIdx = 1,
                    colorText = "Green",
                    addEditNoteViewModel = addEditNoteViewModel
                )
                ColorsDropDownMenuItem(
                    colorIdx = 2,
                    colorText = "Blue",
                    addEditNoteViewModel = addEditNoteViewModel
                )
            }
        },
        title = {

        }
    )
}

@Composable
fun FAB(
    addEditNoteViewModel: AddEditNoteViewModel,
    notesListViewModel: NotesListViewModel,
    navController: NavController
) {
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

@Composable
fun ColorsDropDownMenuItem(
    colorIdx: Int,
    colorText: String,
    addEditNoteViewModel: AddEditNoteViewModel
) {
    DropdownMenuItem(
        onClick = {
            addEditNoteViewModel.noteColor = colorIdx
            addEditNoteViewModel.dropDownMenuState.value = false
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
                    .background(Constants.colors[colorIdx])
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = colorText,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}