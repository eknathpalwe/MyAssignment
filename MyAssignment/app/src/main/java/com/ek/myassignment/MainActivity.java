package com.ek.myassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnClimates;
    private Button btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.title_main);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_climates:
                startActivity(new Intent(this, ClimatesActivity.class));
                break;

            case R.id.btn_map:
                startActivity(new Intent(this, MapActivity.class));
                break;
        }
    }

    /**
     * iniliaze  view
     */
    private void initView() {
        btnMaps = findViewById(R.id.btn_map);
        btnClimates = findViewById(R.id.btn_climates);

        btnClimates.setOnClickListener(this);
        btnMaps.setOnClickListener(this);
    }
}
