package com.example.project.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.project.Model.ProductModel
import com.example.project.repository.CommonRepoImpl
import com.example.project.repository.ProductRepoImpl
import com.example.project.viewmodel.CommonViewModel
import com.example.project.viewmodel.ProductViewModel

@Composable
fun AddProduct() {

    val context = LocalContext.current

    var productName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }

    var cloudinaryUrl by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    val productViewModel = remember { ProductViewModel(ProductRepoImpl()) }
    val commonViewModel = remember { CommonViewModel(CommonRepoImpl()) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { imageuri: Uri? ->

            imageuri?.let {
                isUploading = true

                commonViewModel.uploadImage(context, it) { imageUrl ->
                    isUploading = false

                    if (imageUrl != null) {
                        cloudinaryUrl = imageUrl
                        Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Add Product",
                style = MaterialTheme.typography.headlineSmall
            )

            // ✅ Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {

                if (cloudinaryUrl.isEmpty()) {
                    Text("Tap to select image")
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(cloudinaryUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                if (isUploading) {
                    CircularProgressIndicator()
                }
            }

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categoryId,
                onValueChange = { categoryId = it },
                label = { Text("Category ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    if (cloudinaryUrl.isEmpty()) {
                        Toast.makeText(context, "Please upload image", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val product = ProductModel(
                        productName = productName,
                        description = description,
                        price = price.toDoubleOrNull() ?: 0.0,
                        quantity = quantity.toIntOrNull() ?: 0,
                        categoryId = categoryId,
                        productImage = cloudinaryUrl   // ✅ SAVE URL HERE
                    )

                    productViewModel.addProduct(product) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Product Added", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUploading
            ) {
                Text(if (isUploading) "Uploading..." else "Save Product")
            }
        }
    }
}