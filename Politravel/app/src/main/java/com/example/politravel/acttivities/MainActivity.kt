package com.example.politravel.acttivities

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.politravel.R
import com.example.politravel.datamodels.Viatge
import com.example.politravel.adapters.ViatgeAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileReader
import java.io.FileWriter

class MainActivity : AppCompatActivity() {
    lateinit var viatges: MutableList<Viatge>

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {

        if(it.resultCode == RESULT_OK)
        {
            val missatge = it.data?.getStringExtra("missatge")
            Toast.makeText(this, missatge, Toast.LENGTH_LONG).show()
            Polillista()
        }
        else if(it.resultCode == RESULT_CANCELED)
        {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val boto: ImageButton = findViewById(R.id.buttonafegir)
        val update: ImageView = findViewById(R.id.updateappbar)

        Polillista()

        boto.setOnClickListener()
        {

            val intent = Intent(this, ViatgeEdit::class.java)
            intent.putExtra("nou", true)
            intent.putExtra("llistaviatges", viatges as java.io.Serializable)
            getResult.launch(intent)
        }

        update.setOnClickListener { view ->
            escribirJson(getbackup())
            val mIntent = intent
            finish()
            startActivity(mIntent)
        }

    }

    private fun Polillista()
    {
        viatges = getViatgesJSON()
        val lstviatges = findViewById<RecyclerView>(R.id.gridnovedades)

        val adapter = ViatgeAdapter(this, viatges)
        lstviatges.hasFixedSize()
        lstviatges.layoutManager = GridLayoutManager(this, 1)
        lstviatges.adapter = adapter

        adapter.setOnClickListener()
        {
            val intent = Intent(this, ViatgeDetail::class.java)
            intent.putExtra("viatge", viatges[lstviatges.getChildAdapterPosition(it)] as java.io.Serializable)
            getResult.launch(intent)
        }


        adapter.setOnLongClickListener()
        {
            val intent = Intent(this, ViatgeEdit::class.java)
            intent.putExtra("nou", false)
            intent.putExtra("llistaviatges", viatges as java.io.Serializable)
            intent.putExtra("viatge", viatges[lstviatges.getChildAdapterPosition(it)] as java.io.Serializable)
            getResult.launch(intent)
            true
        }
    }

    fun getViatgesJSON(): MutableList<Viatge> {
        val jsonFilePath = "$filesDir/viatges.json"
        val jsonFile = FileReader(jsonFilePath)
        val listPlayerType = object : TypeToken<MutableList<Viatge>>() {}.type
        val llista: MutableList<Viatge> =  Gson().fromJson(jsonFile, listPlayerType)
        var llistadisplay: MutableList<Viatge> = mutableListOf()
        var counter: Int = 0
        for(v in llista)
        {
            v.id = counter
            llistadisplay.add(v)
            counter++
        }

        return llistadisplay
    }

    fun getbackup(): MutableList<Viatge> {
        val jsonFilePath = "$filesDir/viatgesbackup.json"
        val jsonFile = FileReader(jsonFilePath)
        val listPlayerType = object : TypeToken<MutableList<Viatge>>() {}.type
        return Gson().fromJson(jsonFile, listPlayerType)
    }

    fun escribirJson(viatgesList: MutableList<Viatge>) {
        val jsonFilePath = "$filesDir/viatges.json"
        val jsonFile = FileWriter(jsonFilePath)
        var gson = Gson()
        var jsonElement = gson.toJson(viatgesList)
        jsonFile.write(jsonElement)
        jsonFile.close()
    }

}