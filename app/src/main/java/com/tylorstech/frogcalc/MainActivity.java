package com.tylorstech.frogcalc;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends FragmentActivity
{
    Button      button0,button1,button2,button3,
                button4,button5,button6,button7,button8,button9,buttonPlus,buttonMinus,buttonTimes,
                buttonDivide,buttonEquals,buttonPoint;
    TextView    currentCalcTextView, resultTextView;

    CurrentDisplayMode displayMode = CurrentDisplayMode.Result;
    CurrentAction currentAction = CurrentAction.None;
    String calculationText = "";
    String currentInput = "";
    BigDecimal result = new BigDecimal(0);

    /*
    TODO:
        make back button work to delete previous results i guess maybe kthxbai
        make that imagebutton white
        show current operator in top right
        add support for the period/dot/whatever
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
    }

    private void setControls()
    {
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);

        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        button7=findViewById(R.id.button7);
        button8=findViewById(R.id.button8);
        button9=findViewById(R.id.button9);

        buttonPlus=findViewById(R.id.buttonPlus);
        buttonMinus=findViewById(R.id.buttonMinus);
        buttonTimes=findViewById(R.id.buttonTimes);
        buttonDivide=findViewById(R.id.buttonDivide);
        buttonPoint=findViewById(R.id.buttonPoint);

        buttonEquals=findViewById(R.id.buttonEquals);

        currentCalcTextView = findViewById(R.id.calculationTextView);
        resultTextView = findViewById(R.id.resultTextView);
    }

    public void onNumberButtonClicked(View v)
    {
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
        else if (displayMode == CurrentDisplayMode.Result)
        {
            DecimalFormat d = new DecimalFormat();
            d.setMinimumFractionDigits(2);
            d.setDecimalSeparatorAlwaysShown(true);
            resultTextView.setText(d.format(result));
        }

        currentCalcTextView.setText(calculationText);
    }

    public void onActionButtonClicked(View v)
    {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (v.getId()){
            case R.id.button_ce:
                displayMode = CurrentDisplayMode.Inputting;
                currentInput = "";
                calculationText = "";
                result = new BigDecimal(0);
                updateTextViews();
                break;

            case R.id.button_c:
                currentInput = "";
                updateTextViews();
                break;

            case R.id.button_back:
                currentInput = removeLastChar(currentInput);
                updateTextViews();
                break;
            case R.id.buttonPlus:
                onOperatorActionButtonClicked(CurrentAction.Adding);
                calculationText = RemoveLastOperatorIfEndsInOperator(calculationText) +  " + ";
                updateTextViews();
                break;

            case R.id.buttonMinus:
                onOperatorActionButtonClicked(CurrentAction.Subtracting);
                calculationText = RemoveLastOperatorIfEndsInOperator(calculationText) +  " - ";
                updateTextViews();
                break;

            case R.id.buttonTimes:
                onOperatorActionButtonClicked(CurrentAction.Multiplying);
                calculationText = RemoveLastOperatorIfEndsInOperator(calculationText) +  " * ";
                updateTextViews();
                break;

            case R.id.buttonDivide:
                onOperatorActionButtonClicked(CurrentAction.Dividing);
                calculationText = RemoveLastOperatorIfEndsInOperator(calculationText) + " / ";
                updateTextViews();
                break;

            case R.id.buttonEquals:
                EqualResult(currentAction);
                calculationText = RemoveLastOperatorIfEndsInOperator(calculationText) + " = ";
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
            EqualResult(oldAction);
    }

    public void EqualResult(CurrentAction action){
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
    public String RemoveLastOperatorIfEndsInOperator(String str)
    {
        if (str.endsWith(" "))
            str = removeLastChar(str);
        if (str.endsWith("+") || str.endsWith("-") || str.endsWith("*") || str.endsWith("/") || str.endsWith("=")){
            str = removeLastChar(str);
            str = removeLastChar(str);
            //^thats just laziness
        }
        return str;
    }
}
