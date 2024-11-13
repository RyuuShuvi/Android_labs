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

public class LengthConverterActivity extends AppCompatActivity {

    private EditText inputField;
    private TextView resultField;
    private Spinner fromUnitSpinner, toUnitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length_converter);

        inputField = findViewById(R.id.inputField);
        resultField = findViewById(R.id.resultField);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.length_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        // Слухач для автоматичної конвертації при введенні даних
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                convertLength();
            }

            // Пусті методи, оскільки вони не потрібні для цього випадку
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void convertLength() {
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
                double result = performLengthConversion(inputValue, fromUnit, toUnit);
                resultField.setText(String.format(Locale.getDefault(), "%.2f", result));
            } catch (NumberFormatException e) {
            }
        }
    }

    private double performLengthConversion(double value, String fromUnit, String toUnit) {
        // Спочатку конвертуємо всі одиниці в метри
        double valueInMeters = convertToMeters(value, fromUnit);

        // Далі конвертуємо з метрів у кінцеву одиницю
        return convertFromMeters(valueInMeters, toUnit);
    }

    private double convertToMeters(double value, String fromUnit) {
        switch (fromUnit) {
            case "Сантиметр":
                return value / 100;
            case "Метр":
                return value;
            case "Кілометр":
                return value * 1000;
            case "Дюйм":
                return value * 0.0254;
            case "Миля":
                return value * 1609.34;
            case "Ярд":
                return value * 0.9144;
            case "Фут":
                return value * 0.3048;
            default:
                throw new IllegalArgumentException("Невідома одиниця виміру: " + fromUnit);
        }
    }

    private double convertFromMeters(double valueInMeters, String toUnit) {
        switch (toUnit) {
            case "Сантиметр":
                return valueInMeters * 100;
            case "Метр":
                return valueInMeters;
            case "Кілометр":
                return valueInMeters / 1000;
            case "Дюйм":
                return valueInMeters / 0.0254;
            case "Миля":
                return valueInMeters / 1609.34;
            case "Ярд":
                return valueInMeters / 0.9144;
            case "Фут":
                return valueInMeters / 0.3048;
            default:
                throw new IllegalArgumentException("Невідома одиниця виміру: " + toUnit);
        }
    }

}

