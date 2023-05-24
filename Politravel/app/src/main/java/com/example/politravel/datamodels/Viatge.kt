package com.example.politravel.datamodels

import java.io.Serializable

class Viatge (var id: Int, var titol: String, var transport: String, var imatge: String, var durada: String, var inici: String, var final: String, var punts: ArrayList<String>): Serializable