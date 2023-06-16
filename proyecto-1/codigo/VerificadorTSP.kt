/**
* Verificador de soluciones para el problema del vendedor viajero
* Recibe como argumentos el archivo de instancia y el archivo de solucion
* Imprime si la solucion es correcta o no y muestra algunos errores si los hay
*/

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
* Funcion: distancia2D(p1: Pair<Int, Int>, p2: Pair<Int, Int>)
* Entradas: p1 y p2, dos pares de enteros que representan las coordenadas de dos puntos
* Salidas: La distancia euclidiana entre los dos puntos
* Descripcion: Calcula la distancia euclidiana entre dos puntos en el plano
*/
fun distancia2D(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
    val x = p1.first - p2.first
    val y = p1.second - p2.second
    return Math.sqrt((x * x + y * y).toDouble()).toInt()
}

/**
* Funcion: distanciaRuta(ciudades: Array<Pair<Int, Int>>)
* Entradas: ciudades, un arreglo de pares de enteros que representan las coordenadas de las ciudades
* Salidas: La distancia total de la ruta que recorre todas las ciudades en el orden dado
* Descripcion: Calcula la distancia total de la ruta que recorre todas las ciudades en el orden dado
*/
fun distanciaRuta(ciudades: Array<Pair<Int, Int>>): Int {
    var acc = 0
    for (i in 0 until ciudades.size - 1) {
        acc += distancia2D(ciudades[i], ciudades[i + 1])
    }
    return acc
}

/**
* Funcion: checkSolution(ciudadesInstancia: Array<Pair<Int, Int>>, ciudadesSolucion: Array<Pair<Int, Int>>)
* Entradas: ciudadesInstancia, un arreglo de pares de enteros que representan las coordenadas de las ciudades de la instancia
*           ciudadesSolucion, un arreglo de pares de enteros que representan las coordenadas de las ciudades de la solucion
* Salidas: true si la solucion es correcta, false en otro caso
* Descripcion: Verifica que la solucion dada sea correcta
*/
fun checkSolution(ciudadesInstancia: Array<Pair<Int, Int>>, ciudadesSolucion: Array<Pair<Int, Int>>): Boolean {
    // Verificar que las ciudades de la ruta de la solucion sean las mismas que las de la instancia
    for (i in 0 until ciudadesInstanciaOrdenadas.size) {
        if (ciudadesInstanciaOrdenadas[i] != ciudadesSolucionOrdenadas[i]) {
            println("Ciudad ${ciudadesInstanciaOrdenadas[i]} no coincide con ${ciudadesSolucionOrdenadas[i]}")
            return false
        }
    }
    // Verificar que la distancia de la ruta de la solucion sea la misma que la de la instancia
    distanciaInstancia = distanciaRuta(ciudadesInstanciaOrdenadas)
    distanciaSolucion = distanciaRuta(ciudadesSolucionOrdenadas)
    if (distanciaInstancia != distanciaSolucion) {
        println("Distancia de instancia $distanciaInstancia no coincide con distancia de solucion $distanciaSolucion")
        return false
    }
    return true
}

/**
* Funcion: leerArchivoRuta(archivo: File)
* Entradas: archivo, un archivo de texto que contiene la instancia o la solucion
* Salidas: Un arreglo de pares de enteros que representan las coordenadas de las ciudades
* Descripcion: Lee el archivo de texto y extrae las coordenadas de las ciudades
*/
fun leerArchivoRuta(archivo: File): Array<Pair<Int, Int>> {
    val lector = BufferedReader(FileReader(archivo, Charsets.UTF_8))
    lector.readLine()
    lector.readLine()
    lector.readLine()
    val numeroCiudades = lector.readLine().split(":")[1].trim().toInt()
    lector.readLine()
    lector.readLine()
    val ciudades = Array<Pair<Int, Int>>(numeroCiudades, { Pair(0, 0) })
    for (i in 0 until numeroCiudades) {
        val ciudad = lector.readLine().split(" ")
        val x = ciudad[1].trim().toInt()
        val y = ciudad[2].trim().toInt()
        ciudades[i] = Pair(x, y)
    }
    return ciudades
}

fun main(args: Array<String>) {
    val archivoInstancia = File(args[0])
    val ciudadesInstancia = leerArchivoRuta(archivoInstancia)

    val archivoSolucion = File(args[1])
    val ciudadesSolucion = leerArchivoRuta(archivoSolucion)

    if (!checkSolution(ciudadesInstancia, ciudadesSolucion)) {
        println("Solución incorrecta")
        return
    }
    
    println("Solución correcta")
}