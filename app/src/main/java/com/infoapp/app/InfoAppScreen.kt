package com.infoapp.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoAppScreen(
    appData: AppData,
    isAdmin: Boolean,
    onLoginClick: () -> Unit,
    onLogout: () -> Unit,
    onDataChange: (AppData) -> Unit
) {
    var pokazDodaj by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("InfoApp", fontWeight = FontWeight.Bold)
                        Text(
                            "Tablica informacyjna",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                actions = {
                    if (isAdmin) {
                        TextButton(onClick = onLogout) {
                            Text(
                                "Wyloguj",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Button(
                            onClick = onLoginClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Zaloguj", fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                ExtendedFloatingActionButton(
                    onClick = { pokazDodaj = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Dodaj") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (appData.ogloszenia.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appData.ogloszenia.reversed()) { o ->
                        OgloszenieCard(
                            ogloszenie = o,
                            isAdmin = isAdmin,
                            onDelete = {
                                val nowa = AppData(
                                    users = appData.users,
                                    ogloszenia = appData.ogloszenia
                                        .filter { it.id != o.id }
                                        .toMutableList()
                                )
                                onDataChange(nowa)
                            }
                        )
                    }
                }
            }
        }
    }

    if (pokazDodaj) {
        DodajDialog(
            onDismiss = { pokazDodaj = false },
            onDodaj = { tytul, tresc, wazne ->
                val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val nowe = Ogloszenie(
                    id = System.currentTimeMillis().toString(),
                    tytul = tytul,
                    tresc = tresc,
                    autor = "admin",
                    wazne = wazne,
                    data = data
                )
                val nowa = AppData(
                    users = appData.users,
                    ogloszenia = (appData.ogloszenia + nowe).toMutableList()
                )
                onDataChange(nowa)
                pokazDodaj = false
            }
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Brak informacji",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Nie ma jeszcze żadnych ogłoszeń.\nZaloguj się jako administrator, aby dodać pierwsze.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun OgloszenieCard(
    ogloszenie: Ogloszenie,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    val accent = if (ogloszenie.wazne) Color(0xFFE53935) else MaterialTheme.colorScheme.primary
    val bg = if (ogloszenie.wazne) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(accent)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (ogloszenie.wazne) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFFE53935), RoundedCornerShape(4.dp))
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "WAŻNE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE53935)
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                        }
                        Text(
                            ogloszenie.tytul,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (isAdmin) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Usuń",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    ogloszenie.tresc,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        ogloszenie.autor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "•",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        ogloszenie.data,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DodajDialog(
    onDismiss: () -> Unit,
    onDodaj: (String, String, Boolean) -> Unit
) {
    var tytul by remember { mutableStateOf("") }
    var tresc by remember { mutableStateOf("") }
    var wazne by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nowe ogłoszenie", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = tytul,
                    onValueChange = { tytul = it },
                    label = { Text("Tytuł") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = tresc,
                    onValueChange = { tresc = it },
                    label = { Text("Treść") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = wazne,
                        onCheckedChange = { wazne = it }
                    )
                    Text("Oznacz jako ważne", fontSize = 14.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (tytul.isNotBlank()) onDodaj(tytul, tresc, wazne)
                },
                enabled = tytul.isNotBlank()
            ) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anuluj") }
        }
    )
}
