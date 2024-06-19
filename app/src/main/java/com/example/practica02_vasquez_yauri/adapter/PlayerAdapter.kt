package com.example.practica02_vasquez_yauri.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.practica02_vasquez_yauri.R
import com.example.practica02_vasquez_yauri.model.PlayerModel
import com.squareup.picasso.Picasso

class PlayerAdapter(private var lstPlayers: List<PlayerModel>) :
    RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvNameP)
        val tvPosition: TextView = itemView.findViewById(R.id.tvPositionP)
        val tvDorsal: TextView = itemView.findViewById(R.id.tvDorsalP)
        val ivPlayer: ImageView = itemView.findViewById(R.id.ivPlayerP)

        init {
            itemView.setOnClickListener {
                val player = lstPlayers[adapterPosition]
                val playerInfo = """
                    Nombre: ${player.name}
                    País: ${player.country}
                    Equipo actual: ${player.team}
                    Posición: ${player.position}
                    Dorsal: ${player.dorsal}
                """.trimIndent()

                AlertDialog.Builder(itemView.context, R.style.CustomAlertDialog)
                    .setTitle("Información extra del jugador")
                    .setMessage(playerInfo)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_players, parent, false))
    }

    override fun getItemCount(): Int {
        return lstPlayers.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPlayer = lstPlayers[position]
        holder.tvName.text = itemPlayer.name
        holder.tvPosition.text = itemPlayer.position
        holder.tvDorsal.text = "Dorsal: ${itemPlayer.dorsal}"

        if (itemPlayer.image.isEmpty()) {
            Picasso.get()
                .load(R.drawable.ic_playerdefault)
                .resize(175, 175)
                .centerCrop()
                .into(holder.ivPlayer)
        } else {
            Picasso.get()
                .load(itemPlayer.image)
                .resize(175, 175)
                .centerCrop()
                .into(holder.ivPlayer)
        }
    }
}