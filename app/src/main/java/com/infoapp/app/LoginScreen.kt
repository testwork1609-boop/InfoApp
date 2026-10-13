package com.infoapp.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginDialog(
    admin: User?,
    onSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var haslo by remember { mutableStateOf("") }
    var blad by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Lock, contentDescription = null) },
        title = {
            Text("Logowanie administratora", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    "Wprowadź dane, aby dodawać i usuwać ogłoszenia.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it; blad = "" },
                    label = { Text("Login") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = haslo,
                    onValueChange = { haslo = it; blad = "" },
                    label = { Text("Hasło") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                if (blad.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        blad,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val user = admin
                if (user != null && user.login == login && user.haslo == haslo) {
                    onSuccess()
                } else {
                    blad = "Nieprawidłowy login lub hasło"
                }
            }) {
                Text("Zaloguj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anuluj") }
        }
    )
}
