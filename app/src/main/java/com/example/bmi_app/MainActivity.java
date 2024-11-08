package com.example.bmi_app;

import android.annotation.SuppressLint;
import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText weightInput, heightInput;
    private TextView bmiResult, bmiCategory;
    private Button calculateButton;
    private ImageButton historyButton;
    private SimpleDateFormat date_Formatter;

    @SuppressLint("SimpleDateFormat")
    public MainActivity(){
        date_Formatter = new SimpleDateFormat("dd/MM/yyyy");
    }

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
                saveDataToJson();
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

    private void saveDataToJson(){
        JSONArray jsonArray = new JSONArray();
        String filename = getString(R.string.file_name);
        // Try reading the existing JSON file (if any)
        try (FileInputStream fis = openFileInput(filename);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)){
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // If the file isn't empty, parse it into a JSONArray
            if (stringBuilder.length() > 0) {
                jsonArray = new JSONArray(stringBuilder.toString());
            }
        } catch (FileNotFoundException e) {
            // Handle file not found (file might not exist yet)
            Log.v("save","File Not Found");
        } catch (IOException | JSONException e) {
            // Handle other exceptions (e.g., IO errors or JSON parsing errors)
            Log.v("save",e.getMessage(),e);
        }


        try {
            // รับค่าจาก EditText
            String date = date_Formatter.format(new Date());
            String weight = weightInput.getText().toString();
            String height = heightInput.getText().toString();
            String bmi = bmiResult.getText().toString();
            String status = bmiCategory.getText().toString();

            // สร้าง JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", date);
            jsonObject.put("weight", weight);
            jsonObject.put("height", height);
            jsonObject.put("bmi", bmi);
            jsonObject.put("status", status);

            jsonArray.put(jsonObject);

            // เขียน JSON ลง Internal Storage
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(jsonArray.toString());
            osw.close();
            fos.close();

        } catch (Exception e) {
            Log.v("save",e.getMessage(),e);
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