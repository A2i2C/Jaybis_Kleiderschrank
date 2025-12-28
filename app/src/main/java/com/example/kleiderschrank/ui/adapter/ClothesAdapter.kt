package com.example.kleiderschrank.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kleiderschrank.R
import com.example.kleiderschrank.datahandling.entity.Kleidung

class ClothesAdapter(private val context: Context, private val clothesList: List<Kleidung>): RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        //What layout to use for each item
        val clothesLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_clothes, parent, false)
        return ClothesViewHolder(clothesLayout)
    }

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        //Bind data to the views
        val clothesName = clothesList[position]
        holder.clothesImage.setImageResource(R.drawable.ic_launcher_background) // Placeholder image
        holder.clothesName.text = clothesName.name

    }

    override fun getItemCount(): Int {
        return clothesList.size
    }

    class ClothesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val clothesImage: ImageView = itemView.findViewById(com.example.kleiderschrank.R.id.imageClothes)
        val clothesName: TextView = itemView.findViewById(com.example.kleiderschrank.R.id.imageName)
    }

}