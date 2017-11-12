package com.example.abc.test2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Assign extends AppCompatActivity {

    String address = null;
    int id=1;
    Button assign;
    RadioGroup rbg;
    RadioButton one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);

        setContentView(R.layout.activity_assign);

        assign=(Button)findViewById(R.id.ok);
        rbg=(RadioGroup)findViewById(R.id.rg);

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbg.getCheckedRadioButtonId()!=-1) {
                    id = rbg.getCheckedRadioButtonId();
                    one=(RadioButton)findViewById(id);
                    String fname=one.getText().toString();
                    String mail=address;
                    if(fname.equals("")||mail.equals(""))
                    {
                        Toast.makeText(Assign.this, "Empty!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SQLiteDatabase datas = openOrCreateDatabase("gname",MODE_PRIVATE,null);
                        datas.execSQL("CREATE TABLE IF NOT EXISTS TP(name VARCHAR,address VARCHAR);");
                        Cursor resultSet = datas.rawQuery("SELECT * FROM TP WHERE name ='"+fname+"'",null);
                       if(resultSet.getCount()>0)
                       {
                                Toast.makeText(Assign.this, "Already assigned", Toast.LENGTH_SHORT).show();
                       }
                       else
                        {
                            String query = "INSERT INTO TP VALUES('"+fname+"', '"+mail+"');";
                            datas.execSQL(query);
                            Toast.makeText(Assign.this, "Assigned", Toast.LENGTH_SHORT).show();
                            Intent log=new Intent(Assign.this,DeviceList.class);
                            startActivity(log);
                        }
                    }

                    // Toast.makeText(Assign.this, room+" Assigned Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Assign.this, "Select Something!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Assign.this, one.getText().toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Assign.this, address, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
