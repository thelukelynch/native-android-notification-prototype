package io.lukelynch.nativenotificationprototype

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import io.lukelynch.nativenotificationprototype.ui.theme.NativeNotificationPrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NativeNotificationPrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Hello $name!")
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Token retrieval failed", task.exception)
                    Toast.makeText(context, "Token retrieval failed", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM", "token: $token")
                Toast.makeText(context, "FCM token: $token", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Show FCM Token")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NativeNotificationPrototypeTheme {
        Greeting("Android")
    }
}