package com.example.nitishatal.quizapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;


import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuizDetail extends AppCompatActivity {
    DbHelper mDatabaseHelper;

    private int serverResponseCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        /*Frag2 f2=new Frag2();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction manager2=manager.beginTransaction();
        manager2.add(R.id.fragment2,f2);
        manager2.commit();*/

        Intent intent = getIntent();
       // Bundle extras = intent.getExtras();
        //String message=extras.getString("Q");
        //int id=extras.getInt("Q2");
        //final String strid=getString(id);
        String message = intent.getStringExtra("msg");
        final int id=intent.getIntExtra("id",-1);
        //final String strid=getString(id);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.qtext);
        //final TextView textView1=findViewById(R.id.t);
        textView.setText(message);
        Button Save=(Button) findViewById(R.id.save);
        final Button True=(Button) findViewById(R.id.t);
        Button False=(Button) findViewById(R.id.f);
        Button Dbase=(Button) findViewById(R.id.dt);
        Button Submit=(Button) findViewById(R.id.submit);
        mDatabaseHelper = new DbHelper(this);
        //new BackgroundUploader("das",new File("ds")).execute();





        /*True.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.addAnswer(true);

            }
        });

        False.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.addAnswer(false);
                True.setOnClickListener(false);

            }
        });*/

        //final boolean[] answr = new boolean[1];
        final String[] answr = {""};
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.t) {
                    Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();
                    //mDatabaseHelper.UpdateAns(strid,"true");
                   // mDatabaseHelper.Update(id,"true");
                    answr[0] ="true";
                } else if(checkedId == R.id.f) {
                    Toast.makeText(getApplicationContext(), "False", Toast.LENGTH_SHORT).show();
                    //mDatabaseHelper.UpdateAns(strid,"false");
                    //mDatabaseHelper.Update(id,"false","null");
                    answr[0] ="false";
                }
            }

        });

        for(int i=0;i<30;i++){
            mDatabaseHelper.addData(QuizBook.questions[i],"null");
        }

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseHelper.Update(id,answr[0]);

            }
        });

        Dbase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data=mDatabaseHelper.getData();
                //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuizDetail.this, DbActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);


            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Filename="QuizQnA.csv";
                File file = new File(getApplicationContext().getFilesDir(), Filename);
                try {
                    FileOutputStream out=openFileOutput(Filename, Context.MODE_PRIVATE);
                    mDatabaseHelper.export(out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myClickHandler();

                new Async().execute("");

            }
        });



    }
    public void myClickHandler() {
        // Gets the URL from the UI's text field.
        //String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //new DownloadWebpageText().execute(stringUrl);
            Toast.makeText(getApplicationContext(), " Network Connected", Toast.LENGTH_SHORT).show();

        } else {
            //textView.setText("No network connection available.");
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }
    public  void isConnected()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager!=null)
            Toast.makeText(getApplicationContext(), " Network Available", Toast.LENGTH_SHORT).show();
        {
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if (info!=null && info.getState()==NetworkInfo.State.CONNECTED)
            {

                    Toast.makeText(getApplicationContext(), " Network Connected", Toast.LENGTH_SHORT).show();
                  //  return true;

            }else{
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
      //  return false;
    }



    private class Async extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(QuizDetail.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... result) {

            try {
                String sourceFile = "/data/data/com.example.nitishatal.quizapp/files/QuizQnA.csv";



                File csvFile = new File(sourceFile);

                String upLoadServerUri = "http://192.168.0.104";

                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                HttpURLConnection net = (HttpURLConnection) url.openConnection();
                net.setDoInput(true);
                net.setDoOutput(true);
                //net.setUseCaches(false);
                net.setRequestMethod("POST");
                net.setRequestProperty("Connection", "Keep-Alive");
                net.setRequestProperty("ENCTYPE", "multipart/form-data");
                net.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "*****");
                net.setRequestProperty("nitish", sourceFile);

                DataOutputStream dataos = new DataOutputStream(net.getOutputStream());

                dataos.writeBytes("--" + "*****" + "\r\n");
                dataos.writeBytes("Content-Disposition: form-data; name=\"nitish\";filename=\"" + sourceFile + "\"" + "\r\n");
                dataos.writeBytes("\r\n");
                int bytesAvailable = fileInputStream.available();

                int bufferSize = Math.min(bytesAvailable, 10);//buffer size is 10
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                int progress = 0;

                while (bytesRead > 0) {
                    dataos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, 10);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    // System.out.println(progress);
                    Log.d("das","progress"+progress);
                    progress += 1;
                    publishProgress((int) ((progress * 100) / (sourceFile.length())));
                    publishProgress(progress);
                }

                dataos.writeBytes("\r\n");
                dataos.writeBytes("--" + "*****" + "--" + "\r\n");

                // Responses from the server (code and message)
                serverResponseCode = net.getResponseCode();
                //      String serverResponseMessage = net.getResponseMessage();
                if (serverResponseCode == 200) {
                }
                fileInputStream.close();
                dataos.flush();
                dataos.close();
                //} catch (Exception e) {
                //  e.printStackTrace();
                //}

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress((progress[0]));
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
            }



        }
    }


    //public  void AddQ(String g){
      //  mDatabaseHelper.addData(g);
    //}

}
