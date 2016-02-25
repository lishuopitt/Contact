package com.example.lishuo.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> ContactDetails=new ArrayList<String>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fillAllUser();
        listView = (ListView) findViewById(R.id.listView);
        populateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position is the location of item clicked
                editContact(position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //position is the location of item clicked
                Log.i("Results long", Integer.toString(position));
                popUpDelete(position);
                return false;
            }
        });

    }

    public void fillAllUser(){
        try {
            SQLiteDatabase eventsDB = this.openOrCreateDatabase("Contents", MODE_PRIVATE, null);
            eventsDB.execSQL("CREATE TABLE IF NOT EXISTS contents (name VARCHAR, year INT(4))");
            Cursor c = eventsDB.rawQuery("SELECT * FROM contents", null);
            int nameIndex = c.getColumnIndex("name");
            int phoneIndex = c.getColumnIndex("year");
            c.moveToFirst();
            while (c != null) {
                Log.i("Results - name", c.getString(nameIndex));
                Log.i("Results - year", c.getString(phoneIndex));
                ContactDetails.add(c.getString(nameIndex) + "\n" + c.getString(phoneIndex));
                c.moveToNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteDataBase(){
        try {
            Log.i("Results","Try to delete");
            SQLiteDatabase eventsDB = this.openOrCreateDatabase("Contents", MODE_PRIVATE, null);
            eventsDB.execSQL("CREATE TABLE IF NOT EXISTS contents (name VARCHAR, year INT(4))");
            eventsDB.execSQL("DELETE FROM Contents");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            Intent i = new Intent(getApplicationContext(), AddContact.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.deleteAll) {
            DeleteDataBase();
            ContactDetails.clear();
            populateListView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void popUpDelete(final int index){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete.")
                .setMessage("Do you want to delete current contact?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                    //Dosometingifyoupressyes
                        Log.i("Tapped","Yes");
                        ContactDetails.remove(index);
                        populateListView();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                    //Dosometingifyoupressno
                        Log.i("Tapped","No");
                    }
                }).show();
    }
    public void editContact(int position){
        String name= ContactDetails.get(position);
        name.split("\n");
        Log.i("Results", name);
        //Intent i = new Intent(getApplicationContext(), AddContact.class);
       // i.putExtra("contact_name",name);

        // startActivity(i);
    }

    public void populateListView(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ContactDetails);
        listView.setAdapter(adapter);
    }
}
