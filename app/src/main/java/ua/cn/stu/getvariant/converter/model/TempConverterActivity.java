package ua.cn.stu.getvariant.converter.model;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import ua.cn.stu.getvariant.converter.R;

public class TempConverterActivity extends AppCompatActivity {

    private EditText inputField;
    private TextView resultField;
    private Spinner fromUnitSpinner, toUnitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_converter);

        inputField = findViewById(R.id.inputField);
        resultField = findViewById(R.id.resultField);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.temp_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        // Автоматична конвертація при введенні
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                convertTemperature();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void convertTemperature() {
        String fromUnit = fromUnitSpinner.getSelectedItem().toString();
        String toUnit = toUnitSpinner.getSelectedItem().toString();
        String inputString = inputField.getText().toString();
        double inputValue;
        if(!inputString.isEmpty())
        {
            try{
                // Перевірка на те, що введений текст - це число
                inputValue = Double.parseDouble(inputString);
                double result = performTempConversion(inputValue, fromUnit, toUnit);
                resultField.setText(String.format(Locale.getDefault(), "%.2f", result));
            } catch (NumberFormatException e){
            }
        }
    }

    private double performTempConversion(double value, String fromUnit, String toUnit) {
        // Логіка конвертації температур між Кельвіном, Цельсієм і Фаренгейтом
        if (fromUnit.equals("Кельвін") && toUnit.equals("Цельсій")) {
            return value - 273.15;
        } else if (fromUnit.equals("Цельсій") && toUnit.equals("Кельвін")) {
            return value + 273.15;
        } else if (fromUnit.equals("Цельсій") && toUnit.equals("Фаренгейт")) {
            return (value * 9/5) + 32;
        } else if (fromUnit.equals("Фаренгейт") && toUnit.equals("Цельсій")) {
            return (value - 32) * 5/9;
        } else if (fromUnit.equals("Кельвін") && toUnit.equals("Фаренгейт")) {
            return (value - 273.15) * 9/5 + 32;
        } else if (fromUnit.equals("Фаренгейт") && toUnit.equals("Кельвін")) {
            return (value - 32) * 5/9 + 273.15;
        }
        return value;  // Якщо одиниці однакові або немає змін
    }
}

