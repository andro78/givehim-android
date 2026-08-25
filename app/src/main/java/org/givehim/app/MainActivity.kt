package org.givehim.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.givehim.app.ui.GivehimApp
import org.givehim.app.ui.GivehimTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GivehimTheme { Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { GivehimApp() } } }
    }
}
