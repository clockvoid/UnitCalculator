package com.lucciola.unitcalculator.calculator

import com.lucciola.unitcalculator.BasePresenter
import com.lucciola.unitcalculator.BaseView

interface CalculatorContract {

    interface View : BaseView<Presenter> {
        fun showResult(result: String)
        fun showFormula(formula: String)
    }

    interface Presenter : BasePresenter {
        fun onEvaluate()
        fun onInputNumber(number: String)
        fun onInputOperator(operator: String)
        fun onInputBrace(brace: String)
        fun onInputUnit(unit: String)
        fun onInputPrefix(prefix: String)
    }

}
