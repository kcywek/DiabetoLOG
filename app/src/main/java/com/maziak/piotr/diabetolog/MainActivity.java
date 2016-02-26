package com.maziak.piotr.diabetolog;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    EditText editSugarLevel;
    Button btnAddData;
    Button btnViewData;
    Button btnDeleteData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DatabaseHelper(this);
        editSugarLevel = (EditText)findViewById(R.id.editText2);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnViewData = (Button)findViewById(R.id.button);
        btnDeleteData = (Button)findViewById(R.id.button_delete);
        addData();
        viewAll();
        rysowanko();
        DeleteData();


    }
    public void addData(){
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date currentDate = new Date(System.currentTimeMillis());
                        boolean isInserted = myDB.insertData(currentDate.toString(), editSugarLevel.getText().toString());
                        if (isInserted == true)
                            Toast.makeText(MainActivity.this, "Poziom cukru wprowadzony", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Poziom nie cukru wprowadzony", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
        btnViewData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Cursor res =  myDB.getAllData();
                        if (res.getCount()==0){
                            showMessage("Error", "Nie ma danych");
                            return;
                        }

                            StringBuffer buffer = new StringBuffer();
                            while (res.moveToNext()){
                                buffer.append("DATE:" + res.getString(0) +"\n");
                                buffer.append("SUGAR_LEVEL:" + res.getString(1)+"\n");
                                buffer.append("----------------------------\n");

                            }
                        showMessage("Wprowadzone Wyniki", buffer.toString());


                    }
                }
        );
    }
    // test test
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void rysowanko() {

        LinearLayout ll = (LinearLayout) findViewById(R.id.rect);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        paint.setStrokeWidth(1);
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#00ee44"));
        paint2.setStrokeWidth(6);



        Cursor res =  myDB.getAllData();

       int szerokosc = res.getCount()*20;
       Bitmap bg = Bitmap.createBitmap(1920, 700, Bitmap.Config.ARGB_8888);
       Canvas canvas = new Canvas(bg);
       /* canvas.drawLine(0, 6, 480,6, paint);

        canvas.drawLine(0, 100, 480,100, paint);
        canvas.drawLine(0, 200, 480,200, paint);
        // edytowane w gh
        canvas.drawLine(0, 300, 480,300, paint);

        canvas.drawLine(0, 400, 480,400, paint);
        canvas.drawLine(0, 500, 480,500, paint);*/
        //edit on githu


        int ofset = 9;
        int i = 0;
        int j = 0;
        int k = ofset;
        while (res.moveToNext()){

            int x = res.getInt(res.getColumnIndex("SUGAR_LEVEL"));
            canvas.drawLine(i,j,k,x,paint2);
            i = k;
            j = x;
            k = k + ofset;


        }
        ll.setBackgroundDrawable(new BitmapDrawable(bg));

    }
public void DeleteData() {
    btnDeleteData.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Integer deletedData = myDB.deleteData();


                }
            }
    );

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
