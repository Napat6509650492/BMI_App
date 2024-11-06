package com.example.bmi_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText weightInput, heightInput;
    private TextView bmiResult, bmiCategory;
    private Button calculateButton;
    private ImageButton historyButton;

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
        historyButton = findViewById(R.id.history);
        weightInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        heightInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});


        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmiCalculate();
            }
        });


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
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_severe_thinness), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_severe_thinness), 128));
            } else if (bmi >= 16 && bmi < 17 ) {
                category = getString(R.string.moderate_thinness);
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_moderate_thinness), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_moderate_thinness), 128));
            } else if (bmi >= 17 && bmi < 18.5) {
                category = getString(R.string.mild_thinness);
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_mild_thinness), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_mild_thinness), 128));
            } else if (bmi >= 18.5 && bmi < 25) {
                category = getString(R.string.normal);
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_normal), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_normal), 128));
            } else if (bmi >= 25 && bmi < 30) {
                category = getString(R.string.overweight);
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_overweight), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_overweight), 128));
            }else if (bmi >= 30 && bmi < 35) {
                category = getString(R.string.obese_class_i);
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_obese_class_1), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_obese_class_1), 128));
            }else if (bmi >= 35 && bmi < 40) {
                category = getString(R.string.obese_class_ii);
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_obese_class_2), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_obese_class_2), 128));
            }else {
                category = getString(R.string.obese_class_iii);
                bmiCategory.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_obese_class_3), 128));
                bmiResult.setBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.bmi_obese_class_3), 128));
            }

            bmiCategory.setText(category);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        float newScale = newConfig.fontScale;

        float scaledTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_normal) * newScale;

        adjustFontSizeForAllViews(findViewById(R.id.main), newScale);
    }

    private void adjustFontSizeForAllViews(View view, float newScale) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                adjustFontSizeForAllViews(child, newScale); // เรียกซ้ำสำหรับลูกของ ViewGroup
            }
        } else if (view instanceof TextView) {
            // ปรับขนาดข้อความสำหรับ TextView
            TextView textView = (TextView) view;
            float scaledTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_normal) * newScale;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, scaledTextSize);
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