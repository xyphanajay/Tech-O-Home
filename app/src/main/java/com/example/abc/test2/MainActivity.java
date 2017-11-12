package com.example.abc.test2;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=(Button)findViewById(R.id.login);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        TextView pc=(TextView) findViewById(R.id.pc);

        ImageView app=(ImageView)findViewById(R.id.app);
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abtapp=new Intent(MainActivity.this,aboutapp.class);
                startActivity(abtapp);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=email.getText().toString();
                String pass=password.getText().toString();
                if(mail.equals("")||pass.equals("")){
                    Toast.makeText(MainActivity.this,"Empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SQLiteDatabase
                            data=openOrCreateDatabase("function",MODE_PRIVATE,null);
                    data.execSQL("create table if not exists func(name varchar,email varchar,password varchar)");
                    String s="select  * from func where email='"+mail+"' and password='"+pass+"'";
                    Cursor cursor=data.rawQuery(s,null);
                    if(cursor.getCount()>0)
                    {
                        Toast.makeText(MainActivity.this,"Logging In",Toast.LENGTH_SHORT).show();
                        Intent logg=new Intent(MainActivity.this,home.class);
                        startActivity(logg);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Wrong Info",Toast.LENGTH_SHORT).show();
                        Intent x=new Intent(MainActivity.this,MainActivity.class);
                        startActivity(x);
                        finish();
                    }
                }
            }
        });
        pc.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutus=new Intent(MainActivity.this,aboutus.class);
                startActivity(aboutus);

            }
        }));
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), register.class);
                startActivity(i);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
