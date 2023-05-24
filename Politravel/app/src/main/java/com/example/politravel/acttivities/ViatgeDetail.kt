package com.example.politravel.acttivities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import com.example.politravel.R
import com.example.politravel.adapters.PuntsAdapter
import com.example.politravel.datamodels.Viatge

class ViatgeDetail: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.travel_detail)

        val intent = getIntent()
        val viatge = intent.extras?.getSerializable("viatge") as Viatge

        val titolviatge: TextView = findViewById(R.id.titoldetail)
        val durada: TextView = findViewById(R.id.duradadetail)
        val inici: TextView = findViewById(R.id.llocinicidetail)
        val final: TextView = findViewById(R.id.llocfinaldetail)
        val llistallocs: ListView = findViewById(R.id.listdetail)
        val botoreserva: Button = findViewById(R.id.botoreservadetail)
        val imatgeviatge: ImageView = findViewById(R.id.travelitemimg)
        val imatgetransport: ImageView = findViewById(R.id.transportdetail)
        val mapa: ImageView = findViewById(R.id.mapadetail)
        val update: ImageView = findViewById(R.id.updateappbar)
        update.isVisible = false
        update.isEnabled = false

        titolviatge.text = viatge.titol
        durada.text = viatge.durada.toString() + " dies"
        inici.text = viatge.inici
        final.text = viatge.final

        var Path = this.getFilesDir().toString() + "/img/foto/" + viatge.imatge
        var bitmap = BitmapFactory.decodeFile(Path)
        imatgeviatge.setImageBitmap(bitmap)

        Path = this.getFilesDir().toString() + "/img/icon/" + viatge.transport
        bitmap = BitmapFactory.decodeFile(Path)
        imatgetransport.setImageBitmap(bitmap)

        val punts: MutableList<String> = viatge.punts
        val adapter = PuntsAdapter(this, R.layout.interes_item, punts)
        llistallocs.adapter = adapter

        updateListViewHeight(llistallocs)

        mapa.setOnClickListener()
        {
            Toast.makeText(this, "El mapa es trova temporalment fora de servei", Toast.LENGTH_LONG).show()
        }

        botoreserva.setOnClickListener()
        {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("missatge", "Viatge reservat")
            setResult(RESULT_OK, intent)
            finish()
        }


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

    override fun onBackPressed()
    {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }
}