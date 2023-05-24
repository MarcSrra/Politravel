package com.example.politravel.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.politravel.R
import com.example.politravel.datamodels.Viatge

class ViatgeAdapter (private val context: Context, private val viatges: MutableList<Viatge>):
    RecyclerView.Adapter<ViatgeAdapter.ViatgeViewHolder>(),
    View.OnClickListener,
    View.OnLongClickListener
{
    private val layout = R.layout.travel_item
    private var clickListener : View.OnClickListener? = null
    private var longclicklistener: View.OnLongClickListener? = null
    class ViatgeViewHolder(val view: View): RecyclerView.ViewHolder(view)
    {
        var lblViatgeDurada: TextView
        var lblViatgeTitol: TextView
        var imgViatgeImage: ImageView
        var imgViatgeTransport: ImageView

        init
        {
            imgViatgeImage = view.findViewById(R.id.travelitemimg)
            lblViatgeTitol = view.findViewById(R.id.travelitemtitol)
            lblViatgeDurada = view.findViewById(R.id.travelitemdurada)
            imgViatgeTransport = view.findViewById(R.id.travelitemtransport)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViatgeViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return ViatgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViatgeViewHolder, position: Int)
    {
        val planet = viatges[position]
        bindViatge(holder, planet)
    }

    fun bindViatge(holder: ViatgeViewHolder, viatge: Viatge)
    {
        val planetPath = context.getFilesDir().toString() + "/img/foto/" + viatge.imatge
        val bitmap = BitmapFactory.decodeFile(planetPath)
        holder.imgViatgeImage?.setImageBitmap(bitmap)

        val transportPath = context.getFilesDir().toString() + "/img/icon/" + viatge.transport
        val transportbitmap = BitmapFactory.decodeFile(transportPath)
        holder.imgViatgeTransport?.setImageBitmap(transportbitmap)

        if(viatge.titol.length > 21)
        {
            holder.lblViatgeTitol?.text = viatge.titol.substring(0, 18) + "..."
        }
        else
        {
            holder.lblViatgeTitol?.text = viatge.titol
        }
        holder.lblViatgeDurada?.text = viatge.durada.toString() + " dies"
    }

    override fun getItemCount() = viatges.size

    fun setOnClickListener(listener: View.OnClickListener)
    {
        clickListener = listener
    }

    override fun onClick(view: View?)
    {
        clickListener?.onClick(view)
    }

    fun setOnLongClickListener(listener: View.OnLongClickListener)
    {
        longclicklistener = listener
    }

    override fun onLongClick(view: View?): Boolean {
        longclicklistener?.onLongClick(view)
        return true
    }

}