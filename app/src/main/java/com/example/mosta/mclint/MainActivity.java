package com.example.mosta.mclint;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    //declaration for our attribute
    private TextView massageFromTheServer;
    private EditText massageSendForTheServer ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendBtn = findViewById(R.id.Send);
        massageFromTheServer  = findViewById(R.id.re_massage);
        massageSendForTheServer = findViewById(R.id.send_massage);




        /* when Click on the Button massage send to the server
        and receive the massage again after that display it in the TextView section
         */
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.Send){
                    sendMassage(massageSendForTheServer.getText().toString());

                }
            }
        });
    }

    // make a sendMassage method to control the sending and receiving massage
    private void sendMassage(final String msg){

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // make a socket that have a host and port to receive from the server
                    final Socket s = new Socket("192.168.1.4", 7777);

                    OutputStream out = s.getOutputStream();
                    PrintWriter output = new PrintWriter(out);

                    output.println(msg);
                    output.flush();
                    // create a buffering object to read the text send by the server back to the clint
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    final String st = input.readLine();

                    // get the massage and display it in the TextView section
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String s = massageFromTheServer.getText().toString();
                            if (st.trim().length() != 0)
                                massageFromTheServer.setText(s + "\nServer : " + st);
                        }
                    });
                    // end ( close ) socket , out , output object
                    output.close();
                    out.close();
                    s.close();


                }
                // make an Exception for the code from crashing out
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // to start the thread method
        thread.start();
    }
}

