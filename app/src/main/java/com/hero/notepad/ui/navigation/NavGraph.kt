package com.hero.notepad.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hero.notepad.common.Constants
import com.hero.notepad.ui.screens.add_edit_note_screen.AddEditNoteScreen
import com.hero.notepad.ui.screens.notes_list_screen.NotesListScreen
import com.hero.notepad.ui.screens.notes_list_screen.NotesListViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.NotesListScreen) {
        composable(Screens.NotesListScreen) {
            NotesListScreen(navController = navController)
        }
        composable(Screens.AddEditNoteScreen + "/{${Constants.NOTE_ID_KEY}}") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Screens.NotesListScreen)
            }
            val notesListViewModel: NotesListViewModel = hiltViewModel(parentEntry)
            AddEditNoteScreen(navController = navController, notesListViewModel = notesListViewModel)
        }
    }
}