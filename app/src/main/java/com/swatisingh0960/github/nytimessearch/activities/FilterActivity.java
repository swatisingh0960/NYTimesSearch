package com.swatisingh0960.github.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.swatisingh0960.github.nytimessearch.R;
import com.swatisingh0960.github.nytimessearch.extras.SearchFilters;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Swati on 10/22/2016.
 */

public class FilterActivity extends AppCompatActivity {

    SearchFilters filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        filters = Parcels.unwrap(getIntent().getParcelableExtra("filters"));

        if (filters.getBeginDate() != 0) {
            String begin = "" + filters.getBeginDate();
            Button startbtn = (Button) findViewById(R.id.btnStart);
            startbtn.setText(begin.substring(4, 6) + "/" + begin.substring(6, 8) + "/" + begin.substring(0, 4));
        }

        if (filters.getEndDate() != 0) {
            String end = "" + filters.getEndDate();
            Button endbtn = (Button) findViewById(R.id.btnEnd);

        }

        ArrayList<String> news = filters.getNewsDesks();
        for (int i = 0; i < news.size(); i++) {
            if (news.get(i).equalsIgnoreCase("\"Food\"")) {
                CheckBox check = ((CheckBox) findViewById(R.id.cbFood));
                check.setChecked(true);
            } else if (news.get(i).equalsIgnoreCase("\"Arts\"")) {
                CheckBox check = ((CheckBox) findViewById(R.id.cbArts));
                check.setChecked(true);
            } else if (news.get(i).equalsIgnoreCase("\"Sports\"")) {
                CheckBox check = ((CheckBox) findViewById(R.id.cbSports));
                check.setChecked(true);
            } else if (news.get(i).equalsIgnoreCase("\"Fashion\"")) {
                CheckBox check = ((CheckBox) findViewById(R.id.cbFashion));
                check.setChecked(true);
            } else if (news.get(i).equalsIgnoreCase("\"Science\"")) {
                CheckBox check = ((CheckBox) findViewById(R.id.cbScience));
                check.setChecked(true);
            } else if (news.get(i).equalsIgnoreCase("\"Magazine\"")) {
                CheckBox check = ((CheckBox) findViewById(R.id.cbMagazine));
                check.setChecked(true);
            } else {

            }
        }

        Spinner sortSpinner = (Spinner) findViewById(R.id.sSort);
        if (filters.getSort().equalsIgnoreCase("none")) {
            sortSpinner.setSelection(0);
        } else if (filters.getSort().equalsIgnoreCase("oldest")) {
            sortSpinner.setSelection(1);
        } else if (filters.getSort().equalsIgnoreCase("newest")) {
            sortSpinner.setSelection(2);
        } else {
            sortSpinner.setSelection(0);
        }

    }
    public FilterActivity(){
    }

    public void onSave(View view){
        getSort(); //update Spinner
        checkCheckboxes(); //update news desk
        filters.setUpdated(true); //update updated

        Intent data = new Intent();
        data.putExtra("filters",Parcels.wrap(filters)); //pass it back to search activity
        setResult(RESULT_OK,data);
        finish();
    }

    public void onClickStart(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Button startbtn = (Button) findViewById(R.id.btnStart);
                startbtn.setText(month+1 + "/" + dayOfMonth + "/" +year);

                String monthOfYear = Integer.toString(month+1);
                 if(month + 1 < 10 ){
                     monthOfYear = "0"+monthOfYear;
                 }

                Log.d("month",monthOfYear);

                String day = Integer.toString(dayOfMonth);
                if(dayOfMonth < 10){
                    day = "0" + day;
                }

                String date = Integer.toString(year) + monthOfYear + day;

                filters.setBeginDate(Integer.parseInt(date));
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onClickEnd(View view){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            //handle date selected
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Button endbtn = (Button) findViewById(R.id.btnEnd);
                endbtn.setText(month+1 + "/" + dayOfMonth + "/" + year);

                String monthOfYear = Integer.toString((month+1));
                if(month + 1 < 10) {
                    monthOfYear = "0" +monthOfYear;
                }
                Log.d("month",monthOfYear);

                String day = Integer.toString(dayOfMonth);
                if(dayOfMonth < 10){
                    day = "0" + day;
                }

                String date = Integer.toString(year) + monthOfYear + day;

                filters.setEndDate(Integer.parseInt(date));
            }
        });

        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void getSort(){
        Spinner sortSpinner = (Spinner) findViewById(R.id.sSort);
        String sort = sortSpinner.getSelectedItem().toString();

        filters.setSort(sort);
    }

    public void checkCheckboxes(){
        boolean foodchecked = ((CheckBox) findViewById(R.id.cbFood)).isChecked();
        if(foodchecked){
            filters.getNewsDesks().add("\"Food\"");
            filters.getNewsDesks().add("\"Dining\"");
        }
        else {
            filters.getNewsDesks().remove("\"Food\"");
            filters.getNewsDesks().remove("\"Dining\"");
        }

        boolean stchecked = ((CheckBox) findViewById(R.id.cbScience)).isChecked();
        if(stchecked){
            filters.getNewsDesks().add("\"Science\"");
            filters.getNewsDesks().add("\"Technology\"");
        }
        else {
            filters.getNewsDesks().remove("\"Science\"");
            filters.getNewsDesks().remove("\"Technology\"");
            Log.d("remove","removed");
        }
        boolean fasionchecked = ((CheckBox) findViewById(R.id.cbFashion)).isChecked();
        if(fasionchecked){
            filters.getNewsDesks().add("\"Fashion\"");
            filters.getNewsDesks().add("\"Style\"");
        }
        else {
            filters.getNewsDesks().remove("\"Fashion\"");
            filters.getNewsDesks().remove("\"Style\"");
        }
        boolean magchecked = ((CheckBox) findViewById(R.id.cbMagazine)).isChecked();
        if(magchecked) {
            filters.getNewsDesks().add("\"Magazine\"");
        }
        else {
            filters.getNewsDesks().remove("\"Magazine\"");
        }

        boolean artschecked = ((CheckBox) findViewById(R.id.cbArts)).isChecked();
        if(artschecked){
            filters.getNewsDesks().add("\"Arts\"");
        }
        else {
            filters.getNewsDesks().remove("\"Arts\"");
        }
        boolean sportschecked = ((CheckBox) findViewById(R.id.cbSports)).isChecked();
        if(sportschecked){
            filters.getNewsDesks().add("\"Sports\"");
        }
        else {
            filters.getNewsDesks().remove("\"Sports\"");
        }
    }
}