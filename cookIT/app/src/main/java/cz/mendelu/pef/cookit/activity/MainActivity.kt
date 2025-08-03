package cz.mendelu.pef.cookit.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.mendelu.pef.cookit.navigation.Destination
import cz.mendelu.pef.cookit.navigation.NavGraph
import cz.mendelu.pef.cookit.ui.theme.theme.CookITTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookITTheme {
                NavGraph(startDestination = Destination.SearchScreen.route)
            }
        }
    }
}

@Composable
fun HelloScreen(paddingValues: PaddingValues) {

    var text by rememberSaveable { mutableStateOf("") }

    HelloScreenContent(
        paddingValues = paddingValues,
        text = text,
        onValueChange = {
            text = it
        }
    )


}

@Composable
fun HelloScreenContent(
    paddingValues: PaddingValues,
    text: String,
    onValueChange: (String) -> Unit,
){
    Column(modifier = Modifier.padding(paddingValues))
    {
        Text(text = "Tady je text")

        OutlinedTextField(
            value = text,
            onValueChange = onValueChange
        )
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CookITTheme {
        Greeting("Android")
    }

}