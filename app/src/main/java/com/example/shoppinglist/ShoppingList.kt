package com.example.shoppinglist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp(){
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }


    Column(modifier= Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = {showDialog=true}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item")

        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(sItems){
                item ->
                if (item.isEditing){
                    ShoppingItemEditor(Item = item, onClickSave = {
                        editedItemName, editedItemQuantity ->
                        sItems=sItems.map { it.copy(isEditing = false) }
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem?.let {
                            it.name=editedItemName
                            it.quantity=editedItemQuantity
                        }
                    })
                    
                }
                else {
                    ShoppingListItem(item = item, onEditClick = {sItems=sItems.map { it.copy(isEditing = it.id ==item.id) }}, onDeleteClick = {sItems=sItems-item})

                }
            }

        }

    }
    
    

    if (showDialog){
        AlertDialog(
            onDismissRequest = { showDialog=false },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = {showDialog=false
                        itemName=""
                        itemQuantity=""
                    }) {
                        Text(text = "Cancel")

                    }

                    Button(onClick = {
                        if (itemName.isNotBlank() && itemQuantity.isNotBlank() && itemQuantity.isDigitsOnly()){
                            val item = ShoppingItem(
                                id = sItems.size+1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItems += item
                            showDialog = false
                            itemName=""
                            itemQuantity=""


                        }
                    }) {
                        Text(text = "Add")
                    }
                }

            },
            title = {Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName, onValueChange ={itemName=it}, label = { Text(text = "Item Name")}, singleLine = true)
                    OutlinedTextField(value = itemQuantity, onValueChange ={itemQuantity=it}, label = { Text(text = "Item Quantity")}, singleLine = true )
                }
            }

        )
    }


}

@Composable
fun ShoppingListItem(item: ShoppingItem, onEditClick: ()->Unit, onDeleteClick: ()->Unit){
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween,) {
        Text(text = item.name, Modifier.padding(top = 12.dp))
        Text(text = "Qty: ${item.quantity}", Modifier.padding(top = 12.dp))
        Row {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }



    }
    HorizontalDivider()
}

@Composable
fun ShoppingItemEditor(Item: ShoppingItem, onClickSave:(String,Int)->Unit){
    var editedItemName by remember {(mutableStateOf(Item.name))}
    var editedItemQuantity by remember { mutableStateOf(Item.quantity.toString()) }
    var isEditing by remember{ mutableStateOf(Item.isEditing) }
    Row (
        Modifier
            .background(color = Color.LightGray, RoundedCornerShape(50))
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment =Alignment.CenterVertically) {
        Column {
            BasicTextField(value = editedItemName, onValueChange ={editedItemName = it}, Modifier.padding(vertical = 8.dp))
            BasicTextField(value = editedItemQuantity, onValueChange = {editedItemQuantity=it}, Modifier.padding(vertical = 8.dp))
        }
        Button(onClick = {
            isEditing=false
            onClickSave(editedItemName, editedItemQuantity.toIntOrNull()?:1) }) {
            Text(text = "Save")
        }

    }
    
}