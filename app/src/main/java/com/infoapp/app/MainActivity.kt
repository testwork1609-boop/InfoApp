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
                var zalogowany by remember { mutableStateOf<User?>(null) }

                if (zalogowany == null) {
                    LoginScreen(users = appData.users) { user ->
                        zalogowany = user
                    }
                } else {
                    InfoAppScreen(
                        appData = appData,
                        currentUser = zalogowany!!,
                        onDataChange = { nowe ->
                            appData = nowe
                            storage.save(nowe)
                        },
                        onLogout = { zalogowany = null }
                    )
                }
            }
        }
    }
}
