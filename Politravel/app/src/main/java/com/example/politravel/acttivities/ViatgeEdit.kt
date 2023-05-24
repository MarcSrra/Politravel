package com.example.politravel.acttivities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.EGLImage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.example.politravel.R
import com.example.politravel.adapters.PuntsAdapter
import com.example.politravel.datamodels.Viatge
import com.google.gson.Gson
import java.io.FileWriter
import java.util.ArrayList

class ViatgeEdit : AppCompatActivity() {
    lateinit var fotoviatge: String

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {

        if(it.resultCode == RESULT_OK)
        {
            val missatge = it.data?.getStringExtra("missatge")
            fotoviatge = missatge.toString()
            Actualitzafoto()
        }
        else if(it.resultCode == RESULT_CANCELED)
        {

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.travel_edit)

        val intent = getIntent()
        //Variables generals
        val nou = intent.getBooleanExtra("nou", true)
        var llistaviatges = intent.extras?.getSerializable("llistaviatges") as MutableList<Viatge>

        //Variables XML

        val titol:EditText = findViewById(R.id.edittextnompaquet)
        val durada:EditText = findViewById(R.id.edittextdurada)
        val transport:Spinner = findViewById(R.id.spinneredit)
        val imgtransport:ImageView = findViewById(R.id.imatgetransportedit)
        val iniciviatge:EditText = findViewById(R.id.inicieditedit)
        val finalviatge:EditText = findViewById(R.id.finaleditedit)
        val mapa:ImageView = findViewById(R.id.mapaedit)
        val puntinteres:EditText = findViewById(R.id.puntinteresedit)
        val botoafegir:ImageButton = findViewById(R.id.buttonafegir)
        val llistapunts:ListView = findViewById(R.id.listedit)
        val botoguardar:Button = findViewById(R.id.botoafegiredit)
        val botoborrar:ImageView = findViewById(R.id.deleteappbar)
        val botoimatge: Button = findViewById(R.id.botoimatgeedit)

        //Variables d'us dins Activity
        var id: Int = llistaviatges.size
        var viatge: Viatge
        fotoviatge = "noimatge.png"
        val opcionstransport: List<String> = listOf("Avió", "Bus", "Tren", "Vaixell")
        var spinnerposition: Int
        var punts: MutableList<String> = mutableListOf()
        var adapter = PuntsAdapter(this, R.layout.interes_item, punts)
        llistapunts.adapter = adapter
        updateListViewHeight(llistapunts)
        //Preparació spinner
        if (transport != null)
        {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionstransport)
            transport.adapter = adapter
        }


        if (!nou)
        {
            botoguardar.setText(R.string.guardar)
            viatge = intent.extras?.getSerializable("viatge") as Viatge
            id = viatge.id
            fotoviatge = viatge.imatge
            Actualitzafoto()
            titol.setText(viatge.titol)
            durada.setText(viatge.durada)

            spinnerposition = 0
            val transportsplit = viatge.transport.split(".")
            for(s in opcionstransport)
            {
                if(transportsplit[0] == opcionstransport[spinnerposition].toLowerCase())
                {
                    transport.setSelection(spinnerposition)
                }
                spinnerposition++
            }

            imgtransport.setImageBitmap(ActualitzaImgTransport(transport.getSelectedItem().toString()))

            iniciviatge.setText(viatge.inici)
            finalviatge.setText(viatge.final)

            punts = viatge.punts
            adapter = PuntsAdapter(this, R.layout.interes_item, punts)
            llistapunts.adapter = adapter
            updateListViewHeight(llistapunts)

        }
        else
        {
            viatge = Viatge(id, titol.text.toString(), gettransport(transport),
                fotoviatge, durada.text.toString(),iniciviatge.text.toString(),finalviatge.text.toString(),
                ArrayList(punts))
            Actualitzafoto()
        }

        //Amagar teclat
        titol.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        durada.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        iniciviatge.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        finalviatge.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        puntinteres.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        //Listeners
        transport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                imgtransport.setImageBitmap(ActualitzaImgTransport(opcionstransport[position]))
            }
        }

        mapa.setOnClickListener()
        {
            Toast.makeText(this, "El mapa es trova temporalment fora de servei", Toast.LENGTH_LONG).show()
        }

        botoafegir.setOnClickListener()
        {
            if(puntinteres.text.toString() != "")
            {
                punts.add(puntinteres.text.toString())
                updateListViewHeight(llistapunts)
                puntinteres.setText("")
            }
        }


        llistapunts.setOnItemLongClickListener ()
        {
                _, _, i, _ ->

            punts.remove(punts[i])
            updateListViewHeight(llistapunts)

            true
        }

        botoguardar.setOnClickListener()
        {
            if(!checkerrors(titol, durada, iniciviatge, finalviatge))
            {
                val viatgeguardar = Viatge(id, titol.text.toString(), gettransport(transport),
                    fotoviatge, durada.text.toString(),iniciviatge.text.toString(),finalviatge.text.toString(),
                    ArrayList(punts))

                val missatge:String
                if(nou)
                {
                    llistaviatges.add(viatgeguardar)
                    escribirJson(llistaviatges)
                    missatge = "Paquet creat"
                }
                else
                {
                    val viatgesguardar = actualitzarviatge(llistaviatges, viatgeguardar)
                    escribirJson(viatgesguardar)
                    missatge = "Paquet actualitzat"
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("missatge", missatge)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        botoborrar.setOnClickListener { view ->
            if(nou)
            {
                setResult(RESULT_CANCELED)
                finish()
            }
            else
            {
                val viatgesguardar = borrarviatge(llistaviatges, viatge)
                escribirJson(viatgesguardar)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("missatge", "Paquet borrat")
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        botoimatge.setOnClickListener()
        {
            val intent = Intent(this, FotoActivity::class.java)
            getResult.launch(intent)
        }

    }


    private fun Actualitzafoto() {
        val imgviatge:ImageView = findViewById(R.id.traveleditimg)
        val Path = this.getFilesDir().toString() + "/img/foto/" + fotoviatge
        val bitman = BitmapFactory.decodeFile(Path)
        imgviatge.setImageBitmap(bitman)
    }

    private fun gettransport(transport: Spinner): String {
        var t:String = transport.getSelectedItem().toString().toLowerCase()

        if(t == "avió")
        {
            t = "avio"
        }

        return t + ".png"
    }

    private fun borrarviatge(llistaviatges: MutableList<Viatge>, viatge: Viatge): MutableList<Viatge> {
        var novallista: MutableList<Viatge> = mutableListOf()

        for(v in llistaviatges)
        {
            if(v.id != viatge.id)
            {
                novallista.add(v)
            }
        }

        return novallista
    }

    private fun actualitzarviatge(llistaviatges: MutableList<Viatge>, viatge: Viatge): MutableList<Viatge> {
        var novallista: MutableList<Viatge> = mutableListOf()

        for(v in llistaviatges)
        {
            if(v.id == viatge.id)
            {
                novallista.add(viatge)
            }
            else
            {
                novallista.add(v)
            }
        }

        return novallista
    }

    private fun checkerrors(titol: EditText, durada: EditText, iniciviatge: EditText, finalviatge: EditText): Boolean {
        var errors = false

        titol.setBackgroundResource(R.drawable.edittextbackground)
        durada.setBackgroundResource(R.drawable.edittextbackground)
        iniciviatge.setBackgroundResource(R.drawable.edittextbackground)
        finalviatge.setBackgroundResource(R.drawable.edittextbackground)

        if(titol.text.toString() == "")
        {
            titol.setBackgroundResource(R.drawable.edittextbackgrounderror)
            errors = true
        }
        if(durada.text.toString() == "")
        {
            durada.setBackgroundResource(R.drawable.edittextbackgrounderror)
            errors = true
        }
        if(iniciviatge.text.toString() == "")
        {
            iniciviatge.setBackgroundResource(R.drawable.edittextbackgrounderror)
            errors = true
        }
        if(finalviatge.text.toString() == "")
        {
            finalviatge.setBackgroundResource(R.drawable.edittextbackgrounderror)
            errors = true
        }

        if(errors)
        {
            Toast.makeText(this, "Per guardar els canvis omple els paquets marcats", Toast.LENGTH_LONG).show()
        }

        return errors
    }

    private fun ActualitzaImgTransport(selectedItem: String) : Bitmap {
        val transportPath:String
        var transport: String = selectedItem

        if(transport == "Avió")
        {
            transport = "Avio"
        }

        if (selectedItem.endsWith(".png"))
        {
            transportPath = this.getFilesDir().toString() + "/img/icon/" + transport.toLowerCase()
        }
        else
        {
            transportPath = this.getFilesDir().toString() + "/img/icon/" + transport.toLowerCase() + ".png"
        }

        return BitmapFactory.decodeFile(transportPath)
    }



    fun updateListViewHeight(myListView: ListView)
    {
        val myListAdapter: ListAdapter = myListView.getAdapter() ?: return
        // get listview height
        var totalHeight = 0
        val adapterCount: Int = myListAdapter.getCount()
        for (size in 0 until adapterCount) {
            val listItem: View = myListAdapter.getView(size, null, myListView)
            listItem.measure(0, 0)
            totalHeight += listItem.getMeasuredHeight()
        }
        // Change Height of ListView
        val params: ViewGroup.LayoutParams = myListView.getLayoutParams()
        params.height = (totalHeight
                + myListView.getDividerHeight() * adapterCount)
        myListView.setLayoutParams(params)
    }

    fun escribirJson(viatgesList: MutableList<Viatge>) {
        val jsonFilePath = "$filesDir/viatges.json"
        val jsonFile = FileWriter(jsonFilePath)
        var gson = Gson()
        var jsonElement = gson.toJson(viatgesList)
        jsonFile.write(jsonElement)
        jsonFile.close()
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

}