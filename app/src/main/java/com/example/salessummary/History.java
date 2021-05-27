package com.example.salessummary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class History extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    LayoutInflater layoutInflater;
    LinearLayout container;
    LinearLayout row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        container  = findViewById(R.id.container);
        layoutInflater = LayoutInflater.from(this);

        databaseHelper = new DatabaseHelper(this);
        displayRecords();




    }

    void displayRecords(){
        final JSONObject object = new JSONObject();
        Cursor res =  databaseHelper.getData();

        while (res.moveToNext()){
            String data = res.getString(res.getColumnIndex("data"));
            String mTitle = res.getString(res.getColumnIndex("title"));
            String dateCreated =  res.getString(res.getColumnIndex("date_created"));
            String mTotal =  res.getString(res.getColumnIndex("total"));
            final int id =  res.getInt(res.getColumnIndex("id"));

            View view = layoutInflater.inflate(R.layout.display_layout,container,false);
            TextView title = view.findViewById(R.id.title);
            TextView date = view.findViewById(R.id.date);
            TextView total = view.findViewById(R.id.total);
            row = view.findViewById(R.id.row);

            title.setText(mTitle);
            date.setText(dateCreated);
            total.setText("Total: "+mTotal);
            try{
                object.put("total",mTotal);
                object.put("title",mTitle);
                object.put("data",data);

            }catch(Exception e){
                System.out.println("Json Exeption");

            }

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Json Exeption");
                    Intent intent = new Intent(History.this,Edit.class);
                    intent.putExtra("object",String.valueOf(object));
                    intent.putExtra("rowId",String.valueOf(id));
                    startActivity(intent);



                }
            });

            container.addView(view);






        }
    }

    public void deleteRow(View view) {
    }
}
