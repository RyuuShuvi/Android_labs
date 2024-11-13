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

public class WeightConverterActivity extends AppCompatActivity {

    private EditText inputField;
    private TextView resultField;
    private Spinner fromUnitSpinner, toUnitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_converter);

        inputField = findViewById(R.id.inputField);
        resultField = findViewById(R.id.resultField);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weight_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        // Автоматична конвертація при введенні даних
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                convertWeight();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void convertWeight() {
        String fromUnit = fromUnitSpinner.getSelectedItem().toString();
        String toUnit = toUnitSpinner.getSelectedItem().toString();
        String inputString = inputField.getText().toString();
        double inputValue;
        if(!inputString.isEmpty())
        {
            try {
                // Перевірка на те, що введений текст - це число
                inputValue = Double.parseDouble(inputString);

                // Виконуємо конвертацію
                double result = performWeightConversion(inputValue, fromUnit, toUnit);
                resultField.setText(String.format(Locale.getDefault(), "%.2f", result));
            } catch (NumberFormatException e) {
            }
        }
    }

    private double performWeightConversion(double value, String fromUnit, String toUnit) {
        // Спочатку конвертуємо всі одиниці в грам
        double valueInGrams = convertToGrams(value, fromUnit);

        // Далі конвертуємо з грам у кінцеву одиницю
        return convertFromGrams(valueInGrams, toUnit);
    }

    private double convertToGrams(double value, String fromUnit) {
        switch (fromUnit) {
            case "Грам":
                return value;
            case "Кілограм":
                return value * 1000;
            case "Тона":
                return value * 1_000_000;
            case "Карат":
                return value * 0.2;
            case "Фунт":
                return value * 453.59237;
            case "Пуд":
                return value * 16_380.7;
            default:
                throw new IllegalArgumentException("Невідома одиниця виміру: " + fromUnit);
        }
    }

    private double convertFromGrams(double valueInGrams, String toUnit) {
        switch (toUnit) {
            case "Грам":
                return valueInGrams;
            case "Кілограм":
                return valueInGrams / 1000;
            case "Тона":
                return valueInGrams / 1_000_000;
            case "Карат":
                return valueInGrams / 0.2;
            case "Фунт":
                return valueInGrams / 453.59237;
            case "Пуд":
                return valueInGrams / 16_380.7;
            default:
                throw new IllegalArgumentException("Невідома одиниця виміру: " + toUnit);
        }
    }
}

