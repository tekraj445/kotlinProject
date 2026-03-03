package com.example.project.view

import com.example.project.R
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project.Model.ProductModel
import com.example.project.repository.ProductRepoImpl
import com.example.project.viewmodel.ProductViewModel
import kotlin.collections.get

@Composable
fun HomeScreen(){
    var pName by remember { mutableStateOf("") }
    var pPrice by remember { mutableStateOf("") }
    var pDesc by remember { mutableStateOf("") }

    val productViewModel = remember { ProductViewModel(ProductRepoImpl()) }
    var showDialog by remember { mutableStateOf(false) }

    val data = productViewModel.products.observeAsState(initial=null)


    LaunchedEffect(data.value) {
        productViewModel.getAllProduct()

        data.value?.let { product ->
            pName = product.productName
            pPrice = product.price.toString()
            pDesc = product.description
        }
    }

    val products  = productViewModel.allProducts.observeAsState(initial = emptyList())


    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(Color.White)
    ) {
        item{
            if (showDialog){
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    confirmButton = {
                        TextButton(onClick={
                            val model = ProductModel(
                                productId = data.value!!.productId,
                                productName = pName,
                                price = pPrice.toDouble(),
                                description = pDesc,
                                categoryId = data.value!!.categoryId
                            )
                            productViewModel.updateProduct(model){
                                    success,message->
                                if (success){
                                    showDialog=false
                                }else {
                                    val context = null
                                    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }){
                            Text("Update")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick={
                            showDialog=false
                        }){
                            Text("Cancel")
                        }
                    },
                    title = {Text(text="Update Product")},
                    text = {Column {
                        Text("Product Name")
                        OutlinedTextField(value = pName, onValueChange = {pName=it} )
                        Text("Product price")
                        OutlinedTextField(value = pPrice, onValueChange = {pPrice=it})
                        Text("Product description")
                        OutlinedTextField(value = pDesc, onValueChange = {pDesc=it})
                    }}
                )
            }
        }

        items(products.value!!.size){index ->
            val data = products.value!![index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp)
            ){
                Column{
                    AsyncImage(
                        model = data.productImage,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(data.productName)
                    Text(data.description)
                    Text(data.price.toString())


                    IconButton(onClick ={
                        showDialog=true;
                    }){
                        Icon(
                            painter = painterResource(R.drawable.baseline_edit_24),
                            contentDescription = null
                        )
                    }


                    IconButton(onClick ={
                        productViewModel.deleteProduct(data.productId){success,message->

                        }

                    }){
                        Icon(
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }

}
