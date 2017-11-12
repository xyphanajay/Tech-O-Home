package com.example.abc.test2;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.MediaController;

import java.io.IOException;
import java.util.UUID;

public class Security extends AppCompatActivity {
Button backs;
    ToggleButton lock;
    VideoView cctv,vv;
    ProgressDialog pd;
    String url = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
    String addres = null;
    private ProgressDialog progres;
    int i=0,j=0,n=0;
    BluetoothAdapter myBluetoot = null;
    BluetoothSocket btSocke = null;
    private boolean isBtConnecte = false;
    //SPP UUID. Look for it
    static final UUID myUUI = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        backs=(Button)findViewById(R.id.backs);

        addres = "00:21:13:01:22:71";//00:21:13:01:22:F3
        new ConnectBT().execute(); //Call the class to connect
        lock=(ToggleButton)findViewById(R.id.lock);

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    i++;
                    turnOnLed("A");
                } else {
                    i = 0;
                    turnOffLed("B");
                }      //method to turn on
            }
        });

        //cctv=(VideoView)findViewById(R.id.cctv);
        vv=(VideoView)findViewById(R.id.vv);
        pd=new ProgressDialog(Security.this);
        pd.setTitle("CCTV Live");
        pd.setMessage("Loading");
        pd.setIndeterminate(false);
        pd.setCancelable(true);
        pd.show();
        try {
            MediaController mc=new MediaController(Security.this);
            mc.setAnchorView(vv);
            Uri video = Uri.parse(url);
            vv.setMediaController(mc);
            vv.setVideoURI(video);

        }catch (Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
        vv.requestFocus();
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                pd.dismiss();
                vv.start();
            }
        });
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/*
                Intent sec=new Intent(Security.this,home.class  );
                startActivity(sec);*/
                Disconnect();
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
            progres = ProgressDialog.show(Security.this, "Connecting", "Please wait");  //show a progress dialog
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