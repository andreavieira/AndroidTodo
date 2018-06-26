package com.example.andreavieira.simpletodoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.andreavieira.simpletodoapp.MainActivity.ITEM_POSITION;
import static com.example.andreavieira.simpletodoapp.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    // text field containing updated item description
    EditText etItemText;
    // we need to track the item's position in the list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //Resolve the text field from the layout
        etItemText = (EditText) findViewById(R.id.etItemText);
        //Set the text field's content from the intent
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //Track the position of the item in the list
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        //Set the title bar to reflect the purpose of the view
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v) {
        //Prepare intent to pass back to MainActivity
        Intent data = new Intent();
        //Pass updated item text and original position
        data.putExtra(ITEM_TEXT, etItemText.getText().toString());
        data.putExtra(ITEM_POSITION, position); // ints work too
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the edit activity, passes intent back to main
    }


}
