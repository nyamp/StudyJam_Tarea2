package com.gdgandroidbolivia.mycalculator;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdgandroidbolivia.mycalculator.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class MainActivity extends AppCompatActivity {

    // como botones numericos
    private int[] numericButtons = {R.id.btOne, R.id.btTwo, R.id.btThree,
            R.id.btFour, R.id.btFive, R.id.btSix, R.id.btSeven, R.id.btEight,
            R.id.btNine, R.id.btZero};
    // operadores
    private int[] operatorButtons = {R.id.btnAdd, R.id.btnSubstract,
            R.id.btnDivide, R.id.btnMultiply};
    // Resultado o Input - output
    private TextView txtInput;
    // diferenciar si es dato numerico
    private boolean lastNumeric;
    // error
    private boolean stateError;
    private boolean lastDot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtInput = (TextView) findViewById(R.id.txtInput);
        setNumericOnClickListener();
        setOperatorOnClickListener();
    }

    private void setNumericOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (stateError) {
                    txtInput.setText(button.getText());
                    stateError = false;
                } else {
                    txtInput.append(button.getText());
                }
                lastNumeric = true;
            }
        };
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }
    private void setOperatorOnClickListener() {
        //OnClickListener p operadores
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError) {
                    Button button = (Button) v;
                    txtInput.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        //punto decimal - dot
        findViewById(R.id.btDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    txtInput.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });
        // Clear
        findViewById(R.id.btClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput.setText("");
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });
        // Equal
        findViewById(R.id.btEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }

    private void onEqual() {
        if (lastNumeric && !stateError) {

            String txt = txtInput.getText().toString();

            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // calcular
                double result = expression.evaluate();
                txtInput.setText(Double.toString(result));
                lastDot = true;
            } catch (ArithmeticException ex) {
                // error-mensaje
                txtInput.setText("Error!");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}
