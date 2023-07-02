// Laboratorio de la semana 8 de Algoritmos y Estructuras de Datos II (CI-2692).
// Autores: Juan Cuevas (19-10056) y Luis Isea (19-10175).

/**
* Clase CuckooHashTable, que representa una tabla de hash con resolución de colisiones por el método de Cuckoo Hashing
* @property conocidas: CircularList -> Lista enlazada que contiene las claves de los elementos que ya fueron insertados en la tabla de hash
* @property tabla1: Array<CuckooHashTableEntry> -> Arreglo que contiene los nodos que representan la primera tabla de hash
* @property tabla2: Array<CuckooHashTableEntry> -> Arreglo que contiene los nodos que representan la segunda tabla de hash
*/
class CuckooHashTable() {
    // conocidas: CircularList -> Lista enlazada que contiene las claves de los elementos que ya fueron insertados en la tabla de hash
    private var conocidas: CircularList = CircularList()

    // tabla1: Array<CuckooHashTableEntry> -> Arreglo que contiene los nodos que representan la primera tabla de hash
    private var tabla1: Array<CuckooHashTableEntry> = Array(7) { CuckooHashTableEntry(null, null) }

    // tabla2: Array<CuckooHashTableEntry> -> Arreglo que contiene los nodos que representan la segunda tabla de hash
    private var tabla2: Array<CuckooHashTableEntry> = Array(7) { CuckooHashTableEntry(null, null) }

    // numElementos: Int -> Número de elementos que hay en la tabla de hash
    private var numElementos: Int = 0

    // A: Double -> Constante que se usa en la función hash por el método de la multiplicación
    // Este es el valor de la constante sugerido por Donald Knuth
    private val A: Double = 0.6180339887

    // Métodos de la clase CuckooHashTable

    // h1(clave: Int): Int -> Función hash que se usa para la primera tabla de hash
    private fun h1(clave: Int): Int {
        // Se usa el método de la división
        return (clave % this.hashSize()).toInt()
    }

    // h2(clave: Int): Int -> Función hash que se usa para la segunda tabla de hash
    private fun h2(clave: Int): Int {
        // Se usa el método de la multiplicación
        return (((clave * this.A) % 1) * this.hashSize()).toInt()
    }

    // incr(size: Int): Int -> Función que devuelve el nuevo tamaño de cada tabla de hash
    private fun incr(size: Int): Int {
        return ((size + 16) * 3/2).toInt()
    }

    // swap(clave: Int?, valor: String?, tabla: Array<CuckooHashTableEntry>, indice: Int): CuckooHashTableEntry -> Función que intercambia la clave y el valor especificados en la tabla posición dada de la tabla de hash, y devuelve la clave y el valor que se reemplazaron
    private fun swap(clave: Int?, valor: String?, tabla: Array<CuckooHashTableEntry>, indice: Int): CuckooHashTableEntry {
        // Obtenemos el valor de la clave y el valor en la tabla de hash
        val claveVieja = tabla[indice].obtenerClave()
        val valorViejo = tabla[indice].obtenerValor()

        // Reemplazamos el valor de la clave y el valor en la tabla de hash
        tabla[indice].cambiarValor(valor!!)
        tabla[indice].cambiarClave(clave!!)

        // Retornamos el valor de la clave y el valor que se reemplazó
        return CuckooHashTableEntry(claveVieja, valorViejo)
    }


    // obtenerFactorCarga(): Double -> Función que devuelve el factor de carga del cuckoo hash
    fun obtenerFactorCarga(): Double {
        return (this.obtenerNumElementos().toDouble() / this.totalHashSize().toDouble())
    }

    // rehash(): Unit -> Función que hace rehash del cuckoo hash
    private fun rehash(): Unit {
        // Se obtiene el nuevo tamaño de las tablas de hash
        val newSize = incr(this.hashSize())

        // Se crea un nuevo cuckoo hash con el nuevo tamaño
        this.tabla1 = Array(newSize) { CuckooHashTableEntry(null, null) }
        this.tabla2 = Array(newSize) { CuckooHashTableEntry(null, null) }

        // Reiniciamos el número de elementos que hay en la tabla
        this.numElementos = 0

        // Se obtiene la primera clave de la lista de claves conocidas
        var claveConocida = this.conocidas.obtenerPrimero()

        // Se recorre la lista de claves conocidas
        while (claveConocida != this.conocidas.sentinel) {
            // Se obtiene la clave y el valor de la clave conocida
            val clave = claveConocida?.obtenerClave()!!
            val valor = claveConocida.obtenerValor()!!

            // Se agrega la clave conocida al nuevo cuckoo hash y se especifica que no se vuelva a agregar la clave conocida a conocidas
            this.agregar(clave, valor, false)

            // Se obtiene la siguiente clave de la lista de claves conocidas
            claveConocida = claveConocida.next
        }
    }

    // agregar(clave: Int, valor: String, agregarAConocidas: Boolean = true): Unit -> Función que agrega una clave al cuckoo hash
    // agregarAConocidas: Boolean = true, asigna el valor por defecto en caso de no ser especificado
    fun agregar(clave: Int, valor: String, agregarAConocidas: Boolean = true): Unit {
        // Si la clave ya está en la tabla de hash, no se agrega y se retorna false
        if(this.existe(clave)) return

        // Si el factor de carga es mayor o igual a 0.7, se hace rehash
        if(this.obtenerFactorCarga() >= 0.7) this.rehash()

        // Se calcula la posible posición donde se vaya a agregar el nuevo nodo
        var indice = this.h1(clave)

        // Se declaran las variables necesarias
        var viejoNodo: CuckooHashTableEntry
        var claveAInsertar = clave
        var valorAInsertar = valor

        // Se establece 1000 como el número máximo de intentos para evitar un bucle infinito
        for (i in 0 until 1000) {
            // Intentamos agregar el nodo a la primera tabla de hash
            viejoNodo = swap(claveAInsertar, valorAInsertar, tabla1, indice)
            // Verificamos si el nodo que estaba anteriormente en la posición donde intercambiamos el nodo que queremos agregar está vacío o no
            if (viejoNodo.esVacio()) {
                // Si está vacío, quiere decir que pudimos agregar adecuadamente el nodo
                if (agregarAConocidas) this.conocidas.agregarAlFinal(clave, valor)
                numElementos++
                return
            }
            // En caso de que no esté vacío
            // Actualizamos la información del nodo que queremos agregar ahora
            claveAInsertar = viejoNodo.obtenerClave()!!
            valorAInsertar = viejoNodo.obtenerValor()!!
            indice = this.h2(claveAInsertar)

            // Y entonces intentamos agregar el nodo actualizado a la segunda tabla de hash
            viejoNodo = swap(claveAInsertar, valorAInsertar, tabla2, indice)
            // Verificamos si el nodo que estaba anteriormente en la posición donde intercambiamos el nodo que queremos agregar está vacío o no
            if (viejoNodo.esVacio()) {
                // Si está vacío, quiere decir que pudimos agregar adecuadamente el nodo
                // Si se pidió agregar la clave del nuevo nodo a this.conocidas, se agrega. En caso contrario se omite
                if (agregarAConocidas) this.conocidas.agregarAlFinal(clave, valor)
                numElementos++
                return
            }
            // En caso de que no esté vacío
            // Actualizamos la información del nodo que queremos agregar ahora
            claveAInsertar = viejoNodo.obtenerClave()!!
            valorAInsertar = viejoNodo.obtenerValor()!!
            indice = this.h1(claveAInsertar)
            // Y volvemos a repetir el bucle hasta que se pueda agregar adecuadamente el nodo o se llegue al número máximo de intentos
        }

        // Si no se pudo agregar el nodo al cuckoo hash después del número máximo de intentos, hacemos rehash
        this.rehash()

        // Finalmente, luego del rehash, volvemos a intentar agregar el nodo al cuckoo hash
        this.agregar(clave, valor, agregarAConocidas)
    }

    // buscar(clave: Int): String? -> Función que busca una clave en la tabla de hash. Retorna el valor asociado a la clave si la encuentra y null en caso contrario
    fun buscar(clave: Int): String? {
        // Se calculan las posibles posiciones de la clave
        val indice1 = h1(clave)
        val indice2 = h2(clave)

        // Se busca la clave en la tabla de hash
        if (this.tabla1[indice1].obtenerClave() == clave) {
            return this.tabla1[indice1].obtenerValor()
        } else if (this.tabla2[indice2].obtenerClave() == clave) {
            return this.tabla2[indice2].obtenerValor()
        } else return null
    }

    // eliminar(clave: Int): Unit -> Función que elimina una clave de la tabla de hash
    fun eliminar(clave: Int): Unit {
        // Se calculan las posibles posiciones de la clave
        val indice1 = h1(clave)
        val indice2 = h2(clave)

        // Se elimina la clave de la tabla de hash, si existe en ella
        if (this.tabla1[indice1].obtenerClave() == clave) {
            this.tabla1[indice1].cambiarClave(null)
            this.tabla1[indice1].cambiarValor(null)
            this.conocidas.eliminar(clave)
            numElementos--
        } else if (this.tabla2[indice2].obtenerClave() == clave) {
            this.tabla2[indice2].cambiarClave(null)
            this.tabla2[indice2].cambiarValor(null)
            this.conocidas.eliminar(clave)
            numElementos--
        }
    }

    // existe(clave: Int): Boolean -> Función que verifica si una clave existe en la tabla de hash
    fun existe(clave: Int): Boolean {
        // Se calculan las posibles posiciones de la clave
        val indice1 = h1(clave)
        val indice2 = h2(clave)

        // Se verifica si la clave existe en la tabla de hash
        return (this.tabla1[indice1].obtenerClave() == clave || this.tabla2[indice2].obtenerClave() == clave)
    }

    // obtenerNumElementos(): Int -> Función que devuelve el número de elementos de la tabla de hash
    fun obtenerNumElementos(): Int {
        return this.numElementos
    }

    // hashSize(): Int -> Función que devuelve el tamaño individual de las tablas de hash
    fun hashSize(): Int {
        return this.tabla1.size
    }

    // totalHashSize(): Int -> Función que devuelve el tamaño combinado de las tablas de hash
    fun totalHashSize(): Int {
        return this.tabla1.size + this.tabla2.size
    }

    // obtenerNumClavesConocidas(): Int -> Función que retorna cuántas claves conocidas por el diccionario hay
    fun obtenerNumClavesConocidas(): Int {
        return this.conocidas.getSize()
    }

    // override fun toString(): String -> Función que devuelve una representación en String de la tabla de hash
    override fun toString(): String {
        var str = "Claves conocidas: ${this.conocidas}\n"
        str += "T1  ---  T2\n"
        for (i in 0 until this.hashSize()) {
            str += "${this.tabla1[i]}  ---  ${this.tabla2[i]}\n"
        }
        return str
    }
}

// createDictionaryCuckoo(): CuckooHashTable -> Función que crea un diccionario vacío basado en cuckoo hashing
fun createDictionaryCuckoo(): CuckooHashTable {
    return CuckooHashTable()
}
