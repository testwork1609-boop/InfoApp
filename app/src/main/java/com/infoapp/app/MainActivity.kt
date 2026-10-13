package com.infoapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val context = LocalContext.current
                val storage = remember { StorageManager(context) }
                var appData by remember { mutableStateOf(storage.load()) }
                var isAdmin by remember { mutableStateOf(false) }
                var pokazLogin by remember { mutableStateOf(false) }

                InfoAppScreen(
                    appData = appData,
                    isAdmin = isAdmin,
                    onLoginClick = { pokazLogin = true },
                    onLogout = { isAdmin = false },
                    onDataChange = { nowe ->
                        appData = nowe
                        storage.save(nowe)
                    }
                )

                if (pokazLogin) {
                    LoginDialog(
                        admin = appData.users.firstOrNull(),
                        onSuccess = {
                            isAdmin = true
                            pokazLogin = false
                        },
                        onDismiss = { pokazLogin = false }
                    )
                }
            }
        }
    }
}
