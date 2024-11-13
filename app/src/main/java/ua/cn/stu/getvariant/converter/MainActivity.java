package ua.cn.stu.getvariant.converter;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ua.cn.stu.getvariant.converter.model.LengthConverterActivity;
import ua.cn.stu.getvariant.converter.model.TempConverterActivity;
import ua.cn.stu.getvariant.converter.model.WeightConverterActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Обробка натискання кнопок для переходу до конвертерів
        Button btnLengthConverter = findViewById(R.id.btnLengthConverter);
        Button btnWeightConverter = findViewById(R.id.btnWeightConverter);
        Button btnTempConverter = findViewById(R.id.btnTempConverter);

        btnLengthConverter.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LengthConverterActivity.class)));
        btnWeightConverter.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WeightConverterActivity.class)));
        btnTempConverter.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TempConverterActivity.class)));
    }
}
