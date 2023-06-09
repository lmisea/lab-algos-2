/*
 * Laboratorio de la semana 8 de Algoritmos y Estructuras de Datos II (CI-2692).
 * Autores: Juan Cuevas (19-10056) y Luis Isea (19-10175).
 */

/**
 * Clase CircularList, que representa una lista circular doblemente enlazada
 * Los nodos de la lista son de la clase HashTableEntry
 * @property sentinel: Nodo -> El nodo sentinela de la lista
 * @property size: Int -> El tamaño de la lista
 */
class CircularList() {
    // sentinel: HashTableEntry -> El nodo sentinela de la lista
    var sentinel: HashTableEntry = HashTableEntry(null, null)

    // size: Int -> El tamaño de la lista
    private var size: Int = 0

    // Metodos de la clase CircularList

    // getSize(): Int -> Devuelve el tamaño de la lista
    fun getSize(): Int {
        return size
    }

    // Metodo init, que se ejecuta al crear un objeto de la clase CircularList. Este método es el llamado constructor
    init {
        // Cambiamos el nodo siguiente y anterior del nodo sentinela para que apunte a si mismo
        sentinel.cambiarNext(sentinel)
        sentinel.cambiarPrev(sentinel)
    }

    // estaVacia(): Boolean -> Devuelve true si la lista esta vacia, false en caso contrario
    fun estaVacia(): Boolean {
        return size == 0
    }

    // agregarAlFrente(clave:Int, valor: String): Unit -> Agrega un nodo con la clave y valor dados al frente de la lista
    fun agregarAlFrente(clave: Int, valor: String) {
        // Creamos el nuevo nodo
        val nuevoNodo = HashTableEntry(clave, valor)
        nuevoNodo.cambiarPrev(sentinel) // Cambiamos el nodo anterior del nuevo nodo para que apunte al nodo sentinela
        nuevoNodo.cambiarNext(sentinel.next!!) // El nodo siguiente al nodo que agregamos es el siguiente del sentinela
        sentinel.next!!.cambiarPrev(nuevoNodo) // Cambiamos el nodo anterior del nodo siguiente al nuevo nodo para que apunte al nuevo nodo
        sentinel.cambiarNext(nuevoNodo) // Cambiamos el nodo siguiente del nodo sentinela para que apunte al nuevo nodo
        size++ // Aumentamos el tamaño de la lista
    }

    // agregarAlFinal(clave: Int, valor: String): Unit -> Agrega un nodo con la clave y valor dados al final de la lista
    fun agregarAlFinal(clave: Int, valor: String) {
        // Creamos el nuevo nodo
        val nuevoNodo = HashTableEntry(clave, valor)
        nuevoNodo.cambiarPrev(sentinel.prev!!) // Cambiamos el nodo anterior del nuevo nodo para que apunte al nodo anterior al nodo sentinela
        nuevoNodo.cambiarNext(sentinel) // Ahora el nodo siguiente al nodo que agregamos es el sentinela
        sentinel.prev!!.cambiarNext(nuevoNodo) // Cambiamos el nodo siguiente del nodo anterior al nodo sentinela para que apunte al nuevo nodo
        sentinel.cambiarPrev(nuevoNodo) // Cambiamos el nodo anterior del nodo sentinela para que apunte al nuevo nodo
        size++ // Aumentamos el tamaño de la lista
    }

    // eliminar(clave: Int): Boolean -> Elimina el nodo con la clave dada de la lista, devuelve true si se elimino el nodo, false en caso contrario
    fun eliminar(clave: Int): Boolean {
        // Si la lista esta vacia, no hacemos nada
        if (estaVacia()) return false

        // Declaramos el nodo actual como el nodo siguiente al nodo sentinela
        var nodoActual = sentinel.next
        // Recorremos la lista hasta que el nodo actual sea el nodo sentinela
        while (nodoActual != sentinel) {
            // Si la clave del nodo actual es igual a la clave dada, eliminamos el nodo actual
            if (nodoActual?.obtenerClave() == clave) {
                nodoActual.prev?.cambiarNext(nodoActual.next!!)
                nodoActual.next?.cambiarPrev(nodoActual.prev!!)
                size-- // Reducimos el tamaño de la lista
                return true
            }
            // Si no, cambiamos el nodo actual al nodo siguiente al nodo actual
            nodoActual = nodoActual?.next
        }
        return false
    }

    // eliminarPrimero(): Unit -> Elimina el primer nodo de la lista
    fun eliminarPrimero() {
        // Si la lista esta vacia, no hacemos nada
        if (estaVacia()) return

        // Cambiamos el nodo siguiente y anterior del nodo siguiente al nodo sentinela
        sentinel.next!!.next!!.cambiarPrev(sentinel)
        sentinel.cambiarNext(sentinel.next!!.next!!)
        size-- // Reducimos el tamaño de la lista
    }

    // eliminarUltimo(): Unit -> Elimina el ultimo nodo de la lista
    fun eliminarUltimo() {
        // Si la lista esta vacia, no hacemos nada
        if (estaVacia()) return

        // Cambiamos el nodo siguiente y anterior del nodo anterior al nodo sentinela
        sentinel.prev!!.prev!!.cambiarNext(sentinel)
        sentinel.cambiarPrev(sentinel.prev!!.prev!!)
        size-- // Reducimos el tamaño de la lista
    }

    // buscar(clave: Int): String? -> Devuelve el valor del nodo con la clave dada, o si el nodo con dicha clave no se encuentra en la lista, devuelve null
    fun buscar(clave: Int): String? {
        // Si la lista esta vacia, retornamos null
        if (estaVacia()) return null

        // Declaramos el nodo actual como el nodo siguiente al nodo sentinela
        var nodoActual = sentinel.next
        // Recorremos la lista hasta que el nodo actual sea el nodo sentinela
        while (nodoActual != sentinel) {
            if (nodoActual!!.obtenerClave() == clave) {
                return nodoActual.obtenerValor()
            }
            // Si no, cambiamos el nodo actual al nodo siguiente al nodo actual
            nodoActual = nodoActual.next
        }

        // Si no se encuentra el nodo con la clave dada, devolvemos null
        return null
    }

    // existe(clave: Int): Boolean -> Devuelve true si existe un nodo con la clave dada, false en caso contrario
    fun existe(clave: Int): Boolean {
        // Si la lista esta vacia, retornamos false
        if (estaVacia()) return false

        // Declaramos el nodo actual como el nodo siguiente al nodo sentinela
        var nodoActual = sentinel.next
        // Recorremos la lista hasta que el nodo actual sea el nodo sentinela
        while (nodoActual != sentinel) {
            if (nodoActual!!.obtenerClave() == clave) {
                return true
            }
            // Si no, cambiamos el nodo actual al nodo siguiente al nodo actual
            nodoActual = nodoActual.next
        }

        // Si no se encuentra el nodo con la clave dada, devolvemos null
        return false
    }

    // obtenerPrimero(): HashTableEntry? -> Devuelve el primer nodo de la lista, o null si la lista esta vacia
    fun obtenerPrimero(): HashTableEntry? {
        // Si la lista esta vacia, retornamos null
        if (estaVacia()) return null
        return sentinel.next
    }

    // obtenerUltimo(): HashTableEntry? -> Devuelve el ultimo nodo de la lista, o null si la lista esta vacia
    fun obtenerUltimo(): HashTableEntry? {
        // Si la lista esta vacia, retornamos null
        if (estaVacia()) return null
        return sentinel.prev
    }

    // toString(): String -> Devuelve una representacion en String de la lista
    // override quiere decir que estamos sobreescribiendo el metodo toString de la clase Any
    // Así, cuando llamemos al metodo toString de una lista, se ejecutara este metodo en vez del metodo toString de la clase Any
    override fun toString(): String {
        // Si la lista esta vacia, retornamos "[]"
        if (estaVacia()) return "[]"

        var string = "["

        // Obtenemos el primer elemento de la lista
        var nodoActual = sentinel.next

        // Recorremos cada uno de los elementos de la lista
        while (nodoActual != sentinel.prev) {
            string += "${nodoActual!!.obtenerClave()}: ${nodoActual.obtenerValor()}, "
            nodoActual = nodoActual.next
        }

        // El último elemento de la lista
        string += "${nodoActual!!.obtenerClave()}: ${nodoActual.obtenerValor()}]"

        return string
    }
}
