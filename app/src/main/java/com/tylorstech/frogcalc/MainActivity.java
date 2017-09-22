package com.tylorstech.frogcalc;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity
{
    Button      button0,button1,button2,button3,
                button4,button5,button6,button7,button8,button9,buttonPlus,buttonMinus,buttonTimes,
                buttonDivide,buttonEquals,buttonPoint;
    TextView    currentCalcTextView, resultTextView;

    CurrentDisplayMode displayMode = CurrentDisplayMode.EqualedResult;
    CurrentAction currentAction = CurrentAction.None;
    String calculationText = "";
    String currentInput = "";
    BigDecimal result = new BigDecimal(0);

    /*
    TODO:
        make back button work to delete previous results i guess maybe kthxbai
        make that imagebutton white
        show current operator in top right
        (done) add support for the period/dot/whatever
        (done) buzz on button press
        add history by adding calculationText + result to a stack every time the equals button is pressed
        make a splash screen
        comment code
        provide further user interaction (Could include sound, flashing, highlighting, color, etc. )
        make sure adapting properly to screen size changes
        Flash window that shows your name and a welcome message as it starts.
        About screen
        Allow switch between decimal, hex, and octal, and binary
        Allow switch between normal, fixed point, and scientific notation
        "Settings" screen: can set decimals places, coloring options for negative values, etc.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControls();
        currentCalcTextView.setText("");
        resultTextView.setText("0");

        getActionBar().setTitle("Standard Mode");
    }

    private void setControls()
    {
        button0=(Button)findViewById(R.id.button0);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);

        button5=(Button)findViewById(R.id.button5);
        button6=(Button)findViewById(R.id.button6);
        button7=(Button)findViewById(R.id.button7);
        button8=(Button)findViewById(R.id.button8);
        button9=(Button)findViewById(R.id.button9);

        buttonPlus=(Button)findViewById(R.id.buttonPlus);
        buttonMinus=(Button)findViewById(R.id.buttonMinus);
        buttonTimes=(Button)findViewById(R.id.buttonTimes);
        buttonDivide=(Button)findViewById(R.id.buttonDivide);
        buttonPoint=(Button)findViewById(R.id.buttonPoint);

        buttonEquals=(Button)findViewById(R.id.buttonEquals);

        currentCalcTextView = (TextView)findViewById(R.id.calculationTextView);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
    }

    public void onNumberButtonClicked(View v)
    {
        if (displayMode == CurrentDisplayMode.EqualedResult)
            doFullClear();

        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        Button sender = (Button)v;
        int num = Integer.parseInt(sender.getText().toString());
        displayMode = CurrentDisplayMode.Inputting;
        currentInput += num;
        calculationText += num;
        updateTextViews();
    }

    public void updateTextViews()
    {
        if (displayMode == CurrentDisplayMode.Inputting)
        {
            resultTextView.setText(currentInput);
        }
        else if (displayMode == CurrentDisplayMode.Result || displayMode == CurrentDisplayMode.EqualedResult)
        {
            DecimalFormat d = new DecimalFormat();
            //d.setMinimumFractionDigits(2);
            //d.setDecimalSeparatorAlwaysShown(true);

            resultTextView.setText(d.format(result));
        }

        currentCalcTextView.setText(calculationText);
    }

    public void onActionButtonClicked(View v)
    {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (v.getId()){
            case R.id.button_ce:
                doFullClear();
                displayMode = CurrentDisplayMode.Inputting;
                updateTextViews();
                break;

            case R.id.button_c:
                currentInput = "";
                updateTextViews();
                break;

            case R.id.button_back:
                currentInput = removeLastChar(currentInput);
                calculationText = backspaceString(calculationText);
                updateTextViews();
                break;
            case R.id.buttonPlus:
                onOperatorActionButtonClicked(CurrentAction.Adding);
                calculationText = removeLastOperatorIfEndsInOperator(calculationText) +  " + ";
                updateTextViews();
                break;

            case R.id.buttonMinus:
                onOperatorActionButtonClicked(CurrentAction.Subtracting);
                calculationText = removeLastOperatorIfEndsInOperator(calculationText) +  " - ";
                updateTextViews();
                break;

            case R.id.buttonTimes:
                onOperatorActionButtonClicked(CurrentAction.Multiplying);
                calculationText = removeLastOperatorIfEndsInOperator(calculationText) +  " * ";
                updateTextViews();
                break;

            case R.id.buttonDivide:
                onOperatorActionButtonClicked(CurrentAction.Dividing);
                calculationText = removeLastOperatorIfEndsInOperator(calculationText) + " / ";
                updateTextViews();
                break;

            case R.id.buttonEquals:
                equalResult(currentAction);
                calculationText = removeLastOperatorIfEndsInOperator(calculationText) + " = ";
                displayMode = CurrentDisplayMode.EqualedResult;
                updateTextViews();
                break;

            case R.id.buttonPoint:
                if (!currentInput.contains("."))
                {
                    currentInput += ".";
                    calculationText += ".";
                }
                updateTextViews();
                break;
        }
    }

    private void onOperatorActionButtonClicked(CurrentAction action)
    {
        CurrentAction oldAction = currentAction;
        currentAction = action;
        if (currentInput.length() > 0)
            equalResult(oldAction);
    }

    private void doFullClear()
    {
        currentInput = "";
        calculationText = "";
        result = new BigDecimal(0);
        currentAction = CurrentAction.None;
        displayMode = CurrentDisplayMode.Inputting;
        updateTextViews();
    }

    public void equalResult(CurrentAction action){
        if (currentInput.length() > 0)
        {
            switch (action){
                case Adding:
                    result = result.add(new BigDecimal(currentInput));
                    break;
                case None:
                    result = new BigDecimal(currentInput);
                    break;
                case Subtracting:
                    result = result.subtract(new BigDecimal(currentInput));
                    break;
                case Multiplying:
                    result = result.multiply(new BigDecimal(currentInput));
                    break;
                case Dividing:
                    result = result.divide(new BigDecimal(currentInput));
                    break;
            }
            currentInput = "";
            displayMode = CurrentDisplayMode.Result;
            updateTextViews();
        }
    }

    public String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public boolean isOperator(char s){
        return s == '+' || s == '-' || s == '*' || s == '/' || s == '=';
    }

    // #methodnamingat2:30am
    public String removeLastOperatorIfEndsInOperator(String str)
    {
        if (str.endsWith(" "))
            str = removeLastChar(str);
        if (str.endsWith("+") || str.endsWith("-") || str.endsWith("*") || str.endsWith("/") || str.endsWith("="))
        {
            str = removeLastChar(str);
            str = removeLastChar(str);
            //^thats just laziness
        }
        return str;
    }

    public String backspaceString(String s)
    {
        StringBuilder b = new StringBuilder(s);
        for (int i = s.length(); i >= 0; i--)
        {
            if (!isOperator(s.charAt(i)) && s.charAt(i) != ' ')
            {
                b.deleteCharAt(i);
                return b.toString();
            }
        }
        return b.toString();
    }
}
