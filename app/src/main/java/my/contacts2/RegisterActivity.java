package my.contacts2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by user-1 on 30-Jun-16.
 */
public class RegisterActivity extends ActionBarActivity {
    private EditText rollNumField,nameField;
    private Boolean isEdit = false;
    private long index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bundle bundle = getIntent().getExtras();
        nameField = (EditText)findViewById(R.id.namefield);
        rollNumField = (EditText)findViewById(R.id.rollnumfiled);
        Button save = (Button)findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDB myDB = new MyDB(RegisterActivity.this);
                myDB.openDB();
                Intent intent = new Intent();

                if (isEdit){
                    //edit

                    myDB.updateUserById(index,nameField.getText().toString(),Integer.parseInt(rollNumField.getText().toString()));
                    setResult(SimpleListActivity.REG_RESULT_CODE, intent);
                }else{
                    //add

                    myDB.addUser(nameField.getText().toString(),Integer.parseInt(rollNumField.getText().toString()));
                    setResult(SimpleListActivity.REG_RESULT_CODE,intent);
                }

                myDB.closeDB();

                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (bundle != null){
            isEdit = bundle.getBoolean(SimpleListActivity.ISEDIT);
            if (isEdit){
                //for editing
                index = bundle.getLong(SimpleListActivity.ID);
                MyDB myDB = new MyDB(this);
                myDB.openDB();
                Cursor cursor = myDB.getUserById(index);
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount();i++){
                    nameField.setText(cursor.getString(cursor.getColumnIndex(MyDB.STUDENT_NAME)));
                    int rollNum = cursor.getInt(cursor.getColumnIndex(MyDB.STUDENT_ROLL));
                    rollNumField.setText(Integer.toString(rollNum));
                    cursor.moveToNext();
                }
                cursor.close();
                myDB.closeDB();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

