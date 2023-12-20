import java.util.*

//Lab6 ГОСТ Р 34.10-94.

fun main() {
    var p = 0   //p=31
    while (p == 0)
        p = inputP()
    var q = 0
    while (q == 0)
        q = inputQ(p)
    var a = 0
    while (a == 0)
        a = inputA(p, q)
    var flag = true
    val strk = "*АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ1234" //p=37
    var m = 0
    var x = 0
    var y = 0
    println("Ввод текста:")
    val vvod: String = readln()
    while (flag) {
        while (x == 0)
            x = inputX(q)
        y = gorner(a, x, p)!!
        val mas = IntArray(1000)
        for (i in 0..vvod.length - 1) {
            for (j in 0..strk.length - 1) {
                if (vvod[i] == strk[j]) {
                    mas[i] = j
                }
            }
        }
        for (i in vvod.indices) {
            val searchhash = gorner(mas[i], y!!, p)
            m = (m xor searchhash!!).mod(p)
        }
        m = m.mod(q)
        if (m < p - 1)// учтено условие 0<m<(p-1)
            flag = false
        else println("Пожалуйста, измените значение х")
        if (m == 0)
            m = 1
    }
    val random = Random()
    var r = 0
    var k = 0
    while (r == 0) {
        k = random.nextInt(1, q - 1)
        r = gorner(a, k, p)!!.mod(q)
    }
    val s = (x * r + k * m).mod(q)
    println("Распространяются открытые параметры: p=$p,q=$q,y=$y,a=$a,r=$r,s=$s")
    println("Числа х=$x и k=$k являются секретными\nНачинается проверка подписи на стороне Б:")
    val v = gorner(m, q - 2, q)
    val z1 = (s * v!!).mod(q)
    val z2 = ((q - r) * v).mod(q)
    val first = (Math.pow(a.toDouble(), z1.toDouble())).toInt()
    val second = (Math.pow(y!!.toDouble(), z2.toDouble())).toInt()
    val u = ((first * second).mod(p)).mod(q)
    if (u == r) {
        println("Подпись считается верной: u=$u=r=$r")
    } else println("Подделка")
}

fun inputP(): Int {
    var P: Int = 0
    println("Введите простое число p:")
    try {
        P = readln().toInt()
        var flag = true
        for (i in 2..(P / 2)) {
            if (P % i == 0) {
                flag = false
                break
            }
        }
        return if (P < 1 || !flag) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else P
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        return 0
    }
}  //ВВОДИМ ПРОСТОЕ ЧИСЛО p

fun inputQ(p: Int): Int {
    var q: Int = 0
    println("Введите простое число q, которое >2 и является сомножителем для числа ${p - 1} :")
    try {
        q = readln().toInt()
        var flag = true
        for (i in 2..(q / 2)) {
            if (q % i == 0) {
                flag = false
                break
            }
        }
        return if (q < 3 || !flag) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else q
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        return 0
    }
}  //ВВОДИМ ПРОСТОЕ ЧИСЛО q

fun inputA(p: Int, q: Int): Int {
    var a: Int = 0
    println("Введите число а, которое удовлетворяет условиям: 0<a<${p - 1} и (a^q)mod(p)=1")
    try {
        a = readln().toInt()
        var flag = false
        if (gorner(a, q, p) == 1)
            flag = true
        return if (a > p - 1 || a < 0 || !flag) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else a
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        return 0
    }
}  //ВВОДИМ ЧИСЛО a

fun inputX(q: Int): Int {
    var x: Int = 0
    println("Введите секретный ключ x, который меньше $q ")
    try {
        x = readln().toInt()
        return if (x >= q) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else x
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        return 0
    }
}  //ВВОДИМ ЧИСЛО x

fun gorner(a: Int, x: Int, m: Int): Int? {
    var r = 1
    var k = 0
    var y: Int? = null
    if (m == 0) return null
    if (a == 0) return 0
    if (a == 1 || x == 0) return 1
    while (r <= x && k < 32) {
        r = (1 shl k)
        k++
    }
    k--
    if (k == 0 || k > 32) return null
    r = a
    y = if (x % 2 == 1) a
    else 1
    for (i in 1..k - 1) {
        r = (r * r).mod(m)
        if (((x shr i) and 1) == 1) {
            if (y != null) {
                y = (y.toInt() * r).mod(m)
            }
        }
    }
    return y
} //a^x mod m