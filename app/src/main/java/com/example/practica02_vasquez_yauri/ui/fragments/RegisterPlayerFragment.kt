package com.example.practica02_vasquez_yauri.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica02_vasquez_yauri.R
import com.example.practica02_vasquez_yauri.adapter.PlayerAdapter
import com.example.practica02_vasquez_yauri.model.PlayerModel
import com.google.firebase.firestore.FirebaseFirestore


class RegisterPlayerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        val View:View =inflater.inflate(R.layout.fragment_register_player, container, false)

        val etNamePlayer: EditText = View.findViewById(R.id.etNamePlayer)
        val etPositionPlayer: EditText = View.findViewById(R.id.etPosition)
        val etDorsalPlayer: EditText = View.findViewById(R.id.etDorsal)
        val etImageUrlPlayer: EditText = View.findViewById(R.id.etUrlImage)
        val btnRegisterPlayer: Button = View.findViewById(R.id.btGuardar)
        val spCountry: Spinner = View.findViewById(R.id.spCountrys)
        val etTeamName: EditText = View.findViewById(R.id.etTeamName)



        val db = FirebaseFirestore.getInstance()
        var spCountryValue: String = ""
        var lstPlayers: List<PlayerModel>
        var rvPlayer: RecyclerView = View.findViewById(R.id.rvListPlayers)

        db.collection("Países").get()
            .addOnSuccessListener { result ->
                val countryList = mutableListOf<String>()
                for (document in result) {
                    countryList.add(document.getString("Description") ?: "")
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spCountry.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error getting countries: ${exception.message}", Toast.LENGTH_LONG).show()
            }

        spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                spCountryValue = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing
            }
        }
        btnRegisterPlayer.setOnClickListener() {
            // Verifica si todos los campos están llenos
            if (etNamePlayer.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese el nombre del jugador", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (etPositionPlayer.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese la posición del jugador", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (etTeamName.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese el nombre del equipo", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (etDorsalPlayer.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese el dorsal del jugador", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (etImageUrlPlayer.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese la URL de la foto del jugador", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (spCountryValue.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, seleccione el país", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Si todos los campos están llenos, guarda el jugador
            val player = hashMapOf(
                "name" to etNamePlayer.text.toString(),
                "position" to etPositionPlayer.text.toString(),
                "equipo" to etTeamName.text.toString(),
                "dorsal" to etDorsalPlayer.text.toString(),
                "foto" to etImageUrlPlayer.text.toString(),
                "country" to spCountryValue
            )

            db.collection("Jugadores")
                .add(player)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(requireContext(), "Player registered successfully", Toast.LENGTH_LONG).show()

                    // Limpia los campos del formulario
                    etNamePlayer.text.clear()
                    etPositionPlayer.text.clear()
                    etTeamName.text.clear()
                    etDorsalPlayer.text.clear()
                    etImageUrlPlayer.text.clear()
                    spCountry.setSelection(0)

                    // Oculta el teclado
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error registering player: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

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

        return View
    }

}