package com.lucciola.unitcalculator.calculator

import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.lucciola.unitcalculator.R
import com.lucciola.unitcalculator.util.ActivityUtils

class CalculatorActivity : AppCompatActivity() {

    private lateinit var mCalculatorPresenter: CalculatorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.calculator_activity)

        val calculatorFragment: CalculatorFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as CalculatorFragment?
                ?: let {
                    val instance = CalculatorFragment.newInstance()
                    ActivityUtils.addFragmentToActivity(supportFragmentManager, instance, R.id.contentFrame)
                    instance
                }

        this.mCalculatorPresenter = CalculatorPresenter(calculatorFragment)
    }

    fun onButtonClick(view: View) {
        when (view.id) {
            R.id.digit_0, R.id.digit_1, R.id.digit_2, R.id.digit_3, R.id.digit_4, R.id.digit_5, R.id.digit_6, R.id.digit_7, R.id.digit_8, R.id.digit_9
            -> this.mCalculatorPresenter.onInputNumber((view as MaterialButton).text.toString())
            R.id.plus, R.id.minus, R.id.time, R.id.divide
            -> this.mCalculatorPresenter.onInputOperator((view as MaterialButton).text.toString())
            R.id.equal -> this.mCalculatorPresenter.onEvaluate()
        }
    }

}