package com.example.abc.test2;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;

import static com.example.abc.test2.R.id.lamp;

public class Bath extends AppCompatActivity{

    Button backt;
    ToggleButton geyserb,washingb,lampb,ledb,fanb;
    SeekBar brightness;
    TextView lumn;
    String addres = null;
    private ProgressDialog progres;
    int i=0,j=0,k=0,l=0,m=0,n=0;
    BluetoothAdapter myBluetoot = null;
    BluetoothSocket btSocke = null;
    private boolean isBtConnecte = false;
    //SPP UUID. Look for it
    static final UUID myUUI = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bath);

        washingb=(ToggleButton)findViewById(R.id.washingb);
        ledb= (ToggleButton)findViewById(R.id.ledb);
        geyserb= (ToggleButton)findViewById(R.id.geyserb);
        fanb=(ToggleButton)findViewById(R.id.fanb);

        addres="00:21:13:01:24:77";//00:21:13:01:22:F3
        new ConnectBT().execute(); //Call the class to connect


        backt=(Button)findViewById(R.id.backt);
        backt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Disconnect();
                /*
                Intent bath=new Intent(Bath.this,home.class);
                startActivity(bath);*/
            }
        });
        fanb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(n==0)
                {
                    n++;
                    turnOnLed("C");
                }
                else{
                    n=0;
                    turnOffLed("D");
                }
            }
        });
        ledb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(j==0)
                {
                    j++;
                    turnOnLed("A");
                }
                else{
                    j=0;
                    turnOffLed("B");
                }   //method to turn off
            }
        });

        washingb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(k==0)
                {
                    k++;
                    turnOnLed("G");
                }
                else{
                    k=0;
                    turnOffLed("H");
                } //close connection
            }
        });

        geyserb.setOnClickListener(new View.OnClickListener() {
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
            progres = ProgressDialog.show(Bath.this, "Connecting", "Please wait");  //show a progress dialog
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
