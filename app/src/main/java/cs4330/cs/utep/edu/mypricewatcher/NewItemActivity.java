package cs4330.cs.utep.edu.mypricewatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @Author : Daniel Ornelas
 * Activity to create or edit items
 */
public class NewItemActivity extends AppCompatActivity {
    String itemName;
    String itemURL;
    TextView mode;

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        mode = findViewById(R.id.mode);
        EditText item_name = findViewById(R.id.edit_item_name);
        EditText item_URL = findViewById(R.id.edit_item_url);
        Button submitItem = findViewById(R.id.submit_item);

        itemName = item_name.getText().toString();
        itemURL = item_URL.getText().toString();

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String url = i.getStringExtra("url");

        position = i.getIntExtra("position",-1);
        if(position != -1){
            mode.setText("Edit Item");
        }
        else{
            mode.setText("New Item");
        }
        if(name != null){
            itemName = name;
            item_name.setText(itemName);
            if(url != null){
                itemURL = url;
                item_URL.setText(itemURL);
            }
        }

        item_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        item_URL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemURL = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submitItem.setOnClickListener(this::submitClicked);

        //Handle intent sent from outside app
        String action = getIntent().getAction();
        String type = getIntent().getType();
        if(Intent.ACTION_SEND.equalsIgnoreCase(action)
                && type != null && ("text/plain".equals(type))){
            //get URL from intent and create item
            String outsideURL = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            itemURL = outsideURL;
            item_URL.setText(itemURL);


        }
    }

    /**
     * Submit item clicked
     * @param v view of item
     */
    protected void submitClicked(View v){
        Intent i = new Intent(this,ItemViewerActivity.class);
        i.putExtra("name",itemName);
        i.putExtra("url",itemURL);
        if(position != -1){
            i.putExtra("position",position);
            i.putExtra("edit",true);
        }
        startActivity(i);
    }

}
