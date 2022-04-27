package com.hero.notepad.ui.screens.add_edit_note_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hero.notepad.common.Constants
import com.hero.notepad.common.UiState
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
        when(addEditNoteViewModel.loadNoteState.value) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = addEditNoteViewModel.loadNoteState.value.message!!,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            else -> {
                Body(addEditNoteViewModel = addEditNoteViewModel)
            }
        }
    }
}

@Composable
fun Body(addEditNoteViewModel: AddEditNoteViewModel) {
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
            MyDropDownMenu(addEditNoteViewModel)
        },
        title = {}
    )
}

@Composable
fun MyDropDownMenu(addEditNoteViewModel: AddEditNoteViewModel) {
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
}

@Composable
fun FAB(
    addEditNoteViewModel: AddEditNoteViewModel,
    notesListViewModel: NotesListViewModel,
    navController: NavController
) {
    FloatingActionButton(
        onClick = {
            addEditNoteViewModel.saveNote {
                notesListViewModel.getNotes()
                navController.popBackStack()
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
    ) {
        when (addEditNoteViewModel.saveNoteState.value) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 3.dp
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Rounded.Save,
                    contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
            }
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