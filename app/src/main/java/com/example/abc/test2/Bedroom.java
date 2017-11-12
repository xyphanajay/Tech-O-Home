package com.example.abc.test2;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
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


public class Bedroom extends AppCompatActivity {

    Button backb, btnOff, btnDis,two,three;
    ToggleButton ledb,lampb,curtb,tvb,acb,dimmerb,fannb;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    int i=0,j=0,k=0,l=0,m=0,n=0,p=0;
    //SPP UUID. Look for it
    RadioButton slow,fast;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_bedroom);
        String room="Bedroom";
        SQLiteDatabase datas = openOrCreateDatabase("gname",MODE_PRIVATE,null);
        datas.execSQL("CREATE TABLE IF NOT EXISTS TP(name VARCHAR,address VARCHAR);");
        Cursor cursor=datas.rawQuery("SELECT address FROM TP WHERE name='"+room+"'", null);
        // address=cursor.getString(cursor.getColumnIndex("address"));
        //msg("0");
        //call the widgtes
        ledb=(ToggleButton)findViewById(R.id.ledb);
        lampb=(ToggleButton)findViewById(R.id.lampb);
        curtb=(ToggleButton)findViewById(R.id.curtb);
        tvb=(ToggleButton)findViewById(R.id.tvb);
        acb=(ToggleButton)findViewById(R.id.acb);
        dimmerb=(ToggleButton)findViewById(R.id.dimmerb);
        fannb=(ToggleButton)findViewById(R.id.fannb) ;
        slow=(RadioButton)findViewById(R.id.slow);
        fast=(RadioButton)findViewById(R.id.fast);
        address="00:21:13:01:22:71";//"98:D3:32:70:D8:17";
        new ConnectBT().execute(); //Call the class to connect
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
        fannb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p==0)
                {
                    p++;
                    turnOnLed("Z");
                }
                else{
                    p=0;
                    turnOffLed("X");
                }
            }
        });
        ledb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==0)
                {
                    i++;
                    turnOnLed("A");
                }
                else{
                    i=0;
                    turnOffLed("B");
                }
            }
        });

        lampb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(j==0){
                    j++;
                    turnOnLed("G");
                }
                else
                {
                    j=0;
                    turnOffLed("H");
                }
            }
        });

        curtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(k==0){
                    k++;
                    turnOnLed("P");
                }
                else{
                    k=0;
                    turnOffLed("R");
                }
            }
        });

        tvb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(l==0)
                {
                    l++;
                    turnOnLed("C");
                }
                else
                {
                    l=0;
                    turnOffLed("D");

                }
            }
        });

        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m==0)
                {
                    m++;
                    turnOnLed("E");
                }
                else{
                    m=0;
                    turnOffLed("F");
                }
            }
        });

        dimmerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(n==0){
                    n++;
                    turnOnLed("I");
                }
                else
                {
                    n=0;
                    turnOffLed("J");
                }
            }
        });

        backb=(Button)findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent home=new Intent(ledControl.this,home.class);
                Disconnect();
                //  startActivity(home);
            }
        });
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish();//return to the first layout

    }

    private void turnOffLed(String off)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(off.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed(String on)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(on.toString().getBytes());
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
            progress = ProgressDialog.show(Bedroom.this, "Connecting", "Please wait");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
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
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
