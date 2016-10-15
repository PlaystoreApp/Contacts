package my.contacts2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private EditText inputField;
    private ArrayList<String> names = new ArrayList<String>();
    private ListView myList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputField = (EditText)findViewById(R.id.namefield);
        Button save = (Button)findViewById(R.id.save);

        myList = (ListView)findViewById(R.id.mylist);
        myList.setOnItemClickListener(this);
        save.setOnClickListener(this);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        myList.setAdapter(adapter);
        updateListView();

    }

    private void updateListView(){
        // just notify the adapter that array list has got updated with new data
        adapter.notifyDataSetChanged();

        //in case a full reload needed
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        myList.setAdapter(adapter);
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_next:
                Intent intent = new Intent(this,SimpleListActivity.class);
                startActivity(intent);
                break;

            case R.id.action_settings:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.save:
                names.add(inputField.getText().toString());
                updateListView();
                inputField.setText("");
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,names.get(position)+" got clicked", Toast.LENGTH_LONG).show();
    }
}