package com.lucciola.unitcalculator.calculator

import FormulaParser.Parser
import Exception.RuntimeErrorException
import Exception.SyntaxErrorException

class CalculatorPresenter(calculatorView: CalculatorContract.View) : CalculatorContract.Presenter {

    private val mCalculatorView: CalculatorContract.View = calculatorView
    private var mFormula: String = ""
    private val mParser = Parser("")

    init {
        mCalculatorView.presenter = this
    }

    override fun start() {
    }

    override fun onEvaluate() {
        this.mParser.setProgram(mFormula)
        val ans: String = try {
            mParser.compile().evaluate()
        } catch(e: RuntimeErrorException) {
            "runtime error : " + e.message
        } catch(e: SyntaxErrorException) {
            "syntax error : " + e.message
        }
        mCalculatorView.showResult(ans)
    }

    override fun onInputNumber(number: String) {
        this.mFormula += number
        this.mCalculatorView.showFormula(this.mFormula)
    }

    override fun onInputOperator(operator: String) {
        this.mFormula += " $operator "
        this.mCalculatorView.showFormula(this.mFormula)
    }

    override fun onInputBrace(brace: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInputUnit(unit: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInputPrefix(prefix: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}