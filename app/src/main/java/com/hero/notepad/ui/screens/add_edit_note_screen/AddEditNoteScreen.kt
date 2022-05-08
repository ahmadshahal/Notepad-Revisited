package com.hero.notepad.ui.screens.add_edit_note_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hero.notepad.common.Constants
import com.hero.notepad.common.UiEvent
import com.hero.notepad.common.UiState
import com.hero.notepad.ui.screens.add_edit_note_screen.components.HintTextField
import com.hero.notepad.ui.screens.notes_list_screen.NotesListViewModel

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    addEditNoteViewModel: AddEditNoteViewModel = hiltViewModel(),
    notesListViewModel: NotesListViewModel
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        addEditNoteViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.PopBackStack -> {
                    navController.popBackStack()
                    notesListViewModel.getNotes()
                }
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                    )
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
            AppBar(navController = navController, addEditNoteViewModel = addEditNoteViewModel)
        },
        floatingActionButton = {
            FAB(addEditNoteViewModel = addEditNoteViewModel)
        }
    ) {
        when (val uiState = addEditNoteViewModel.loadNoteState.value) {
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
                        text = uiState.message!!,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            else -> {
                Body(addEditNoteViewModel = addEditNoteViewModel, navController = navController)
            }
        }
    }
}

@Composable
fun Body(addEditNoteViewModel: AddEditNoteViewModel, navController: NavController) {
    if (addEditNoteViewModel.popUpDialogState.value) {
        PopBackAlertDialog(
            addEditNoteViewModel = addEditNoteViewModel,
            navController = navController
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
    ) {
        HintTextField(
            value = addEditNoteViewModel.titleFieldState.value,
            onChange = {
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
    BackHandler {
        backPressed(navController, addEditNoteViewModel)
    }
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 16.dp,
        navigationIcon = {
            IconButton(
                onClick = {
                    backPressed(navController, addEditNoteViewModel)
                }
            ) {
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
            ColorsDropDownMenu(addEditNoteViewModel)
        },
        title = {}
    )
}

@Composable
fun ColorsDropDownMenu(addEditNoteViewModel: AddEditNoteViewModel) {
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
) {
    FloatingActionButton(
        onClick = {
            addEditNoteViewModel.saveNote()
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
            addEditNoteViewModel.noteColorIdx.value = colorIdx
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
            ) {
                if (addEditNoteViewModel.noteColorIdx.value == colorIdx) {
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
                text = colorText,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun PopBackAlertDialog(addEditNoteViewModel: AddEditNoteViewModel, navController: NavController) {
    AlertDialog(
        onDismissRequest = { addEditNoteViewModel.popUpDialogState.value = false },
        title = {
            Text(
                text = "Discard Changes?",
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.SemiBold
            )
        },
        confirmButton = {
            TextButton(onClick = {
                navController.popBackStack()
            }) {
                Text(
                    text = "Discard",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { addEditNoteViewModel.popUpDialogState.value = false }) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        shape = RoundedCornerShape(9.dp),
    )
}

fun backPressed(navController: NavController, addEditNoteViewModel: AddEditNoteViewModel) {
    if (addEditNoteViewModel.hasChanges()) {
        addEditNoteViewModel.popUpDialogState.value = true
    } else {
        navController.popBackStack()
    }
}
