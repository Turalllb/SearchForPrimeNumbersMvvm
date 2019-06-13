package com.turalllb.searchforprimenumbersmvvm

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    private lateinit var adapterPrimeNumbers: AdapterPrimeNumbers
    private lateinit var editText: EditText
    private lateinit var calculateBt: Button
    private lateinit var sumTv: TextView
    private lateinit var calculationTimeTv: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var model: MainViewModel
    private lateinit var spinner: Spinner

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
        spinner = findViewById(R.id.spinner)
        //endregion

        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        model.isLoading.observe(this, Observer<Boolean> {
            progressBar.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
        })

        model.primeNumbers.observe(this, Observer<MutableList<Int>> {
            adapterPrimeNumbers.primeNumbers = it
            adapterPrimeNumbers.notifyDataSetChanged()
        })

        model.sumPrimeNumbers.observe(this, Observer {
            sumTv.text = getString(R.string.sum, it)
        })

        model.timeElapsed.observe(this, Observer {
            calculationTimeTv.text = getString(R.string.calculation_time, it)
        })

        model.calculationBtEnable.observe(this, Observer<Boolean> {
            calculateBt.isEnabled = it
        })


        rv.layoutManager = LinearLayoutManager(this)
        adapterPrimeNumbers = AdapterPrimeNumbers(this)
        rv.adapter = adapterPrimeNumbers

        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.algorithms))
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this
    }



    fun onClickCalculate(v: View?) {
        hideKeyboard()
        val input = editText.text.toString()
        if (input != "") {
            model.clickCalculate(input.toInt())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       model.algorithmsPosition.value = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
         //nothing
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
