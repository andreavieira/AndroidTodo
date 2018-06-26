package com.example.andreavieira.simpletodoapp;

import android.content.Intent;
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
    //OPTIONAL ACTIVITY: FOR EDITING LIST ITEMS
    //Numeric code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;
    //Keys used for passing data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

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

        //Set ListView's regular click listener
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //First parameter is the context, second is the class of the activity to launch
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //Put "extras" into the bundle for access in the edit activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //Brings up the edit activity with the expectation of a result
                startActivityForResult(i, EDIT_REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //EDIT_REQUEST_CODE defined with constants
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            //Extract updated item value from result extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //Get the position of the item which was edited
            int position = data.getExtras().getInt(ITEM_POSITION, 0);
            //Update the model with the new item text at the edited position
            items.set(position, updatedItem);
            //Notify the adapter the model changed
            itemsAdapter.notifyDataSetChanged();
            //Store the updated items back to disk
            writeItems();
            //Notify the user the operation completed OK
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
    }
}
