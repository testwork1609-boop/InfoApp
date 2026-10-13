package com.infoapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

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
                var zdalneOgloszenia by remember { mutableStateOf(storage.loadCache()) }
                var ladowanie by remember { mutableStateOf(true) }
                var blad by remember { mutableStateOf<String?>(null) }
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    ladowanie = true
                    val result = ApiManager.fetchOgloszenia()
                    result.onSuccess { lista ->
                        zdalneOgloszenia = lista
                        storage.saveCache(lista)
                        blad = null
                    }.onFailure { e ->
                        blad = e.message ?: "Błąd połączenia"
                    }
                    ladowanie = false
                }

                InfoAppScreen(
                    appData = appData,
                    zdalneOgloszenia = zdalneOgloszenia,
                    isAdmin = isAdmin,
                    ladowanie = ladowanie,
                    blad = blad,
                    onOdswiez = {
                        scope.launch {
                            ladowanie = true
                            val result = ApiManager.fetchOgloszenia()
                            result.onSuccess { lista ->
                                zdalneOgloszenia = lista
                                storage.saveCache(lista)
                                blad = null
                            }.onFailure { e ->
                                blad = e.message ?: "Błąd połączenia"
                            }
                            ladowanie = false
                        }
                    },
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
