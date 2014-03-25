package com.codepath.bootcamp.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

public class ImageFilterActivity extends Activity implements OnItemSelectedListener {
    ImageFilter filter;
    Spinner sizeSpinner, colorSpinner, typeSpinner;
    EditText etSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);

        filter = (ImageFilter) getIntent().getSerializableExtra("filter");

        sizeSpinner = (Spinner) findViewById(R.id.spnSize);
        sizeSpinner.setOnItemSelectedListener(this);
        sizeSpinner.setSelection(filter.getSize() != null ? filter.getSize().ordinal() + 1 : 0);

        colorSpinner = (Spinner) findViewById(R.id.spnColor);
        colorSpinner.setOnItemSelectedListener(this);
        colorSpinner.setSelection(filter.getColor() != null ? filter.getColor().ordinal() + 1 : 0);

        typeSpinner = (Spinner) findViewById(R.id.spnType);
        typeSpinner.setOnItemSelectedListener(this);
        typeSpinner.setSelection(filter.getType() != null ? filter.getType().ordinal() + 1 : 0);

        etSite = (EditText) findViewById(R.id.etSite);
        etSite.setText(filter.getSite());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResetFilter(View v) {
        filter.setSize(null);
        sizeSpinner.setSelection(0);

        filter.setColor(null);
        colorSpinner.setSelection(0);

        filter.setType(null);
        typeSpinner.setSelection(0);
        
        filter.setSite(null);
        etSite.setText(null);
    }

    public void onFinish(View v) {
        // store site string
        filter.setSite(etSite.getText().toString());

        Intent data = new Intent();
        data.putExtra("filter", filter);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Shared listener to handle selections for all three spinner controls 
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
        // image size
        case R.id.spnSize:
            switch (position) {
            case 0:
                filter.setSize(null);
                break;
            case 1:
                filter.setSize(ImageFilter.Size.SMALL);
                break;
            case 2:
                filter.setSize(ImageFilter.Size.MEDIUM);
                break;
            case 3:
                filter.setSize(ImageFilter.Size.LARGE);
                break;
            case 4:
                filter.setSize(ImageFilter.Size.XLARGE);
                break;
            }
            break;
        // image color
        case R.id.spnColor:
            switch (position) {
            case 0:
                filter.setColor(null);
                break;
            case 1:
                filter.setColor(ImageFilter.Color.BLACK);
                break;
            case 2:
                filter.setColor(ImageFilter.Color.BLUE);
                break;
            case 3:
                filter.setColor(ImageFilter.Color.BROWN);
                break;
            case 4:
                filter.setColor(ImageFilter.Color.GRAY);
                break;
            case 5:
                filter.setColor(ImageFilter.Color.GREEN);
                break;
            case 6:
                filter.setColor(ImageFilter.Color.ORANGE);
                break;
            case 7:
                filter.setColor(ImageFilter.Color.PINK);
                break;
            case 8:
                filter.setColor(ImageFilter.Color.PURPLE);
                break;
            case 9:
                filter.setColor(ImageFilter.Color.RED);
                break;
            case 10:
                filter.setColor(ImageFilter.Color.TEAL);
                break;
            case 11:
                filter.setColor(ImageFilter.Color.WHITE);
                break;
            case 12:
                filter.setColor(ImageFilter.Color.YELLOW);
                break;
            }
            break;
        // image type 
        case R.id.spnType:
            switch (position) {
            case 0:
                filter.setType(null);
                break;
            case 1:
                filter.setType(ImageFilter.Type.FACE);
                break;
            case 2:
                filter.setType(ImageFilter.Type.PHOTO);
                break;
            case 3:
                filter.setType(ImageFilter.Type.CLIPART);
                break;
            case 4:
                filter.setType(ImageFilter.Type.LINEART);
                break;
            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }
}
