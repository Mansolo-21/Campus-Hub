package com.nohari.campus_hub.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OnboardingScreen(navController: NavController){

    val pages = listOf(
        "Welcome to Campus Hub",
        "Manage your school Activities Easily",
        "Connect students and teachers"
    )
    var page by remember { mutableStateOf((0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement= Arrangement.SpaceBetween
    ){
        Spacer(Modifier.height(40.dp))
        Column{
            Text(
                pages[page],
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = {navController.navigate("register_campus")}) {
                Text("Skip")
            }
            Button(onClick = {
                if (page < pages.lastIndex){
                    page++
                }else{
                    navController.navigate("register_campus")
                }
            }) {
                Text(if (page == pages.lastIndex)
                "Start" else "Next")
            }
        }
    }
}