package com.turalllb.searchforprimenumbersmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.max


class MainViewModel : ViewModel() {
    internal val primeNumbers = MutableLiveData<MutableList<Int>>()
    internal var sumPrimeNumbers = MutableLiveData<Long>()
    internal var timeElapsed = MutableLiveData<Double>()
    internal val calculationBtEnable = MutableLiveData<Boolean>()
    internal val isLoading = MutableLiveData<Boolean>()
    internal val algorithmsPosition = MutableLiveData<Int>()


    fun clickCalculate(n: Int) {
        calculationBtEnable.value = false
        isLoading.postValue(true)
        Thread {
            val startTime = System.currentTimeMillis()
            when (algorithmsPosition.value) {
                0 -> findPrimeNumbers(n)
                1 -> findPrimeNumbersAlg2(n)
                2 -> findPrimeBitSet(n)
                3 -> findPrimeSegmented(n)
            }
            val stopTime = System.currentTimeMillis()
            timeElapsed.postValue((stopTime - startTime).toDouble() / 1000)
            isLoading.postValue(false)
            calculationBtEnable.postValue(true)
        }.start()
    }


    /*Самый оптимальный по памяти алгоритм. Оптимизации:
    1) проверка только нечетных чисел, т.к. 2 единственное четное простое число, определяем 2 сразу как простое. (Предложил сам Эратосфен)
    2) Поиск делителей числа только в диапазоне от [3, sqrt(n)] */
    private fun findPrimeNumbers(n: Int) {
        primeNumbers.postValue(mutableListOf())
        val listTemp: MutableList<Int> = arrayListOf()
        var sumPrimeTemp = 0L

        if (n >= 2) {
            listTemp.add(2)
            sumPrimeTemp += 2
        }
        for (i: Int in 3..n step 2) {
            if (isPrime(i)) {
                listTemp.add(i)
                sumPrimeTemp += i
            }
        }


        primeNumbers.postValue(listTemp)
    }


    private fun isPrime(l: Int): Boolean {
        val sqrtL: Long = kotlin.math.sqrt(l.toDouble()).toLong()
        for (i: Long in 3..sqrtL step 2) {
            if (l % i == 0L) return false
        }
        return true
    }


    //Стандартное решето Эратосфена
    private fun findPrimeNumbersAlg2(n: Int) {
        primeNumbers.postValue(mutableListOf())
        val listTemp: MutableList<Int> = arrayListOf()

        val temp = Array(n + 1) { true }

        var i = 3
        while (i * i <= n) {
            if (temp[i]) {
                for (k: Int in i * i..n step i) {
                    temp[k] = false
                }
            }
            i += 2
        }

        listTemp.add(2)
        for (j: Int in 3..n step 2) { //Только по нечетным
            if (temp[j]) listTemp.add(j)
        }


        primeNumbers.postValue(listTemp)
        sumPrimeNumbers.postValue(getSumArray(listTemp))
    }


    // Стандартное решето на битовых флагах
    private fun findPrimeBitSet(n: Int) {
        primeNumbers.postValue(mutableListOf())
        val listTemp: MutableList<Int> = arrayListOf()

        val temp = BitSet(n)

        var i = 3
        while (i * i <= n) {
            if (!temp[i]) {
                for (k: Int in (i * i)..n step i) {
                    temp[k] = true
                }
            }
            i += 2
        }

        listTemp.add(2)
        for (j: Int in 3..n step 2) {
            if (!temp[j]) listTemp.add(j)
        }


        primeNumbers.postValue(listTemp)
        sumPrimeNumbers.postValue(getSumArray(listTemp))
    }


    //Сегментированное Решето Эратосфена. на битовых флагах.
    private fun findPrimeSegmented(n: Int) {
        primeNumbers.postValue(mutableListOf())
        val listTemp: MutableList<Int> = arrayListOf()
        val sortMax = kotlin.math.sqrt(n.toDouble()).toInt()
        val cache = 30000  //оптимальный размер одного блока
        val countBlock: Int = n / cache //кол-во блоков
        val primes = findPrimesUntilSqrtN(sortMax) //сперва получаем все простые числа до sqrtMax
        val cnt = primes.size
        val bool = BitSet(cache)

        var k = 0
        while (k <= countBlock) {
            bool.clear()
            val start = k * cache

            var i = 0
            var startIdx: Int
            while (i < cnt) {
                startIdx = (start + primes[i] - 1) / primes[i]
                var j = max(startIdx, 2) * primes[i] - start
                while (j < cache) {
                    bool[j] = true
                    j += primes[i]
                }
                i++
            }
            if (k == 0) { //В самом первом блоке исключаю
                bool[0] = true
                bool[1] = true
            }
            var e = 0
            while (e < cache && (start + e) < n) {
                if (!bool[e]) listTemp.add(e + start)
                e++
            }
            k++
        }


        primeNumbers.postValue(listTemp)
        sumPrimeNumbers.postValue(getSumArray(listTemp))
    }


    private fun findPrimesUntilSqrtN(sqrtN: Int): MutableList<Int> {
        val temp = BitSet(sqrtN)
        var i = 3
        while (i * i <= sqrtN) {
            if (!temp[i]) {
                for (k: Int in (i * i)..sqrtN step i) {
                    temp[k] = true
                }
            }
            i += 2
        }
        val primes = mutableListOf<Int>()
        primes.add(2)
        for (j: Int in 3..sqrtN step 2) {
            if (!temp[j]) primes.add(j)
        }
        return primes
    }


    private fun getSumArray(list: List<Int>): Long {
        var sum: Long = 0
        list.forEach { sum += it }
        return sum
    }


}
