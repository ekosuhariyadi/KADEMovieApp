package com.codangcoding.kmovieapp.util

import java.io.File
import java.nio.file.Files

fun loadJSON(clazz: Class<Any>, path: String): String {
    val url = clazz.classLoader!!.getResource(path)
    val jsonFile = File(url.path)
    return String(Files.readAllBytes(jsonFile.toPath()))
}