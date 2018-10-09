package com.example.nitishatal.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DbActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        Intent intent = getIntent();
        String dbtext=intent.getStringExtra("data");

        TextView textd=findViewById(R.id.dbt);
        textd.setText(dbtext);

    }
}
