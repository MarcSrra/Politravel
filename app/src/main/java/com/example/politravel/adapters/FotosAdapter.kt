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
import com.example.politravel.datamodels.Foto

class FotosAdapter (private val context: Context, private val fotos: MutableList<Foto>):
    RecyclerView.Adapter<FotosAdapter.FotoViewHolder>(),
    View.OnClickListener,
    View.OnLongClickListener
{
    private val layout = R.layout.foto_item
    private var clickListener : View.OnClickListener? = null
    private var longclicklistener: View.OnLongClickListener? = null
    class FotoViewHolder(val view: View): RecyclerView.ViewHolder(view)
    {
        var imatge: ImageView
        var nom: TextView

        init
        {
            imatge = view.findViewById(R.id.imgitem)
            nom = view.findViewById(R.id.imgnomitem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return FotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FotoViewHolder, position: Int)
    {
        val fotillo = fotos[position]
        bindViatge(holder, fotillo)
    }

    fun bindViatge(holder: FotoViewHolder, fotillo: Foto)
    {
        val Path = context.getFilesDir().toString() + "/img/foto/" + fotillo.imatge
        val bitmap = BitmapFactory.decodeFile(Path)
        holder.imatge?.setImageBitmap(bitmap)


        if(fotillo.nom.length > 13)
        {
            holder.nom?.text = fotillo.nom.substring(0, 10) + "..."
        }
        else
        {
            holder.nom?.text = fotillo.nom
        }
    }

    override fun getItemCount() = fotos.size

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