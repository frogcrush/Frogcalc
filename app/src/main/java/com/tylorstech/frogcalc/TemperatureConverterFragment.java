package com.tylorstech.frogcalc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TemperatureConverterFragment extends Fragment
{
    Spinner spinner_from, spinner_to;
    EditText editText;
    TextView resultsTextView;
    Button submitButton;

    private OnFragmentInteractionListener mListener;

    public TemperatureConverterFragment()
    {
        // Required empty public constructor
    }

    public static TemperatureConverterFragment newInstance()
    {
        TemperatureConverterFragment fragment = new TemperatureConverterFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        spinner_from = getView().findViewById(R.id.spinner_from);
        spinner_to = getView().findViewById(R.id.spinner_to);
        editText = getView().findViewById(R.id.editText);
        resultsTextView = getView().findViewById(R.id.resultsTextView);
        submitButton = getView().findViewById(R.id.temp_convert_submit_button);

        resultsTextView.setVisibility(View.INVISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onGetResultButtonClicked();
            }
        });

        createSpinnerItems();
    }

    public void createSpinnerItems(){
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("Fahrenheit");
        strings.add("Celsius");
        spinner_from.setAdapter(new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, strings));
        spinner_to.setAdapter(new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, strings));
        spinner_to.setSelection(1);
    }

    public void onGetResultButtonClicked(){
        String txt = editText.getText().toString();
        if (txt.isEmpty()){
            toast("You must enter a number to convert.");
            resultsTextView.setVisibility(View.INVISIBLE);
        }

        double i = Double.parseDouble(txt);
        String from = spinner_from.getSelectedItem().toString();
        String to = spinner_to.getSelectedItem().toString();
        if (from.equals(to)){
            toast("You can't convert between the same units.");
            resultsTextView.setVisibility(View.INVISIBLE);
            return;
        }

        double result = convert(from, to, i);
        resultsTextView.setText("Result: " + String.format("%.2f", result) + " degrees");
        resultsTextView.setVisibility(View.VISIBLE);
    }

    public void toast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public double convert(String from, String to, double num){
        if (from.equals(to)) return num;
        return (from.equals("Fahrenheit")) ? (num - 32.0) * (5.0 / 9.0) : num * (9.0 / 5.0) + 32.0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temperature_converter, container, false);
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
