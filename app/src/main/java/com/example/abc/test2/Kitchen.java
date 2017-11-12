package com.example.abc.test2;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;

public class Kitchen extends AppCompatActivity {
    Button backk;
    ToggleButton ovenb, refigb, rob,lampb,efanb,fanb;
    SeekBar brightness;
    TextView lumn;
    String addres = null;
    private ProgressDialog progres;
    int i=0,j=0,k=0,l=0,m=0,n=0;
    BluetoothAdapter myBluetoot = null;
    BluetoothSocket btSocke = null;
    RadioButton off,slow,fast;

    private boolean isBtConnecte = false;
    //SPP UUID. Look for it
    static final UUID myUUI = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        lampb=(ToggleButton) findViewById(R.id.lampb);
        efanb=(ToggleButton)findViewById(R.id.efanb);
        refigb = (ToggleButton)findViewById(R.id.refigb);
        fanb=(ToggleButton)findViewById(R.id.fanb);

        slow=(RadioButton)findViewById(R.id.slow);
        fast=(RadioButton)findViewById(R.id.fast);

        addres="98:D3:32:70:D8:17";//00:21:13:01:22:F3
        new ConnectBT().execute(); //Call the class to connect

        backk=(Button)findViewById(R.id.backk);
        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                Intent backk=new Intent(Kitchen.this, home.class);
                startActivity(backk);*/
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
                    turnOnLed("A");
                }
                else{
                    i=0;
                    turnOffLed("B");
                }      //method to turn on
            }
        });
        efanb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(j==0)
                {
                    j++;
                    turnOnLed("C");
                }
                else{
                    j=0;
                    turnOffLed("D");
                }   //method to turn off
            }
        });

        refigb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(l==0)
                {
                    l++;
                    turnOnLed("E");
                }
                else{
                    l=0;
                    turnOffLed("F");
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
            progres = ProgressDialog.show(Kitchen.this, "Connecting", "Please wait");  //show a progress dialog
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
