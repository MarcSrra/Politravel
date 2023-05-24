package com.example.politravel.adapters

import android.widget.ArrayAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.politravel.R

class PuntsAdapter(context: Context, val layout: Int, val punts: MutableList<String>):
    ArrayAdapter<String>(context, layout, punts)
{
    override fun getView(position: Int, convertView: View?, parent:ViewGroup) : View
    {
        var view: View

        if (convertView != null)
        {
            view = convertView
        }
        else
        {
            view = LayoutInflater.from(getContext()).inflate(layout, parent, false)
        }

        bindVideojoc(view, punts[position])

        return view
    }

    fun bindVideojoc(view: View, punt: String)
    {
        val textpunt = view.findViewById<TextView>(R.id.puntinterestext)
        textpunt.text = punt
    }
}