package com.example.educationapp

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RatingAdapter(private val users: List<RatingItem>) :
    RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rankIcon: ImageView = view.findViewById(R.id.rank_icon)
        val rankNumber: TextView = view.findViewById(R.id.rank_number)
        val username: TextView = view.findViewById(R.id.username)
        val background: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rating, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = users[position].username

        val context = holder.itemView.context
        val bgColor: Int
        val iconRes: Int?

        when (position) {
            0 -> {
                bgColor = Color.YELLOW
                iconRes = R.drawable.ic_trophy_gold
            }

            1 -> {
                bgColor = Color.LTGRAY
                iconRes = R.drawable.ic_trophy_silver
            }

            2 -> {
                bgColor = Color.rgb(255, 165, 0) // оранжевый
                iconRes = R.drawable.ic_trophy_bronze
            }

            else -> {
                bgColor = Color.parseColor("#80D8FF")
                iconRes = null
            }
        }

        // Общий фон карточки
        val bgDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 30f
            setColor(bgColor)
            setStroke(2, Color.BLACK)
        }
        holder.background.background = bgDrawable

        // Иконка или номер
        if (iconRes != null) {
            holder.rankIcon.visibility = View.VISIBLE
            holder.rankIcon.setImageResource(iconRes)
            holder.rankNumber.visibility = View.GONE
        } else {
            holder.rankIcon.visibility = View.GONE
            holder.rankNumber.visibility = View.VISIBLE
            holder.rankNumber.text = (position + 1).toString()
        }

        val circleDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.parseColor("#B388FF")) // светло-фиолетовый
        }
        holder.rankNumber.background = circleDrawable
    }
}

