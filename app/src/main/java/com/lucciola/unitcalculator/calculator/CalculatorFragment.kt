package com.lucciola.unitcalculator.calculator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lucciola.unitcalculator.R
import kotlinx.android.synthetic.main.calculator_fragment.view.*

class CalculatorFragment : CalculatorContract.View, Fragment() {

    override lateinit var presenter: CalculatorContract.Presenter
    private var mFormulaTextView: TextView? = null
    private var mResultTextView: TextView? = null

    companion object {
        fun newInstance(): CalculatorFragment = CalculatorFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View? = inflater.inflate(R.layout.calculator_fragment, container, false)

        this.mFormulaTextView = root?.formulaTextView
        this.mResultTextView = root?.resultTextView

        return root ?: inflater.let { super.onCreateView(it, container, savedInstanceState) }
    }

    override fun showResult(result: String) {
        this.mResultTextView?.text = result
    }

    override fun showFormula(formula: String) {
        this.mFormulaTextView?.text = formula
    }

}