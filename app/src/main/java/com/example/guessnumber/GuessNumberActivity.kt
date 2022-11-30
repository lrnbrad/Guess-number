package com.example.guessnumber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guessnumber.ui.theme.GuessNumberTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.get
import org.koin.core.context.startKoin

class GuessNumberActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@GuessNumberActivity)
            // Load modules
            modules(guessNumberModule)

            setContent {
                GuessNumberTheme {
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            Header()
                            Answer()
                            InputField()
                            RegenerateBtn()
                            ResultDialog()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    if (!LocalInspectionMode.current) {
        // We're _not_ executing in an Android Studio Preview.

        // Use your injected Koin instance here e.g.
        val viewModel: GuessNumberViewModel = get()
        GuessNumberTheme {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Header()
                InputField(viewModel)
                RegenerateBtn(viewModel)
            }
        }
    }
}