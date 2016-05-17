package asterion.com.productdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MySQLiteHelper mProductDB;
    NumberPicker bou;
    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProductDB = new MySQLiteHelper(this);

        tl = (TableLayout) findViewById(R.id.maintable);

        /**
         * CRUD Operations
         * */
        mProductDB.addProduct(new Product("088754", "064642078728","JAMIESON MULTI COMPL.VIT.ADLT","1 X 90 CAPL",1,1,7,false));
        mProductDB.addProduct(new Product("088894","064642078742","JAMIESON MULTI COMPL.VIT.50+","1 X 90 CAPL",1,1,7,false));
        mProductDB.addProduct(new Product("088803","064642078759","JAMIESON MULTI COMPL.VIT.CROQ.","1 X 60 COMP",1,1,7,false));

        mProductDB.insertProduct(
               new Product("088892","064642078735","JAMIESON MULTI COMPL.VIT.MAX FORCE","1 X 90 CAPL",1,1,7,false),2);

        bou = (NumberPicker) findViewById(R.id.numberPicker);

        assert bou != null;
        bou.setMinValue(1);
        bou.setMaxValue(mProductDB.getNbProducts());

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        assert spinner != null;
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> columns = new ArrayList<>();
        columns.add("Loc");
        columns.add("McKesson Id");
        columns.add("UPC");
        columns.add("Description");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, columns);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // Spinner element
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        // Spinner click listener
        assert spinner2 != null;
        spinner2.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> operators = new ArrayList<>();
        operators.add("=");
        operators.add("<");
        operators.add(">");
        operators.add("contains");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, operators);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

   public void showAllProducts(View v) {
        List<Product> products = mProductDB.getAllProducts();

       Log.d("showAllProducts","" + tl.getChildCount());

       int childCount = tl.getChildCount();
       if (childCount > 1)
            tl.removeViews(1, childCount - 1);

       int i = 1;
       for(Product product : products) {
           TableRow row = new TableRow(this);

           TextView txtvId = new TextView(this);
           TextView txtvLoc = new TextView(this);
           TextView txtvMcKId = new TextView(this);
           TextView txtvUpc = new TextView(this);
           TextView txtvDesc = new TextView(this);
           TextView txtvFormat = new TextView(this);
           TextView txtvNbFacing = new TextView(this);

           txtvId.setText(String.valueOf(i));
           txtvLoc.setText(String.valueOf(mProductDB.getLoc(i)));
           txtvMcKId.setText(product.getIdNb());
           txtvUpc.setText(product.getUpc());
           txtvDesc.setText(product.getDesc());
           txtvFormat.setText(product.getFormat());
           txtvNbFacing.setText(String.valueOf(product.getNbFacing()));

           row.addView(txtvId);
           row.addView(txtvLoc);
           row.addView(txtvMcKId);
           row.addView(txtvUpc);
           row.addView(txtvDesc);
           row.addView(txtvFormat);
           row.addView(txtvNbFacing);

           tl.addView(row,i);

           i++;
       }
    }

    public void getProduct(View v) {

        int loc = bou.getValue();

        Product product = mProductDB.getProduct(loc);

        if(product != null) {

            if (tl.getChildCount() > 1)
                tl.removeViewAt(1);

            TableRow row = new TableRow(this);

            TextView txtvId = new TextView(this);
            TextView txtvLoc = new TextView(this);
            TextView txtvMcKId = new TextView(this);
            TextView txtvUpc = new TextView(this);
            TextView txtvDesc = new TextView(this);
            TextView txtvFormat = new TextView(this);
            TextView txtvNbFacing = new TextView(this);

            txtvId.setText(String.valueOf(mProductDB.getId(loc)));
            txtvLoc.setText(String.valueOf(loc));
            txtvMcKId.setText(product.getIdNb());
            txtvUpc.setText(product.getUpc());
            txtvDesc.setText(product.getDesc());
            txtvFormat.setText(product.getFormat());
            txtvNbFacing.setText(String.valueOf(product.getNbFacing()));

            row.addView(txtvId);
            row.addView(txtvLoc);
            row.addView(txtvMcKId);
            row.addView(txtvUpc);
            row.addView(txtvDesc);
            row.addView(txtvFormat);
            row.addView(txtvNbFacing);

            tl.addView(row, 1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
