package io.lukelynch.businesscardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lukelynch.businesscardapp.R.drawable
import io.lukelynch.businesscardapp.ui.theme.BusinesscardappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BusinesscardappTheme {
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
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        BusinessCardMain(name, "Android Developer" )
        Spacer(Modifier.height(200.dp))
        BusinessCardDetails()
    }
}

@Composable
fun BusinessCardMain(name: String, title: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
       Image(
           painter = painterResource(id = drawable.ic_launcher_foreground),
           contentDescription = "My Image",
           modifier = modifier
       )
        Text(
            text = name,
            fontSize = 30.sp,
            modifier = modifier.padding(top = 24.dp, bottom = 2.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            modifier = modifier.padding(top = 2.dp, bottom = 24.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BusinessCardDetails(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 50.dp)) {
        Row {
            Icon(
                painter = painterResource(id = drawable.phone_icon),
                contentDescription = "Phone Icon",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Phone: 555-555-5555",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Row {
            Icon (
                painter = painterResource(id = drawable.share_icon),
                contentDescription = "Social Media Icon",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "@lukelynch",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Row {
            Icon(
                painter = painterResource(id = drawable.email_icon),
                contentDescription = "Email Icon",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "luke@lukelynch.io"
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true,  backgroundColor = 0xFF00FFFF)
@Composable
fun GreetingPreview() {
    BusinesscardappTheme {
        Greeting("Luke Lynch")
    }
}