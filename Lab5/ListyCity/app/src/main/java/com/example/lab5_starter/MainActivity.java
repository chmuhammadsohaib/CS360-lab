package com.example.lab5_starter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CityDialogFragment.CityDialogListener {

    private Button addCityButton;
    private Button deleteCityButton;
    private ListView cityListView;

    private ArrayList<City> cityArrayList;
    private CityArrayAdapter cityArrayAdapter;

    private City selectedCity = null;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set views
        addCityButton = findViewById(R.id.buttonAddCity);
        deleteCityButton = findViewById(R.id.buttonDeleteCity);
        cityListView = findViewById(R.id.listviewCities);

        // create city array
        cityArrayList = new ArrayList<>();
        cityArrayAdapter = new CityArrayAdapter(this, cityArrayList);
        cityListView.setAdapter(cityArrayAdapter);

        db = FirebaseFirestore.getInstance();

        db.collection("cities").addSnapshotListener((value, error) -> {
            if (error != null || value == null) return;
            cityArrayList.clear();
            for (QueryDocumentSnapshot doc : value) {
                City city = doc.toObject(City.class);
                city.setId(doc.getId());
                cityArrayList.add(city);
            }
            selectedCity = null;
            cityArrayAdapter.clearSelection();
            deleteCityButton.setEnabled(false);
        });

        // set listeners
        addCityButton.setOnClickListener(view -> {
            CityDialogFragment cityDialogFragment = new CityDialogFragment();
            cityDialogFragment.show(getSupportFragmentManager(),"Add City");
        });

        cityListView.setOnItemClickListener((adapterView, view, i, l) -> {
            City city = cityArrayAdapter.getItem(i);
            CityDialogFragment cityDialogFragment = CityDialogFragment.newInstance(city);
            cityDialogFragment.show(getSupportFragmentManager(),"City Details");
        });

        cityListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            selectedCity = cityArrayAdapter.getItem(i);
            cityArrayAdapter.setSelectedPosition(i);
            deleteCityButton.setEnabled(true);
            return true;
        });

        deleteCityButton.setOnClickListener(view -> {
            if (selectedCity != null && selectedCity.getId() != null) {
                db.collection("cities").document(selectedCity.getId()).delete();
                selectedCity = null;
                cityArrayAdapter.clearSelection();
                deleteCityButton.setEnabled(false);
            }
        });

    }

    @Override
    public void updateCity(City city, String title, String year) {
        city.setName(title);
        city.setProvince(year);

        if (city.getId() != null) {
            HashMap<String, Object> updates = new HashMap<>();
            updates.put("name", title);
            updates.put("province", year);
            db.collection("cities").document(city.getId()).update(updates);
        }
    }

    @Override
    public void addCity(City city){
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", city.getName());
        data.put("province", city.getProvince());
        db.collection("cities").add(data);
    }

}
