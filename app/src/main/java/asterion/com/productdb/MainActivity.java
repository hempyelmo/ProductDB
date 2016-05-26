package asterion.com.productdb;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    List<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl = (TableLayout) findViewById(R.id.maintable);

        String dbPath = getFilesDir().getPath();
        dbPath = dbPath.substring(0, dbPath.lastIndexOf("/")) + "/databases/";

        mProductDB = new MySQLiteHelper(this,"ProductDB");
        mProductDB.open(dbPath);
        /*
        mProductDB.create();

        mProductDB.addProduct(new Product(1,"088754", "064642078728","JAMIESON MULTI COMPL.VIT.ADLT","1 X 90 CAPL",1,1,7,false));
        mProductDB.addProduct(new Product(2,"088894","064642078742","JAMIESON MULTI COMPL.VIT.50+","1 X 90 CAPL",1,1,7,false));
        mProductDB.addProduct(new Product(3,"088803","064642078759","JAMIESON MULTI COMPL.VIT.CROQ.","1 X 60 COMP",1,1,7,false));

        mProductDB.insertProduct(
               new Product(2,"088892","064642078735","JAMIESON MULTI COMPL.VIT.MAX FORCE","1 X 90 CAPL",1,1,7,false));

        mProductDB.updateProduct("064642078759",MySQLiteHelper.KEY_NBFACING,"3");

        Log.d("MainActivity.onCreate","Nb of products = " + mProductDB.getNbProducts());
*/
        nbPickLoc = (NumberPicker) findViewById(R.id.numberPicker);
        assert nbPickLoc != null;
        nbPickLoc.setMinValue(1);
        nbPickLoc.setMaxValue(mProductDB.getNbProducts());

        mProducts = new LinkedList<>();

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
        columns.add("Position");
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

        mProducts.clear();

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

                int pos = product.getPos();

                TextView txtvId = new TextView(this);
                TextView txtvPos = new TextView(this);
                TextView txtvMcKId = new TextView(this);
                TextView txtvUpc = new TextView(this);
                TextView txtvDesc = new TextView(this);
                TextView txtvFormat = new TextView(this);
                TextView txtvNbFacing = new TextView(this);

                txtvId.setText(String.valueOf(mProductDB.getId(pos)));
                txtvPos.setText(String.valueOf(pos));
                txtvMcKId.setText(product.getIdNb());
                txtvUpc.setText(product.getUpc());
                txtvDesc.setText(product.getDesc());
                txtvFormat.setText(product.getFormat());
                txtvNbFacing.setText(String.valueOf(product.getNbFacing()));

                row.addView(txtvId);
                row.addView(txtvPos);
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

        mProducts.clear();

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

    public void sort(View v) {

        String direction = "ASC";
        String column;

        switch(v.getId()) {
            case R.id.colId:
                column = MySQLiteHelper.KEY_ID;
                break;
            case R.id.colPos:
                column = MySQLiteHelper.KEY_POS;
                break;
            case R.id.colMcKesson:
                column = MySQLiteHelper.KEY_IDNB;
                break;
            case R.id.colUPC:
                column = MySQLiteHelper.KEY_UPC;
                break;
            case R.id.colDesc:
                column = MySQLiteHelper.KEY_DESC;
                break;
            case R.id.colFormat:
                column = MySQLiteHelper.KEY_FORMAT;
                break;
            case R.id.colNbFacing:
                column = MySQLiteHelper.KEY_NBFACING;
                break;
            default:
                throw new RuntimeException("Unknow button ID");
        }

        mProductDB.sort(column,direction);

        Log.d("sort", column + " " + direction);
    }

}
