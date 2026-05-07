package com.nohari.campus_hub.Screens.Admin

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.nohari.campus_hub.Data.EventViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavHostController) {

    val context = LocalContext.current
    val vm: EventViewModel = viewModel()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            date = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Event")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = {
                    Text("Event Title")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Event Description")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            OutlinedButton(
                onClick = {
                    datePicker.show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (date.isEmpty()) {
                        "Select Event Date"
                    } else {
                        date
                    }
                )
            }

            Button(
                onClick = {

                    when {

                        title.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Title required",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        description.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Description required",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        date.isBlank() -> {Toast.makeText(
                            context,
                            "Date required",
                            Toast.LENGTH_SHORT
                        ).show()
                        }

                        else -> {

                            vm.addEvent(
                                title = title,
                                description = description,
                                date = date,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Event created",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.popBackStack()
                                },
                                onError = {
                                    Toast.makeText(
                                        context,
                                        it,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Publish Event")
            }
        }
    }
}