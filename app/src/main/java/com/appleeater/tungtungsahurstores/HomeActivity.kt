package com.appleeater.tungtungsahurstores

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val productList = mutableListOf<Product>()
    private lateinit var toolbarImage: ImageView
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        toolbarImage = findViewById(R.id.toolbarImage)
        loadProfile()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.toolbar),
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> { /* Already here */
                }

                R.id.nav_cart -> startActivity(Intent(this, CartActivity::class.java))
                R.id.nav_orders -> startActivity(Intent(this, OrderHistory::class.java))
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        findViewById<Button>(R.id.viewCartButton).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        recyclerView = findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //gets firebase instance and gets all documents from the collection
        FirebaseFirestore.getInstance().collection("products")
            .get()
            // executes if the data retrieval is successful
            .addOnSuccessListener { result ->
                for (document in result) {
                    //deserialize the document's fields into a strong-typed Kotlin Product data class object
                    val product = document.toObject(Product::class.java)
                    //product object is then added to the local productList
                    productList.add(product)
                }
                /*
                a new instance of ProductAdapter is created using this list
                adapter is assigned to the recyclerView, which triggers the UI to display the products
                 */
                recyclerView.adapter = ProductAdapter(productList)
            }
            //if database operation fails toast message displays
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show()
            }

    }


    private fun loadProfile() {
        userId?.let {
            db.collection("users").document(it).get().addOnSuccessListener { doc ->
                val avatarName = doc.getString("avatar")
                if (avatarName != null) {
                    val resId = resources.getIdentifier(avatarName, "drawable", packageName)
                    toolbarImage.setImageResource(resId)
                } else {
                    toolbarImage.setImageResource(R.drawable.default_avatar)
                }
            }
        }
    }

}