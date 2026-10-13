package com.infoapp.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(users: List<User>, onLogin: (User) -> Unit) {
    var login by remember { mutableStateOf("") }
    var haslo by remember { mutableStateOf("") }
    var blad by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("InfoApp", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Tablica informacyjna", fontSize = 14.sp)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it; blad = "" },
            label = { Text("Login") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = haslo,
            onValueChange = { haslo = it; blad = "" },
            label = { Text("Haslo") },
            modifier = Modifier.fillMaxWidth()
        )

        if (blad.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(blad, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                val user = users.find { it.login == login && it.haslo == haslo }
                if (user != null) onLogin(user)
                else blad = "Nieprawidlowy login lub haslo"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zaloguj sie")
        }
    }
}
