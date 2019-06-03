package com.turalllb.searchforprimenumbersmvvm

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    private lateinit var adapterPrimeNumbers: AdapterPrimeNumbers
    private lateinit var editText: EditText
    private lateinit var calculateBt: Button
    private lateinit var sumTv: TextView
    private lateinit var calculationTimeTv: TextView
    private lateinit var progressBar: ProgressBar
    private var primeNumbers: MutableList<Long> = arrayListOf()
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //region findViewById
        calculateBt = findViewById(R.id.calculate_bt)
        val textLayout = findViewById<TextInputLayout>(R.id.text_input_layout)
        editText = textLayout.findViewById(R.id.editText)
        sumTv = findViewById(R.id.sum_tv)
        calculationTimeTv = findViewById(R.id.calculation_time_tv)
        val rv: RecyclerView = findViewById(R.id.rv)
        progressBar = findViewById(R.id.progressBar)
        //endregion

        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        model.isLoading.observe(this, Observer<Boolean> {
            progressBar.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
        })

        model.primeNumbers.observe(this, Observer<MutableList<Long>> {
            it?.let {
                primeNumbers.clear()
                primeNumbers.addAll(it)
                adapterPrimeNumbers.notifyDataSetChanged()
            }
        })

        rv.layoutManager = LinearLayoutManager(this)
        adapterPrimeNumbers = AdapterPrimeNumbers(this, primeNumbers)
        rv.adapter = adapterPrimeNumbers

        model.sumPrimeNumbers.observe(this, Observer {
            sumTv.text = getString(R.string.sum, it)
        })

        model.timeElapsed.observe(this, Observer {
            calculationTimeTv.text = getString(R.string.calculation_time, it)
        })

        model.calculationBtEnable.observe(this, Observer<Boolean> {
            it?.let {
                calculateBt.isEnabled = it
            }
        })

    }

    fun onClickCalculate(v: View?) {
        hideKeyboard()
        val input = editText.text.toString()
        if (input != "") {
            model.clickCalculate(input.toLong())
        }
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = getCurrentFocus()
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}
