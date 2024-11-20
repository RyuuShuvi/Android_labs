package ua.cn.stu.getvariant.converter;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ua.cn.stu.getvariant.converter.model.LengthConverter;
import ua.cn.stu.getvariant.converter.model.TempConverter;
import ua.cn.stu.getvariant.converter.model.WeightConverter;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnLengthConverter = findViewById(R.id.btnLengthConverter);
        Button btnWeightConverter = findViewById(R.id.btnWeightConverter);
        Button btnTempConverter = findViewById(R.id.btnTempConverter);

        btnLengthConverter.setOnClickListener(v -> replaceFragment(new LengthConverter()));
        btnWeightConverter.setOnClickListener(v -> replaceFragment(new WeightConverter()));
        btnTempConverter.setOnClickListener(v -> replaceFragment(new TempConverter()));
        Log.d("LAB3", "Updating UI on thread: " + Thread.currentThread().getId());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}




