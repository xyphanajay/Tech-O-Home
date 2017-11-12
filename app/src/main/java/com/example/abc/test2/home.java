package com.example.abc.test2;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static com.example.abc.test2.ledControl.myUUID;

public class home extends AppCompatActivity {


    Button bed,draw,kitchen,lawn,bath,balc,sec,connect,logout,exit,abtus,bed2,bath2,din;
    ImageView bedi,kiti,lawni,drawi,bathi,balci,seci,bed2i,bath2i,dini;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;

    private boolean isBtConnected = false;
    public static BluetoothSocket btSocket = null;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }
        kiti=(ImageView)findViewById(R.id.kiti);
        drawi=(ImageView)findViewById(R.id.drawi);
        lawni=(ImageView)findViewById(R.id.lawni);
        bedi=(ImageView)findViewById(R.id.bedi);
        bathi=(ImageView)findViewById(R.id.bathi);
        bed2i=(ImageView)findViewById(R.id.bed2i);
        bath2i=(ImageView)findViewById(R.id.bath2i);
        balci=(ImageView)findViewById(R.id.balci);
        seci=(ImageView)findViewById(R.id.seci);
        dini=(ImageView)findViewById(R.id.dini);
        din=(Button)findViewById(R.id.din);
        connect=(Button)findViewById(R.id.connect);
        logout=(Button)findViewById(R.id.logout);
        exit=(Button)findViewById(R.id.exit);
        abtus=(Button)findViewById(R.id.abtus);

        abtus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abt=new Intent(home.this,aboutus.class);
                startActivity(abt);
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view,"Working on it!", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                Intent bt=new Intent(home.this,DeviceList.class);
                startActivity(bt);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent out=new Intent(home.this, MainActivity.class);
                out.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                out.putExtra("EXIT",true);
                startActivity(out);
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                System.exit(0);
            }
        });
        bed=(Button)findViewById(R.id.bed);
        draw=(Button)findViewById(R.id.draw);
        kitchen=(Button)findViewById(R.id.kitchen);
        lawn=(Button)findViewById(R.id.lawn);
        bath=(Button)findViewById(R.id.bath);
        balc=(Button)findViewById(R.id.balc);
        sec=(Button)findViewById(R.id.sec);
        bed2=(Button)findViewById(R.id.bed2);
        bath2=(Button)findViewById(R.id.bath2);
/*
        if(DeviceList.x>0)
        {   Intent newint = getIntent();
            address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
            new ConnectBT().execute();
        }
        */
        bedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bed=new Intent(home.this,ledControl.class);
                bed.putExtra(DeviceList.EXTRA_ADDRESS, address);
                startActivity(bed);
            }
        });
        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bed=new Intent(home.this,ledControl.class);
                bed.putExtra(DeviceList.EXTRA_ADDRESS, address);
                startActivity(bed);
            }
        });
        bed2i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bed=new Intent(home.this,Bedroom.class);
                bed.putExtra(DeviceList.EXTRA_ADDRESS, address);
                startActivity(bed);
            }
        });
        bed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bed=new Intent(home.this,Bedroom.class);
                bed.putExtra(DeviceList.EXTRA_ADDRESS, address);
                startActivity(bed);
            }
        });
        drawi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent draw=new Intent(home.this,Draw.class);
                startActivity(draw);
            }
        });
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent draw=new Intent(home.this,Draw.class);
                startActivity(draw);
            }
        });
        kiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kitchen=new Intent(home.this,Kitchen.class);
                startActivity(kitchen);
            }
        });
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kitchen=new Intent(home.this,Kitchen.class);
                startActivity(kitchen);
            }
        });
        lawni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lawn=new Intent(home.this,Lawn.class);
                startActivity(lawn);
            }
        });
        lawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lawn=new Intent(home.this,Lawn.class);
                startActivity(lawn);
            }
        });
        bathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bath=new Intent(home.this,Bath.class);
                startActivity(bath);
            }
        });
        bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bath=new Intent(home.this,Bath.class);
                startActivity(bath);
            }
        });
        bath2i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bath=new Intent(home.this,Bath2.class);
                startActivity(bath);
            }
        });
        bath2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bath=new Intent(home.this,Bath2.class);
                startActivity(bath);
            }
        });
        din.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent din=new Intent(home.this,Din.class);
                startActivity(din);
            }
        });
        dini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent din=new Intent(home.this,Din.class);
                startActivity(din);
            }
        });
        balci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent balc=new Intent(home.this,Balcony.class);
                startActivity(balc);
            }
        });
        balc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent balc=new Intent(home.this,Balcony.class);
                startActivity(balc);
            }
        });
        seci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sec=new Intent(home.this,Security.class);
                startActivity(sec);
            }
        });
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sec=new Intent(home.this,Security.class);
                startActivity(sec);
            }
        });
    }

}



