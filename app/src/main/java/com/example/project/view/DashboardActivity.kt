package com.example.project.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.project.R

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody(){
    val context = LocalContext.current
    val activity = context as Activity

    data class NavItem(val label:String,val icon:Int)

    var selectedIndex by remember { mutableStateOf(0) }

    val listItems = listOf(
        NavItem(label = "Home", R.drawable.baseline_home_24),
        NavItem(label = "Search", R.drawable.baseline_search_24),
        NavItem(label = "Profile", R.drawable.baseline_person_24),

        )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listItems.forEachIndexed { index,item->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(item.label)
                        },
                        onClick = {
                            selectedIndex = index
                        },
                        selected = selectedIndex == index
                    )
                }
            }
        }
    ) {padding->
        Box (
            modifier = Modifier.fillMaxSize().padding(padding)
        )
        }
    }

