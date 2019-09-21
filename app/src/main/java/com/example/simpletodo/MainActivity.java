package com.example.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;

    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        // etItem.setText("I'm doing this from java!");

        /* items = new ArrayList<String>();
        items.add("Buy milk");
        items.add("Go to the gym");
        */
        loadItems();

        ItemsAdapter.OnLongClickListener longClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClick(int position) {
                //delete the item from the model
                items.remove(position);
                //notify the adapter at which position we deleted an item
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };
        itemsAdapter = new ItemsAdapter(items, longClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getText() returns an Editable (not a string)
                String todoItem = etItem.getText().toString();

                // Add todoItem to the model
                items.add(todoItem);
                //Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "ToDoData.txt");
    }
    //this function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("MainActivity", "Error Reading items", e);
            items = new ArrayList<>();
        }
    }

    //this function will save items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("MainActivity", "Error Writing items", e);
        }
    }

}
