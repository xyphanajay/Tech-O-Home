package com.example.abc.test2;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;

public class Draw extends AppCompatActivity {
    Button backd;
    ToggleButton acb, tvb, ledb,lampb,curtb,fanb;
    String addres = null;
    private ProgressDialog progres;
    int i=0,j=0,k=0,l=0,m=0,n=0;
    BluetoothAdapter myBluetoot = null;
    BluetoothSocket btSocke = null;
    private boolean isBtConnecte = false;
    //SPP UUID. Look for it
    RadioButton off,slow,fast;

    static final UUID myUUI = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);//call the widgtes
        lampb=(ToggleButton) findViewById(R.id.lampb);
        ledb=(ToggleButton)findViewById(R.id.ledb);
        curtb = (ToggleButton)findViewById(R.id.curtb);
        tvb = (ToggleButton)findViewById(R.id.tvb);
        acb = (ToggleButton)findViewById(R.id.acb);
        fanb=(ToggleButton)findViewById(R.id.fanb);

        addres="00:21:13:01:21:11";
        new ConnectBT().execute(); //Call the class to connect
        slow=(RadioButton)findViewById(R.id.slow);
        fast=(RadioButton)findViewById(R.id.fast);

        backd=(Button)findViewById(R.id.backd);
        backd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });

        slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnLed("Y");
            }
        });
        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnLed("Z");
            }
        });
        fanb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(n==0)
                {
                    n++;
                    turnOnLed("Z");
                }
                else{
                    n=0;
                    turnOffLed("X");
                }
            }
        });
        lampb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(i==0)
                {
                    i++;
                    turnOnLed("G");
                }
                else{
                    i=0;
                    turnOffLed("H");
                }      //method to turn on
            }
        });
        tvb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(j==0)
                {
                    j++;
                    turnOnLed("E");
                }
                else{
                    j=0;
                    turnOffLed("F");
                }   //method to turn off
            }
        });

        acb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(k==0)
                {
                    k++;
                    turnOnLed("C");
                }
                else{
                    k=0;
                    turnOffLed("D");
                } //close connection
            }
        });

        curtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(l==0)
                {
                    l++;
                    turnOnLed("P");
                }
                else{
                    l=0;
                    turnOffLed("R");
                }
            }
        });

        ledb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m==0)
                {
                    m++;
                    turnOnLed("A");
                }
                else{
                    m=0;
                    turnOffLed("B");
                }
            }
        });

    }

    private void Disconnect()
    {
        if (btSocke!=null) //If the btSocket is busy
        {
            try
            {
                btSocke.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOffLed(String off)
    {
        if (btSocke!=null)
        {
            try
            {
                btSocke.getOutputStream().write(off.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed(String on)
    {
        if (btSocke!=null)
        {
            try
            {
                btSocke.getOutputStream().write(on.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progres = ProgressDialog.show(Draw.this, "Connecting", "Please wait");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocke == null || !isBtConnecte)
                {
                    myBluetoot = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetoot.getRemoteDevice(addres);//connects to the device's address and checks if it's available
                    btSocke = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUI);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocke.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Turn on Module!");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnecte = true;
            }
            progres.dismiss();
        }
    }
}