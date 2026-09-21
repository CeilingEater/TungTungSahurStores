package com.appleeater.tungtungsahurstores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductAdapter(private val products: List<Product>):
RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.productName)
        val price = itemView.findViewById<TextView>(R.id.productPrice)
        val image = itemView.findViewById<ImageView>(R.id.productImage)
        val addToCartButton = itemView.findViewById<Button>(R.id.addToCartButton)
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.name
        holder.price.text = "R${product.price}"
        holder.image.load(product.imageURL) {
            placeholder(android.R.drawable.ic_menu_report_image)
            error(android.R.drawable.ic_delete)
        }

        holder.addToCartButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid?: return@setOnClickListener
            val cartItem = CartItem(
                productId = product.name,
                name = product.name,
                price = product.price,
                quantity = 1,
                imageURL = product.imageURL
            )

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("cart")
                .document(product.name)
                .set(cartItem)
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context,"Added to cart", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        holder.itemView.context,
                        "Failed to add to cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}