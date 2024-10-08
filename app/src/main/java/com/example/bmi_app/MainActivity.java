package com.example.bmi_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText weightInput, heightInput;
    private TextView bmiResult, bmiCategory;
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        weightInput = findViewById(R.id.weight_input);
        heightInput = findViewById(R.id.height_input);
        bmiResult = findViewById(R.id.bmi_value);
        bmiCategory = findViewById(R.id.bmi_status);
        calculateButton = findViewById(R.id.calculate_button);

        weightInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        heightInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void bmiCalculate(){
        String weightStr = weightInput.getText().toString();
        String heightStr = heightInput.getText().toString();
        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr) / 100; // Convert cm to meters

            double bmi = weight / (height * height);
            DecimalFormat df = new DecimalFormat("#,###.##");
            bmiResult.setText(df.format(bmi));

            // Determine BMI category
            String category;
            if (bmi < 16) {
                category = getString(R.string.severe_thinness);
            } else if (bmi >= 16 && bmi < 17 ) {
                category = getString(R.string.moderate_thinness);
            } else if (bmi >= 17 && bmi < 18.5) {
                category = getString(R.string.mild_thinness);
            } else if (bmi >= 18.5 && bmi < 25) {
                category = getString(R.string.normal);
            } else if (bmi >= 25 && bmi < 30) {
                category = getString(R.string.overweight);
            }else if (bmi >= 30 && bmi < 35) {
                category = getString(R.string.obese_class_i);
            }else if (bmi >= 35 && bmi < 40) {
                category = getString(R.string.obese_class_ii);
            }else {
                category = getString(R.string.obese_class_iii);
            }

            bmiCategory.setText(category);
        }
    }
}

class DecimalDigitsInputFilter implements InputFilter {
    private Pattern mPattern;
    DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +
                "})?)||(\\.)?");
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}