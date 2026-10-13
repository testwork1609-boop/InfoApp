package com.infoapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Ogloszenie(
    val id: String,
    val tytul: String,
    val tresc: String,
    val wazne: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InfoAppScreen()
                }
            }
        }
    }
}

@Composable
fun InfoAppScreen() {
    var ogloszenia by remember {
        mutableStateOf(
            listOf(
                Ogloszenie(
                    id = "1",
                    tytul = "Witaj w InfoApp!",
                    tresc = "To Twoje pierwsze ogloszenie w tablicy informacyjnej.",
                    wazne = true
                ),
                Ogloszenie(
                    id = "2",
                    tytul = "Zebranie",
                    tresc = "W piatek o 15:00 w sali 204.",
                    wazne = false
                )
            )
        )
    }

    var pokazDialog by remember { mutableStateOf(false) }
    var nowyTytul by remember { mutableStateOf("") }
    var nowaTresc by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "InfoApp - Tablica informacyjna",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ogloszenia) { o ->
                KartaOgloszenia(
                    ogloszenie = o,
                    onUsun = {
                        ogloszenia = ogloszenia.filter { it.id != o.id }
                    }
                )
            }
        }

        Button(
            onClick = { pokazDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("+ Dodaj ogloszenie")
        }
    }

    if (pokazDialog) {
        AlertDialog(
            onDismissRequest = { pokazDialog = false },
            title = { Text("Nowe ogloszenie") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nowyTytul,
                        onValueChange = { nowyTytul = it },
                        label = { Text("Tytul") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nowaTresc,
                        onValueChange = { nowaTresc = it },
                        label = { Text("Tresc") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nowyTytul.isNotBlank()) {
                        ogloszenia = ogloszenia + Ogloszenie(
                            id = System.currentTimeMillis().toString(),
                            tytul = nowyTytul,
                            tresc = nowaTresc,
                            wazne = false
                        )
                        nowyTytul = ""
                        nowaTresc = ""
                        pokazDialog = false
                    }
                }) {
                    Text("Dodaj")
                }
            },
            dismissButton = {
                TextButton(onClick = { pokazDialog = false }) {
                    Text("Anuluj")
                }
            }
        )
    }
}

@Composable
fun KartaOgloszenia(ogloszenie: Ogloszenie, onUsun: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (ogloszenie.wazne) {
                    Modifier.border(
                        width = 3.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else Modifier
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ogloszenie.tytul,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = ogloszenie.tresc,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            TextButton(onClick = onUsun) {
                Text("X", color = Color.Gray)
            }
        }
    }
}
