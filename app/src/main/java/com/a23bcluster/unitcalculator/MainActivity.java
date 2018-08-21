package com.a23bcluster.unitcalculator;

/*
 * Copylight (C) 2016 clockvoid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//import classes about android
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

//import calsses using on FormulaParser
import Exception.RuntimeErrorException;
import Exception.SyntaxErrorException;
import FormulaParser.Expression;
import FormulaParser.Parser;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnLongClickListener {

    private static final int ON_NUMBER = 0;
    private static final int ON_PREFIX = 1;
    private static final int ON_UNIT = 2;
    private static final int ON_SEPARATE = 3;

    private EditText formula;
    private TextView result;
    private Parser parser;
    private View deleteButton;

    private List<String> program;
    private List<String> monitorProgram;

    private int inputMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        program = new ArrayList<String>();
        monitorProgram = new ArrayList<String>();

        formula = (EditText) findViewById(R.id.formula);
        result = (TextView) findViewById(R.id.result);
        deleteButton = findViewById(R.id.delete);

        parser = new Parser("");

        deleteButton.setOnLongClickListener(this);

        inputMode = ON_NUMBER;
    }

    private void onEqual() {
        parser.setProgram(createProgramString(program));
        String ans;
        try {
            ans = parser.compile().evaluate();

        } catch(RuntimeErrorException e) {
            ans = "runtime error : " + e.getMessage();
        } catch(SyntaxErrorException e) {
            ans = "syntax error : " + e.getMessage();
        }
        result.setText("= " + ans.replaceAll("void\\^0", "").replaceAll("'", "･"));
    }

    private String createProgramString(List<String> arg0) {
        String ret = "";
        for (int i = 0; i < arg0.size(); i ++) {
            ret += arg0.get(i);
        }
        return ret;
    }

    private void inputOperator(View view) {
        if(inputMode == ON_NUMBER) {
            switch(view.getId()) {
                case R.id.operator_divide: {
                    program.add(" / ");
                    break;
                }
                case R.id.operator_time: {
                    program.add(" * ");
                    break;
                }
                case R.id.operator_minus: {
                    program.add(" - ");
                    break;
                }
                case R.id.operator_add: {
                    program.add(" + ");
                    break;
                }
                case R.id.operator_power: {
                    program.add(" ^ ");
                }
            }
            monitorProgram.add(((Button) view).getText().toString());
        } else if(inputMode == ON_SEPARATE) {
            switch(view.getId()) {
                case R.id.operator_power: {
                    program.add("^");
                    monitorProgram.add(((Button) view).getText().toString());
                    break;
                }
            }
        }
    }

    private void fixModeOnDelete() {
        switch(inputMode) {
            case ON_PREFIX: {
                if (!program.isEmpty() && program.get(program.size() - 1).equals("[")) {
                    inputMode = ON_NUMBER;
                } else {
                    inputMode = ON_SEPARATE;
                }
                break;
            }
            case ON_UNIT:
                inputMode = ON_PREFIX;
                break;
            case ON_SEPARATE:
                if(program.size() > 2 && program.get(program.size() - 2).indexOf("_") != -1) {
                    inputMode = ON_UNIT;
                } else if(!program.isEmpty() && program.get(program.size() - 1).matches("(\\d)|(\\s?^\\s?)|(\\s\\))|(\\(\\s)")){
                    inputMode = ON_SEPARATE;
                } else {
                    inputMode = ON_PREFIX;
                }
                break;
        }
    }

    /*
     * このメソッドでボタンのクリックイベントを処理する.
     * クリックイベントごとに分けてそれぞれで適切な文字列を変数programに追加する.
     * また,メンバ変数inputModeによってボタンの状態を変化させるようにする.
     */
    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.prefix :{
                if(inputMode == ON_PREFIX) {
                    program.add(((Button) view).getText() + "_");
                    monitorProgram.add(((Button) view).getText().toString());
                    inputMode = ON_UNIT;
                }
                break;
            }
            case R.id.unit :{
                if(inputMode == ON_PREFIX || inputMode == ON_UNIT) {
                    program.add(((Button) view).getText().toString());
                    monitorProgram.add(((Button) view).getText().toString());
                    inputMode = ON_SEPARATE;
                }
                break;
            }
            case R.id.separate :{
                if(inputMode == ON_SEPARATE) {
                    program.add("'");
                    monitorProgram.add(((Button) view).getText().toString());
                    inputMode = ON_PREFIX;
                }
                break;
            }
            case R.id.operator_divide :case R.id.operator_time :case R.id.operator_minus :case R.id.operator_add :case R.id.operator_power :{
                inputOperator(view);
                break;
            }
            case R.id.function_sqrt :{
                if(inputMode == ON_NUMBER) {
                    program.add("sqrt( ");
                    monitorProgram.add(((Button) view).getText() + "(");
                }
                break;
            }
            case R.id.equal :{
                onEqual();
                break;
            }
            case R.id.delete :{
                if(!program.isEmpty()) {
                    fixModeOnDelete();
                    program.remove(program.size() - 1);
                    monitorProgram.remove(monitorProgram.size() - 1);
                }
                break;
            }
            case R.id.factor :{
                if (((Button) view).getText().toString().equals("(")) {
                    program.add("( ");
                } else {
                    program.add(" )");
                }
                monitorProgram.add(((Button) view).getText().toString());
                break;
            }
            default :{
                if(((Button) view).getText().toString().equals("[")) {
                    inputMode = ON_PREFIX;
                } else if(((Button) view).getText().toString().equals("]")) {
                    inputMode = ON_NUMBER;
                }
                program.add(((Button) view).getText().toString());
                monitorProgram.add(((Button) view).getText().toString());
                break;
            }
        }
        formula.setText(createProgramString(monitorProgram));
    }

    @Override
    public boolean onLongClick(View view) {
        if(view.getId() == R.id.delete) {
            program.clear();
            monitorProgram.clear();
            formula.setText(createProgramString(program));
            return true;
        }
        return false;
    }

    //TODO: make small this java file and applying desing patterns.
    //TODO: clear some bugs and remake input mode system.
}
