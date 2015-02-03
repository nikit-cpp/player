package com.github.nikit.cpp.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    ListView lvMain;
    EditText editText;
    ArrayAdapter<String> adapter;

    private static final String TAG = "PLAYER_TAG";


    String[] names = { "Иван I", "Иван II", "Иван III", "Иван IV", "Марья", "Петр I", "Петр II", "Петр III", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей", "Никита" };

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // находим список
        lvMain = (ListView) findViewById(R.id.lvMain);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "itemClick: position = " + position + ", id = "+ id);
                Intent i = new Intent(MyActivity.this, PlaybackActivity.class);
                startActivity(i);

            }
        });

        // создаем адаптер
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);


        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                /*String inputed = editText.getText().toString();

                Toast toast = Toast.makeText(getApplicationContext(), "Введено:"+inputed, Toast.LENGTH_SHORT);
                toast.show();*/

            }

            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                MyActivity.this.adapter.getFilter().filter(cs);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
