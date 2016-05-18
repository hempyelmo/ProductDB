package asterion.com.productdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MySQLiteHelper mProductDB;
    NumberPicker nbPickLoc;
    TableLayout tl;
    String operator;
    String column;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProductDB = new MySQLiteHelper(this);

        tl = (TableLayout) findViewById(R.id.maintable);

        mProductDB.addProduct(new Product("088754", "064642078728","JAMIESON MULTI COMPL.VIT.ADLT","1 X 90 CAPL",1,1,7,false));
        mProductDB.addProduct(new Product("088894","064642078742","JAMIESON MULTI COMPL.VIT.50+","1 X 90 CAPL",1,1,7,false));
        mProductDB.addProduct(new Product("088803","064642078759","JAMIESON MULTI COMPL.VIT.CROQ.","1 X 60 COMP",1,1,7,false));

        mProductDB.insertProduct(
               new Product("088892","064642078735","JAMIESON MULTI COMPL.VIT.MAX FORCE","1 X 90 CAPL",1,1,7,false),2);

        nbPickLoc = (NumberPicker) findViewById(R.id.numberPicker);

        nbPickLoc.setDescendantFocusability(NumberPicker.FOCUS_AFTER_DESCENDANTS);

        assert nbPickLoc != null;
        nbPickLoc.setMinValue(1);
        nbPickLoc.setMaxValue(mProductDB.getNbProducts());

        showSpinner();
    }

    private void showSpinner() {
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

        spinner2.setAdapter(dataAdapter2);
    }

   public void showAllProducts(View v) {
        List<Product> products = mProductDB.getAllProducts();

        refreshTable(products);
    }

    public void getProduct(View v) {

        int loc = nbPickLoc.getValue();

        Product product = mProductDB.getProduct(loc);
        List<Product> products = new LinkedList<>();
        products.add(product);

        refreshTable(products);
    }

    private void refreshTable(List<Product> products) {

        int childCount = tl.getChildCount();
        if (childCount > 1)
            tl.removeViews(1, childCount - 1);

        if (products.get(0) != null) {
            int i = 1;

            for (Product product : products) {
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

                tl.addView(row, i);

                i++;
            }
        }
    }

    public void findProduct(View v) {
      EditText etxtValues = (EditText) findViewById(R.id.editText);

        String value = etxtValues.getText().toString();

        List<Product> products = mProductDB.findProduct(column,operator,value);
        refreshTable(products);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinner) {
            column = parent.getItemAtPosition(position).toString();
        } else if(spinner.getId() == R.id.spinner2) {
            operator = parent.getItemAtPosition(position).toString();
        }

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
