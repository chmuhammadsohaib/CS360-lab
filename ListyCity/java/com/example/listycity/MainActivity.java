package com.example.listycity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;
    Button addCityButton;
    Button deleteCityButton;
    EditText cityInput;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);
        addCityButton = findViewById(R.id.add_city_button);
        deleteCityButton = findViewById(R.id.delete_city_button);
        cityInput = findViewById(R.id.city_input);

        String[] cities = {"Lahore", "Karachi", "Multan", "Islamabad", "Faisalabad", "Hyderabad", "Swat", "Peshawar", "Kel", "Muzaffarabad", "Gilgit", "Chillas"};
        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                deleteCityButton.setEnabled(true);
                String selectedCity = dataList.get(position);
                Toast.makeText(MainActivity.this,"Selected City: " + selectedCity, Toast.LENGTH_SHORT).show();
            }
        });
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCity();
            }
        });
        deleteCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedCity();
            }
        });
    }
    private void addCity() {
        String cityName = cityInput.getText().toString().trim();
        if (!cityName.isEmpty()) {
            dataList.add(cityName);
            cityAdapter.notifyDataSetChanged();
            cityInput.setText("");
        }
    }

    private void deleteSelectedCity() {
        if (selectedPosition != -1 && selectedPosition < dataList.size()) {
            dataList.remove(selectedPosition);
            cityAdapter.notifyDataSetChanged();
            selectedPosition = -1;
            deleteCityButton.setEnabled(false);
        }
    }
}