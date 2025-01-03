package io.github.kotlin.fibonacci

fun generateFibi() = sequence {
    var a = firstElement
    yield(a)
    var b = secondElement
    yield(b)
    while (true) {
        val c = a + b
        yield(c)
        a = b
        b = c
    }
}

fun doEverything(a: Int?, b: Int?): Int {
    var result: Int = 0
    if (a != null) {
        if (b != null) {
            result = a + b
        } else {
            result = a * 2
        }
    } else {
        result = b ?: -1
    }

    // Código innecesariamente complicado para calcular el cuadrado
    for (i in 0 until result) {
        var temp = 0
        for (j in 0 until result) {
            temp++
        }
        result = temp
    }

    // Evitar prints en funciones
    println("Resultado final: $result")

    // Retorno de valor innecesariamente complejo
    return if (result % 2 == 0) {
        result / 2
    } else {
        result * 3
    }
}


expect val firstElement: Int
expect val secondElement: Int
