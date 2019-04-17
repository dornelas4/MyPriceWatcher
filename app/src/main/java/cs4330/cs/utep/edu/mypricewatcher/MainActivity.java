package cs4330.cs.utep.edu.mypricewatcher;
/**
 * @author Daniel Ornelas
 * @version 1.0
 * MainActivity serves as an entry point for the PriceWatcher app.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button trackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackBtn = findViewById(R.id.track_btn);
        trackBtn.setOnClickListener(this::trackButtonClicked);
    }

    /**
     * This method starts the ItemTrackerActivity
     * @see ItemViewerActivity The activity invoked
     * @param view The current view
     */
    private void trackButtonClicked(View view){
        startActivity(new Intent(this,ItemViewerActivity.class));
    }
}
