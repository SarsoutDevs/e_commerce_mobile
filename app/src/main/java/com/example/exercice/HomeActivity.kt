package com.example.exercice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exercice.data.Product
import com.example.exercice.data.SQLiteHelper

class HomeActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = SQLiteHelper(this)


        dbHelper.insertProduct(
            "STGsivir Gaming Desktop",
            5199.0,
            R.drawable.pc_gamer1,
            "Memory PC Ryzen 7 5700X 8X 4.6 GHz, 32 GB DDR4 RAM, 1TB M.2 SSD, RTX 4060 8GB"
        )

        dbHelper.insertProduct(
            "Memory PC",
            769.0,
            R.drawable.pc_gamer2,
            "STGsivir Gaming Desktop PC, Intel Core i7, 1TB SSD"
        )
        dbHelper.insertProduct(
            "Pc de Bureau LITE",
            2410.6,
            R.drawable.pc_gamer3,
            "Boîtier Gamer Enter Razor Mid-Tower RGB + Alimentation 500W / Noir - Processeur AMD Ryzen 5 4600G, (jusqu'à 4.2 GHz, 11 Mo de mémoire cache) - RAM 8 Go (1x 8 Go) DDR4 - Disque SSD 2.5\" SATA 256 Go - Carte graphique AMD Radeon Vega 7 Graphics - Carte mère Biostar B450MHP DDR4"
        )


        val products = dbHelper.getAllProducts()

        setContent {
            HomeScreenScaffold(
                products = products,
                onDeleteAllProducts = {
                    dbHelper.deleteAllProducts()
                },
                onLogout = {
                    val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putBoolean("isLoggedIn", false)
                        apply()
                    }


                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenScaffold(
    products: List<Product>,
    onDeleteAllProducts: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Produits Disponibles") },
                actions = {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Text("Déconnexion", fontSize = 16.sp)
                    }
                }
            )
        },
        content = { paddingValues ->
            HomeScreen(
                products = products,
                onDeleteAllProducts = onDeleteAllProducts,
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}

@Composable
fun HomeScreen(
    products: List<Product>,
    onDeleteAllProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(products) { product ->
                ItemComposable(Product = product)
            }
        }


        Button(
            onClick = onDeleteAllProducts,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text("Supprimer tous les produits", fontSize = 16.sp)
        }
    }
}



/*@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            ItemComposable(Product = product)
        }
    }
} */


@Composable
fun ItemComposable(modifier: Modifier =Modifier, Product: Product){
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra("productName", Product.name)
                intent.putExtra("productPrice", Product.price)
                intent.putExtra("productImage", Product.imageUrl)
                intent.putExtra("productDescription", Product.description)
                context.startActivity(intent)
            },
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = CenterHorizontally
        ){Text(text = Product.name,fontSize = 20.sp,fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = Product.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text ="$${Product.price} $",fontSize = 20.sp,fontWeight = FontWeight.Bold)

        }
    }
}
