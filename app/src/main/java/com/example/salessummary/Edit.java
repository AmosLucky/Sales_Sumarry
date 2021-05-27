package com.example.salessummary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class Edit extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    LinearLayout container;
    LayoutInflater layoutInflater;
    FloatingActionButton fab;
    LinearLayout instruction;
    TextView total;
    int index = 0;
    Spinner spinner;
    TextView domination;
    final ArrayList<Double> sumArray = new ArrayList<>();
    String[] currency =  {"ALL","AFN","ARS","AWG","AUD","AZN","BSD","BBD","BDT","BYR","BZD","BMD","BOB","BAM","BWP","BGN","BRL","BND","KHR","CAD","KYD","CLP","CNY","COP","CRC","HRK","CUP","CZK","DKK","DOP","XCD","EGP","SVC","EEK","EUR","FKP","FJD","GHC","GIP","GTQ","GGP","GYD","HNL","HKD","HUF","ISK","INR","IDR","IRR","IMP","ILS","JMD","JPY","JEP","KZT","KPW","KRW","KGS","LAK","LVL","LBP","LRD","LTL","MKD","MYR","MUR","MXN","MNT","MZN","NAD","NPR","ANG","NZD","NIO","NGN","NOK","OMR","PKR","PAB","PYG","PEN","PHP","PLN","QAR","RON","RUB","SHP","SAR","RSD","SCR","SGD","SBD","SOS","ZAR","LKR","SEK","CHF","SRD","SYP","TWD","THB","TTD","TRY","TRL","TVD","UAH","GBP","USD","UYU","UZS","VEF","VND","YER","ZWD"};

    EditText mainDescription;
    EditText mainQty;
    EditText mainSummary;
    EditText mainAmount;
    EditText title;
    ScrollView scrollView;
    JSONArray jsonArray = new JSONArray();
    CardView cardView;
    JSONObject jsonObjectFromEdit;
    String object;
    String rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardView = findViewById(R.id.card);
        cardView.setVisibility(View.GONE);

        /////////////////////////////
         object = getIntent().getStringExtra("object");
        rowId = getIntent().getStringExtra("rowId");
        System.out.println(object);


        /////////////////////////

        mainDescription = findViewById(R.id.description);
        mainQty = findViewById(R.id.qty);
        mainSummary = findViewById(R.id.sumary);
        title = findViewById(R.id.title);
        mainAmount = findViewById(R.id.amount);
        scrollView = findViewById(R.id.scrollview);

        instruction = findViewById(R.id.instruction);


        ///////////////////////MIAN WIDGET/////////////////////////
        //firstWidget(mainDescription,mainQty,mainAmount,mainSummary);

        databaseHelper = new DatabaseHelper(this);


        ///////////////////////////////////////////////////////////////


        // spinner = findViewById(R.id.spinner);
        domination = findViewById(R.id.dinomiation);




        container = findViewById(R.id.container);
        fab = findViewById(R.id.fab);
        total = findViewById(R.id.total);
        layoutInflater = LayoutInflater.from(this);
//        ArrayList options = new ArrayList();
//        options.add("N");
//        options.add("$");
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,currency);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String selectedName = adapterView.getItemAtPosition(i).toString();
//                domination.setText(selectedName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        spinner.setAdapter(arrayAdapter);


        ///////////////////
        showWidget();

        //////////////

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                instruction.setVisibility(View.GONE);
                final JSONObject jsonObject = new  JSONObject();
                jsonArray.put(jsonObject);
                scrollView.scrollTo(0,scrollView.getHeight());
                final int mIndex = index;
                final double[] qty1 = {0};
                final double[] amount1 = {0};


                sumArray.add(mIndex,0.0);
                Log.i("sssssssssssss", String.valueOf(sumArray));

                view = layoutInflater.inflate(R.layout.item_row,container,false);
                EditText amount = view.findViewById(R.id.amount);
                EditText qty = view.findViewById(R.id.qty);
                final EditText summary = view.findViewById(R.id.total);
                EditText description = view.findViewById(R.id.description);
                ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);

                final View finalView = view;
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(finalView);
                        sumArray.set(mIndex,0.0);
                        jsonObject.remove("description");
                        jsonObject.remove("amount");
                        jsonObject.remove("total");
                        jsonObject.remove("quantity");

                        refresh();




                        Toast.makeText(Edit.this,"Removed",Toast.LENGTH_LONG).show();

                    }
                });

                description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            jsonObject.put("description",String.valueOf(editable));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });



                amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(!editable.toString().isEmpty()) {

                            amount1[0] = Double.parseDouble(String.valueOf(editable));

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);

                        }else{
                            amount1[0] = 0;

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);
                        }
                    }
                });

                qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        if(!editable.toString().isEmpty()) {

                            qty1[0] = Double.parseDouble(String.valueOf(editable));

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);

                        }else{
                            qty1[0] = 0;

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);
                        }


                    }
                });


                container.addView(view);

                index++;

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });




            }
        });


    }


    void firstWidget(EditText mainDescription, EditText mainQty, final EditText mainAmount, final EditText mainSummary ){
        final JSONObject jsonObject = new JSONObject();
        jsonArray.put(jsonObject);
        final int mIndex = 0;
        final double[] qty1 = {0};
        final double[] amount1 = {0};
        sumArray.add(mIndex,0.0);

        mainDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    jsonObject.put("description",String.valueOf(editable));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        mainAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()) {

                    amount1[0] = Double.parseDouble(String.valueOf(editable));

                    addToSum(mIndex,qty1[0],amount1[0],mainSummary,jsonObject);

                }else{
                    amount1[0] = 0;

                    addToSum(mIndex,qty1[0],amount1[0],mainSummary,jsonObject);
                }

            }
        });

        mainQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {



                if(!editable.toString().isEmpty()) {

                    qty1[0] = Double.parseDouble(String.valueOf(editable));

                    addToSum(mIndex,qty1[0],amount1[0],mainSummary,jsonObject);

                }else{
                    qty1[0] = 0;

                    addToSum(mIndex,qty1[0],amount1[0],mainSummary,jsonObject);
                }

            }
        });

    }


    void addToSum(int mIndex, double qty, double amount,EditText summaryEditext,JSONObject jsonObject){
        try {
            double sum = amount * qty;
            sumArray.set(mIndex, sum);
            String theSum = currencyFormatter(String.format("%.2f", sum));
            summaryEditext.setText(String.valueOf(theSum));
            Log.i("sssssssssssss", String.valueOf(sumArray));
            double totalSum = 0;
            for (int y = 0; y < sumArray.size(); y++) {
                totalSum += sumArray.get(y);
                // NumberFormat formatter = new DecimalFormat("#,###");
                String totalSumString = currencyFormatter(String.format("%.2f", totalSum));

                total.setText(totalSumString);
            }


            jsonObject.put("amount",amount);
            jsonObject.put("quantity",qty);
            jsonObject.put("total",sum);
            Log.i("ppppp",String.valueOf(jsonObject));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String currencyFormatter(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }


    public  void refresh(){
        double totalSum = 0;
        for (int y = 0; y < sumArray.size(); y++) {
            totalSum += sumArray.get(y);
            // NumberFormat formatter = new DecimalFormat("#,###");
            String totalSumString = currencyFormatter(String.format("%.2f", totalSum));

            total.setText(totalSumString);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Save(View view) {
        // Toast.makeText(MainActivity.this,"Clicked",Toast.LENGTH_LONG).show();
        Log.i("gggggggggg",String.valueOf(jsonArray));
        JSONArray jsonToSave = new JSONArray();
        boolean isToSave = false;
        try {
            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String description =   jsonObject.optString("description");
                String amount =   jsonObject.optString("amount");
                String quantity =   jsonObject.optString("quantity");
                String total =   jsonObject.optString("total");
                if(jsonObject.length() != 0){
                    jsonToSave.put(jsonObject);


                }


            }
            ////////////////////////////////////////////////
            if(jsonToSave.length() != 0){
                if(!title.getText().toString().trim().isEmpty()){

                    //////////////////SAVE//////
                    Log.i("ooop", String.valueOf(jsonToSave));
                    String data = String.valueOf(jsonToSave);
                    String mTitle = title.getText().toString();
                    String dateStr = "04/05/2010";

                    SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    //  Toast.makeText(MainActivity.this,newDateStr,Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("title",mTitle);
                    contentValues.put("data",data);
                    contentValues.put("date_created",formattedDate);
                    contentValues.put("total", total.getText().toString());
                    boolean isSaved =  databaseHelper.updateDate(rowId,contentValues);
                    if(isSaved){
                        Toast.makeText(Edit.this,"Saved",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Edit.this,History.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(Edit.this,"Failed To save",Toast.LENGTH_LONG).show();

                    }


                }else{
                    title.setError("Please fill");
                    title.requestFocus();
                    Toast.makeText(Edit.this,"Please fill the title",Toast.LENGTH_LONG).show();
                }
            }else{

                Toast.makeText(Edit.this,"Please type the values to save",Toast.LENGTH_LONG).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void RemoveViews(View view) {
        //    MainActivity.this.recreate();
//        finish();
//        startActivity(getIntent());


        refreshActivity();


//       JSONArray newOne = new JSONArray();
//       jsonArray = newOne;
//
//
//        sumArray.clear();
//        sumArray.add(0,0.0);
//        index = 1;
//        refresh();
//        mainSummary.setText("");
//        mainQty.setText("");
//        mainAmount.setText("");
//        mainDescription.setText("");
//        total.setText("0");
//        container.removeAllViews();
    }

    void  refreshActivity(){
//        overridePendingTransition(0, 0);
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(getIntent());
//        overridePendingTransition(0, 0);

        Intent intent = new Intent(Edit.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void History(View view) {
        Intent intent = new Intent(Edit.this,History.class);
        startActivity(intent);
    }

    void showWidget(){
        try{
            Log.i("ooooooooooooooooo",object);

            jsonObjectFromEdit = new JSONObject(object);
            String iTitle = jsonObjectFromEdit.optString("title");



            String iTotal = jsonObjectFromEdit.optString("total");
            Log.i("ooooooooooooooooo",iTitle+" "+iTotal);
            total.setText(iTotal);
            title.setText(iTitle);


            JSONArray array = new JSONArray(jsonObjectFromEdit.optString("data"));
            for (int i = 0; i < array.length(); i++){

                JSONObject iObject =  array.getJSONObject(i);
                instruction.setVisibility(View.GONE);
                final JSONObject jsonObject = new  JSONObject();
                jsonArray.put(jsonObject);
                scrollView.scrollTo(0,scrollView.getHeight());
                final int mIndex = index;
                final double[] qty1 = {0};
                final double[] amount1 = {0};
                qty1[0] = iObject.optDouble("quantity",0.0);
                amount1[0]  = iObject.optDouble("amount",0.0);
                String idescription = iObject.optString("description","");


                sumArray.add(mIndex,iObject.optDouble("total"));
                Log.i("sssssssssssss", String.valueOf(sumArray));

             View   view = layoutInflater.inflate(R.layout.item_row,container,false);
                EditText amount = view.findViewById(R.id.amount);
                EditText qty = view.findViewById(R.id.qty);
                final EditText summary = view.findViewById(R.id.total);
                EditText description = view.findViewById(R.id.description);
                ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
                qty.setText(String.valueOf( qty1[0]));
                amount.setText(String.valueOf( amount1[0]));
                description.setText(idescription);
                ////////////////
                addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);
                //////////////////

                final View finalView = view;
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(finalView);
                        sumArray.set(mIndex,0.0);
                        jsonObject.remove("description");
                        jsonObject.remove("amount");
                        jsonObject.remove("total");
                        jsonObject.remove("quantity");

                        Intent intent = new Intent(Edit.this,MainActivity.class);
                        startActivity(intent);
                        finish();




                        Toast.makeText(Edit.this,"Removed",Toast.LENGTH_LONG).show();

                    }
                });

                description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            jsonObject.put("description",String.valueOf(editable));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });



                amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(!editable.toString().isEmpty()) {

                            amount1[0] = Double.parseDouble(String.valueOf(editable));

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);

                        }else{
                            amount1[0] = 0;

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);
                        }
                    }
                });

                qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        if(!editable.toString().isEmpty()) {

                            qty1[0] = Double.parseDouble(String.valueOf(editable));

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);

                        }else{
                            qty1[0] = 0;

                            addToSum(mIndex,qty1[0],amount1[0],summary,jsonObject);
                        }


                    }
                });


                container.addView(view);

                index++;

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });





            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}


//package com.example.salessummary;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//
//import org.json.JSONObject;
//
//public class Edit extends AppCompatActivity {
//    JSONObject jsonObject;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit);
//        String object = getIntent().getStringExtra("object");
//        System.out.println(object);
//        jsonObject = new JSONObject(object);
//    }
//
//}
