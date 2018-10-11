package com.example.nitishatal.quizapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "Q";
    public static final String EXTRA_MESSAGE2 = "Q2";
    //DbHelper mDatabaseHelper;
    private RecyclerView cities;
    private RecyclerView.Adapter mAdapter;
    ListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Context context;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Frag1 f1=new Frag1();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction manager1=manager.beginTransaction();



        //FragmentManager manager2=getSupportFragmentManager();
        //manager2.beginTransaction().replace(R.id.layout2,f2,f2.getTag()).commit();
        manager1.add(R.id.fragment,f1);

        manager1.commit();
        final ArrayList<City> cities=initCities();

        this.cities= (RecyclerView) findViewById(R.id.rlist);
        mLayoutManager = new LinearLayoutManager(this);
        this.cities.setLayoutManager(mLayoutManager);
        //mDatabaseHelper = new DbHelper(this);



        // specify an adapter (see also next example)
        adapter = new ListAdapter(context, cities, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("ds", "clicked position:" + position);
                Toast.makeText(getApplicationContext(), "Question "+(position+1)+" Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, QuizDetail.class);
                String msg= QuizBook.questions[position];
                i.putExtra("msg", msg);
                i.putExtra("id",position+1);
                //Bundle extras = new Bundle();
                ///extras.putString(EXTRA_MESSAGE,msg);
                //extras.putInt(EXTRA_MESSAGE2,position+1);
                //i.putExtras(extras);
                startActivity(i);
            }

        });
       // mAdapter = new ListAdapter(MainActivity.this,cities);
        this.cities.setAdapter(adapter);


        /*this.cities.setOnClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String cities2 = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(context,cities2,Toast.LENGTH_LONG).show();
                    }
                }

        );*/
        /*
        this.cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, "dsd", Toast.LENGTH_SHORT).show();
            }
        });
        */






    }

    public void onClick(View view, int position) {
        Toast.makeText(context, "dsd", Toast.LENGTH_SHORT).show();

    }
    private ArrayList<City> initCities(){
        ArrayList<City> list=new ArrayList<>();

        for(int i=1;i<=30;i++){
            list.add(new City("Question"+i));
        }
        return list;



    }



}
