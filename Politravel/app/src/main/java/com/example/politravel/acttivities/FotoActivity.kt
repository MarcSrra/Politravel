package com.example.politravel.acttivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.politravel.R
import com.example.politravel.adapters.FotosAdapter
import com.example.politravel.adapters.ViatgeAdapter
import com.example.politravel.datamodels.Foto
import com.example.politravel.datamodels.Viatge
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileReader

class FotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.foto_activity)

        val update: ImageView = findViewById(R.id.updateappbar)
        update.isVisible = false
        update.isEnabled = false

        val fotos = getfotillos()
        val lstfotos = findViewById<RecyclerView>(R.id.listfotos)

        val adapter = FotosAdapter(this, fotos)
        lstfotos.hasFixedSize()
        lstfotos.layoutManager = GridLayoutManager(this, 2)
        lstfotos.adapter = adapter

        adapter.setOnClickListener()
        {
            val intent = Intent(this, ViatgeEdit::class.java)
            intent.putExtra("missatge", fotos[lstfotos.getChildAdapterPosition(it)].imatge)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun getfotillos(): MutableList<Foto> {
        val jsonFilePath = "$filesDir/fotos.json"
        val jsonFile = FileReader(jsonFilePath)
        val listPlayerType = object : TypeToken<MutableList<Foto>>() {}.type
        return Gson().fromJson(jsonFile, listPlayerType)
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }
}