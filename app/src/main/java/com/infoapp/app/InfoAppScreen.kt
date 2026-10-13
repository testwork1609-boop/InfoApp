package com.infoapp.app

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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InfoAppScreen(
    appData: AppData,
    currentUser: User,
    onDataChange: (AppData) -> Unit,
    onLogout: () -> Unit
) {
    var pokazDialog by remember { mutableStateOf(false) }
    var nowyTytul by remember { mutableStateOf("") }
    var nowaTresc by remember { mutableStateOf("") }
    var nowaWazne by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("InfoApp", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Zalogowany: ${currentUser.login} (${currentUser.rola})",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            TextButton(onClick = onLogout) { Text("Wyloguj") }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(appData.ogloszenia) { o ->
                KartaOgloszenia(
                    ogloszenie = o,
                    mozeUsunac = currentUser.rola == "admin" || o.autor == currentUser.login,
                    onUsun = {
                        val nowa = AppData(
                            users = appData.users,
                            ogloszenia = appData.ogloszenia.filter { it.id != o.id }.toMutableList()
                        )
                        onDataChange(nowa)
                    }
                )
            }
        }

        Button(
            onClick = { pokazDialog = true },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
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
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = nowaWazne, onCheckedChange = { nowaWazne = it })
                        Text("Wazne")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nowyTytul.isNotBlank()) {
                        val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val nowe = Ogloszenie(
                            id = System.currentTimeMillis().toString(),
                            tytul = nowyTytul,
                            tresc = nowaTresc,
                            autor = currentUser.login,
                            wazne = nowaWazne,
                            data = data
                        )
                        val nowaData = AppData(
                            users = appData.users,
                            ogloszenia = (appData.ogloszenia + nowe).toMutableList()
                        )
                        onDataChange(nowaData)
                        nowyTytul = ""
                        nowaTresc = ""
                        nowaWazne = false
                        pokazDialog = false
                    }
                }) { Text("Dodaj") }
            },
            dismissButton = {
                TextButton(onClick = { pokazDialog = false }) { Text("Anuluj") }
            }
        )
    }
}

@Composable
fun KartaOgloszenia(ogloszenie: Ogloszenie, mozeUsunac: Boolean, onUsun: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().then(
            if (ogloszenie.wazne) Modifier.border(3.dp, Color.Red, RoundedCornerShape(8.dp))
            else Modifier
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ogloszenie.tytul, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(ogloszenie.tresc, fontSize = 14.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${ogloszenie.autor} - ${ogloszenie.data}",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }
            if (mozeUsunac) {
                TextButton(onClick = onUsun) { Text("X", color = Color.Gray) }
            }
        }
    }
}
