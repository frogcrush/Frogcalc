package com.tylorstech.frogcalc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class Calculator extends Fragment
{
    //region Controls
    Button button0,button1,button2,button3,
            button4,button5,button6,button7,button8,button9,buttonPlus,buttonMinus,buttonTimes,
            buttonDivide,buttonEquals,buttonPoint,
            buttonClear, buttonClearAll;
    ImageButton buttonBackspace;
    TextView currentCalcTextView, resultTextView;
    //endregion

    //region Variables
    CurrentDisplayMode displayMode = CurrentDisplayMode.EqualedResult;
    CurrentAction currentAction = CurrentAction.None;
    String calculationText = "";
    String currentInput = "";
    BigDecimal result = new BigDecimal(0);
    //endregion

    private NumberButtonClickListener numClickListener = new NumberButtonClickListener();
    private ActionButtonClickListener actionClickListener = new ActionButtonClickListener();
    private OnFragmentInteractionListener mListener;

    //region Click Listeners
    public class NumberButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            onNumberButtonClicked(view);
        }
    }

    public class ActionButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            onActionButtonClicked(view);
        }
    }
    //endregion

    private void setControls()
    {
        button0 = (Button)getView().findViewById(R.id.button0);
        button0.setOnClickListener(numClickListener);
        button1 = (Button)getView().findViewById(R.id.button1);
        button1.setOnClickListener(numClickListener);
        button2 = (Button)getView().findViewById(R.id.button2);
        button2.setOnClickListener(numClickListener);
        button3 = (Button)getView().findViewById(R.id.button3);
        button3.setOnClickListener(numClickListener);
        button4 = (Button)getView().findViewById(R.id.button4);
        button4.setOnClickListener(numClickListener);

        button5 = (Button)getView().findViewById(R.id.button5);
        button5.setOnClickListener(numClickListener);
        button6 = (Button)getView().findViewById(R.id.button6);
        button6.setOnClickListener(numClickListener);
        button7 = (Button)getView().findViewById(R.id.button7);
        button7.setOnClickListener(numClickListener);
        button8 = (Button)getView().findViewById(R.id.button8);
        button8.setOnClickListener(numClickListener);
        button9 = (Button)getView().findViewById(R.id.button9);
        button9.setOnClickListener(numClickListener);

        buttonPlus = (Button)getView().findViewById(R.id.buttonPlus);
        buttonPlus.setOnClickListener(actionClickListener);
        buttonMinus = (Button)getView().findViewById(R.id.buttonMinus);
        buttonMinus.setOnClickListener(actionClickListener);
        buttonTimes = (Button)getView().findViewById(R.id.buttonTimes);
        buttonTimes.setOnClickListener(actionClickListener);
        buttonDivide = (Button)getView().findViewById(R.id.buttonDivide);
        buttonDivide.setOnClickListener(actionClickListener);
        buttonPoint = (Button)getView().findViewById(R.id.buttonPoint);
        buttonPoint.setOnClickListener(actionClickListener);

        buttonClear = getView().findViewById(R.id.button_c);
        buttonClear.setOnClickListener(actionClickListener);
        buttonClearAll = getView().findViewById(R.id.button_ce);
        buttonClearAll.setOnClickListener(actionClickListener);

        buttonBackspace = getView().findViewById(R.id.button_back);
        buttonBackspace.setOnClickListener(actionClickListener);

        buttonEquals=(Button)getView().findViewById(R.id.buttonEquals);
        buttonEquals.setOnClickListener(actionClickListener);

        currentCalcTextView = (TextView)getView().findViewById(R.id.calculationTextView);
        resultTextView = (TextView) getView().findViewById(R.id.resultTextView);
    }

    //region Fragment stuff


    public Calculator()
    {
        // Required empty public constructor
    }

    public static Calculator newInstance()
    {
        Calculator fragment = new Calculator();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        setControls();
        currentCalcTextView.setText("");
        resultTextView.setText("0");
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
    //endregion

    //region Button Handlers
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
                try
                {
                    if (displayMode == CurrentDisplayMode.EqualedResult)
                    {
                        doFullClear();
                        displayMode = CurrentDisplayMode.Inputting;
                        updateTextViews();
                    } else if (displayMode == CurrentDisplayMode.Result)
                    {
                        currentInput = "";
                        updateTextViews();
                    } else
                    {
                        currentInput = "";
                        calculationText = deleteToLastOperator(calculationText);
                        updateTextViews();
                    }
                } catch (Exception ex)
                {

                }
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
    //endregion

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
            boolean wasValid = true;

            switch (action){
                case Adding:
                    result = result.add(new BigDecimal(currentInput));
                    break;
                case None:
                    result = new BigDecimal(currentInput.equals(".") ? "0.0" : currentInput);
                    break;
                case Subtracting:
                    result = result.subtract(new BigDecimal(currentInput));
                    break;
                case Multiplying:
                    result = result.multiply(new BigDecimal(currentInput));
                    break;
                case Dividing:
                    BigDecimal d = new BigDecimal(currentInput);
                    if (d.compareTo(BigDecimal.ZERO) == 0)
                    {
                        wasValid = false;
                    }
                    else
                    {
                        result = result.divide(d);
                    }
                    break;
            }
            if (!wasValid)
            {
                doFullClear();
                displayMode = CurrentDisplayMode.EqualedResult;
                updateTextViews();
                resultTextView.setText("INVALID");
                currentCalcTextView.setText("INVALID");
            }
            else
            {
                currentInput = "";
                displayMode = CurrentDisplayMode.Result;
                updateTextViews();
            }
        }
    }

    //region String operations

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
        for (int i = s.length() - 1; i >= 0; i--)
        {
            if (!isOperator(s.charAt(i)) && s.charAt(i) != ' ')
            {
                b.deleteCharAt(i);
                return b.toString();
            }
        }
        return b.toString();
    }

    public String deleteToLastOperator(String s)
    {
        int i = s.length() - 1;
        StringBuilder builder = new StringBuilder(s);

        while (s.charAt(i) == ' ' || isOperator(s.charAt(i)))
        {
            i--;
        }

        while (i >= 0)
        {
            if (s.charAt(i) == ' ' || isOperator(s.charAt(i)))
            {
                return builder.toString();
            }
            builder.deleteCharAt(i);
            i--;
        }

        return builder.toString();

    }
    //endregion
}
