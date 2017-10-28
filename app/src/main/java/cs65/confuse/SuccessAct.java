package cs65.confuse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
activity that shows up after a successful petting of a cat.
 */

public class SuccessAct extends AppCompatActivity {
    private Button done;
    private Button again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);


        done = findViewById(R.id.Done);

        again = findViewById(R.id.Again);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SuccessAct.this, MainApp.class);
                startActivity(myIntent);
            }
        });

        again.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SuccessAct.this, MapActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
