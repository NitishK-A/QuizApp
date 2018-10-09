package com.example.nitishatal.quizapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
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
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

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
                    mDatabaseHelper.Update(id,"true","null");
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
               // Cursor data = mDatabaseHelper.retrieve();
                //String a=data.getString(0);
                /*try {
                    mDatabaseHelper.exportDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                mDatabaseHelper.Update(id,answr[0],"null");
                //String data=mDatabaseHelper.getData();
                //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                /*String Filename="data.csv";
                try {
                    FileOutputStream out=openFileOutput(Filename, Context.MODE_PRIVATE);
                    mDatabaseHelper.export(out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/


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

                /*String Filename="data.csv";
                try {
                    FileOutputStream out = openFileOutput(Filename, Context.MODE_PRIVATE);
                    try {
                        mDatabaseHelper.databasexport(out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                String Filename="data.csv";
                File file = new File(getApplicationContext().getFilesDir(), Filename);
                try {
                    FileOutputStream out=openFileOutput(Filename, Context.MODE_PRIVATE);
                    mDatabaseHelper.export(out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myClickHandler();
                new BackgroundUploader("http://192.168.60.54",new File("/data/data/com.example.nitishatal.quizapp/files/data.csv")).execute();


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

        public class BackgroundUploader extends AsyncTask<Void, Integer, String> {

        private ProgressDialog progressDialog;
        private String url;
        private File file;


        public BackgroundUploader(String url, File file) {
            this.url = url;
            this.file = file;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(QuizDetail.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading...");
            // progressDialog.setCancelable(false);

            //   progressDialog.setMax((int) file.length());
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... v) {
            String res = "fail";
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;
            String fileName = file.getName();
            //String fileName = "";
            if (file.getName().toLowerCase().endsWith(".csv")) {
                fileName = System.currentTimeMillis() + ".csv";
            } else if (file.getName().toLowerCase().endsWith(".png")) {
                fileName = System.currentTimeMillis() + ".png";
            } else if (file.getName().toLowerCase().endsWith(".bmp")) {
                fileName = System.currentTimeMillis() + ".bmp";
            }
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + "" + "\r\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: application/octet-stream\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = file.length() + tail.length();
                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + fileHeader;

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    out.flush();
                    progress += bytesRead;
                    // update progress bar
                    publishProgress((int) ((progress * 100) / (file.length())));
                    //  publishProgress(progress);
                }

                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();
                System.out.println(connection.getResponseCode());
                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                    //Toast.makeText(getApplicationContext(), " Webserver", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // Exception
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }
            return res;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress((int) (progress[0]));
        }



        @Override
        protected void onPostExecute(String v) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    //public  void AddQ(String g){
      //  mDatabaseHelper.addData(g);
    //}

}
