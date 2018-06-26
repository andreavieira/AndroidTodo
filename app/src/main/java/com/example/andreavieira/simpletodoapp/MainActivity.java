package com.example.andreavieira.simpletodoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Objects to be used
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference to ListView
        lvItems = (ListView) findViewById(R.id.lvItems);
        //Initialize items list
        readItems();
        //Initialize the adapter, using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //Wire adapter to view
        lvItems.setAdapter(itemsAdapter);

        //Add items to list
        //items.add("First todo item");
        //items.add("Second todo item");

        //Setup the listener on creation
        setupListViewListener();
    }

    private void setupListViewListener() {
        //Set ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Remove the item in the list at the index given by position
                items.remove(position);
                //Notify the adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();
                //Store updates
                writeItems();
                //Return true to tell the framework that the long click was consumed
                Log.i("MainActivity", "Removed item " + position);
                return true;
            }
        });
    }

    public void onAddItem(View v) {
        //Reference to EditText created with layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //Turn EditText's content to String
        String itemText = etNewItem.getText().toString();
        //Add item to list via adapter
        itemsAdapter.add(itemText);
        //Store updates
        writeItems();
        //Clear the EditText by setting it to empty String
        etNewItem.setText("");
        //Display notif
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    //Returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    //Read the items from the file system
    private void readItems() {
        try {
            //Create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            //Print the error to the console
            e.printStackTrace();
            //Just load an empty list
            items = new ArrayList<>();
        }
    }

    //Write the items to the filesystem
    private void writeItems() {
        try {
            //Save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //Print the error to the console
            e.printStackTrace();
        }
    }
}
