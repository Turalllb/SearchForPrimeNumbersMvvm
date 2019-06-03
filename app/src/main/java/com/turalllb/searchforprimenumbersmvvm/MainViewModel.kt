package com.turalllb.searchforprimenumbersmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {
    internal val primeNumbers = MutableLiveData<MutableList<Long>>()
    internal var sumPrimeNumbers = MutableLiveData<Long>()
    internal var timeElapsed = MutableLiveData<Double>()
    internal val calculationBtEnable = MutableLiveData<Boolean>()
    internal val isLoading = MutableLiveData<Boolean>()


    fun clickCalculate(n: Long) {
        calculationBtEnable.value = false
        isLoading.postValue(true)
        Thread{
            val startTime = System.currentTimeMillis()
            findPrimeNumbers(n)
            val stopTime = System.currentTimeMillis()
            timeElapsed.postValue((stopTime - startTime).toDouble() / 1000)
            isLoading.postValue(false)
            calculationBtEnable.postValue(true)
        }.start()
    }


    private fun findPrimeNumbers(n: Long) {
        val listTemp: MutableList<Long> = arrayListOf()
        var sumPrimeTemp = 0L
        if (n >= 2) {
            listTemp.add(2)
            sumPrimeTemp += 2
        }
        for (i: Long in 3..n step 2) {
            if (isPrime(i)) {
                listTemp.add(i)
                sumPrimeTemp += i
            }
        }
        sumPrimeNumbers.postValue(sumPrimeTemp)
        primeNumbers.value?.clear()
        primeNumbers.postValue(listTemp)

    }


    private fun isPrime(l: Long): Boolean {
        val sqrtL: Long = kotlin.math.sqrt(l.toDouble()).toLong()
        for (i: Long in 3..sqrtL step 2) {
            if (l % i == 0L) return false
        }
        return true
    }

}
