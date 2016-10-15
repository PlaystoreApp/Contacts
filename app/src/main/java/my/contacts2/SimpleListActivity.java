package my.contacts2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user-1 on 30-Jun-16.
 */
public class SimpleListActivity extends ActionBarActivity {
    private ArrayList<HashMap<String,Object>> names = new ArrayList<HashMap<String, Object>>();
    private ListView myList;

    public static final String NAME = "name";
    public static final String ROLLNUM = "rollnum";
    public static final String ID = "_index";
    public static final String ISEDIT = "is_edit";
    public static final int REG_REQ_CODE = 55;
    public static final int REG_RESULT_CODE = 56;
    public static final int ADD = 110;
    public static final int EDIT = 111;
    public static final int DELETE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);

        myList = (ListView)findViewById(R.id.mylist);
        getAllFromDB();

        registerForContextMenu(myList);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void getAllFromDB(){
        names = new ArrayList<HashMap<String, Object>>();
        MyDB myDB = new MyDB(this);
        myDB.openDB();
        Cursor cursor = myDB.getAllUsers();
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            HashMap<String,Object> hm = new HashMap<String,Object>();
            hm.put(NAME,cursor.getString(cursor.getColumnIndex(MyDB.STUDENT_NAME)));
            hm.put(ROLLNUM,cursor.getInt(cursor.getColumnIndex(MyDB.STUDENT_ROLL)));
            hm.put(ID,cursor.getLong(cursor.getColumnIndex(MyDB.STUDENT_ID)));
            names.add(hm);
            cursor.moveToNext();
        }
        cursor.close();
        myDB.closeDB();
        //..........list adapter................
        // R.layout.simpleListbox
        //R.layout.list
        SimpleAdapter adapter = new SimpleAdapter(this,
                names,
                R.layout.list,
                new String[]{NAME,ROLLNUM},
                new int[]{R.id.nameholder,R.id.rollnumholder});
        myList.setAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Do you want to ?");
        menu.add(Menu.NONE,EDIT,0,"Edit");
        menu.add(Menu.NONE,DELETE,0,"Delete");

        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()){
            case EDIT:
                Intent intent = new Intent(this,RegisterActivity.class);
                intent.putExtra(ISEDIT,true);
                intent.putExtra(ID,(Long)names.get(info.position).get(ID));

                startActivityForResult(intent, REG_REQ_CODE);
                break;
            case DELETE:
                MyDB myDB = new MyDB(this);
                myDB.openDB();
                myDB.deleteUser((Long)names.get(info.position).get(ID));
                myDB.closeDB();
                getAllFromDB();

                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            finish();
        }else if (id == R.id.action_add){
            Intent intent = new Intent(this,RegisterActivity.class);
            intent.putExtra(ISEDIT,false);
            startActivityForResult(intent,REG_REQ_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REG_REQ_CODE){
            if (resultCode == REG_RESULT_CODE){

                getAllFromDB();

            }
        }

    }

}
