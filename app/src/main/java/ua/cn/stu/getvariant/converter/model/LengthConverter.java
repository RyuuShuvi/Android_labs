package ua.cn.stu.getvariant.converter.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ua.cn.stu.getvariant.converter.R;
import ua.cn.stu.getvariant.converter.lab2.ConversionService;

public class LengthConverter extends Fragment {
    private ConversionService conversionService;
    private boolean bound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ConversionService.LocalBinder binder = (ConversionService.LocalBinder) service;
            conversionService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), ConversionService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bound) {
            getActivity().unbindService(connection);
            bound = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.length_converter, container, false);

        Spinner fromUnitSpinner = view.findViewById(R.id.fromUnitSpinner);
        Spinner toUnitSpinner = view.findViewById(R.id.toUnitSpinner);
        EditText inputField = view.findViewById(R.id.inputField);
        TextView resultField = view.findViewById(R.id.resultField);
        Button convertButton = view.findViewById(R.id.convertButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.length_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(v -> {
            if (bound) {
                String fromUnit = fromUnitSpinner.getSelectedItem().toString();
                String toUnit = toUnitSpinner.getSelectedItem().toString();
                double inputValue = Double.parseDouble(inputField.getText().toString());
                double result = conversionService.convert(inputValue, fromUnit, toUnit);
                resultField.setText(String.valueOf(result));
            }
        });

        return view;
    }
}


