package com.example.practica02_vasquez_yauri.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica02_vasquez_yauri.R
import com.example.practica02_vasquez_yauri.adapter.PlayerAdapter
import com.example.practica02_vasquez_yauri.model.PlayerModel
import com.google.firebase.firestore.FirebaseFirestore


class GestionPlayerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val View:View= inflater.inflate(R.layout.fragment_gestion_player, container, false)


        val svPlayer: SearchView =View.findViewById(R.id.svPlayer)

        val db = FirebaseFirestore.getInstance()
        var lstPlayers: List<PlayerModel> = listOf()
        var rvPlayer: RecyclerView = View.findViewById(R.id.rvPlayersAll)

        db.collection("Jugadores")

            .addSnapshotListener() { snap, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                lstPlayers = snap!!.documents.map {document ->

                    PlayerModel(
                        document.id,
                        document["country"].toString(),
                        document["name"].toString(),
                        document["equipo"].toString(),
                        document["position"].toString(),
                        document["dorsal"].toString(),
                        document["foto"].toString()


                    )
                }
                rvPlayer.adapter = PlayerAdapter(lstPlayers)
                rvPlayer.layoutManager = GridLayoutManager(requireContext(), 2)
                //rvPlayer.layoutManager = LinearLayoutManager(requireContext())
            }
        //buscar jugadores dentro del recyclerview desde  svPlayer
        svPlayer.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    rvPlayer.adapter = PlayerAdapter(lstPlayers.filter { player ->
                        player.name.contains(newText.toString(), ignoreCase = true)
                    })
                } else {
                    rvPlayer.adapter = PlayerAdapter(lstPlayers)
                }
                return true
            }
        })



        return View
    }




}