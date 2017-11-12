package com.example.abc.test2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class register extends AppCompatActivity {
    Button sign;
    EditText name,email,password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_register);
        TextView pc=(TextView) findViewById(R.id.pc);
        ImageView app1=(ImageView)findViewById(R.id.app1);
        sign=(Button)findViewById(R.id.sign);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=name.getText().toString();
                String mail=email.getText().toString();
                String pass=password.getText().toString();
                if(fname.equals("")||pass.equals("")||mail.equals(""))
                {
                    Toast.makeText(register.this, "Empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SQLiteDatabase
                            data=openOrCreateDatabase("function",MODE_PRIVATE,null);
                    data.execSQL("create table if not exists func(name varchar, email varchar,password varchar)");
                    String s="select * from func where email='"+mail+"'";
                    Cursor cursor=data.rawQuery(s,null);
                    if(cursor.getCount()>0)
                    {
                        Toast.makeText(register.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        data.execSQL("insert into func values('"+fname+"','"+mail+"','"+pass+"')");
                        Toast.makeText(register.this, "Signed Up", Toast.LENGTH_SHORT).show();
                        Intent log=new Intent(register.this,MainActivity.class);
                        startActivity(log);
                    }
                }
            }
        });


        pc.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about=new Intent(register.this,aboutus.class);
                startActivity(about);
            }
        }));
        app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent app1=new Intent(register.this,aboutapp.class);
                startActivity(app1);
            }
        });
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent login=new Intent(register.this,MainActivity.class);
                startActivity(login);
            }
        });
    }
}
