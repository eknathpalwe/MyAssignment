package com.ek.myassignment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ek.myassignment.backgroundtask.DownloadTask;
import com.ek.myassignment.utils.Utility;
import com.ek.myassignment.widgets.ReportLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/26/2018.
 */

public class ClimatesActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerRegion;
    private Spinner spinnerParameter;
    private LinearLayout reportMainLayout;
    private Button btnView;
    private ProgressBar progressBar;

    private DownloadTask downloadTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climates);
        getSupportActionBar().setTitle(R.string.title_Climates);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadTask != null) {
            downloadTask.cancel(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (spinnerRegion.getSelectedItemPosition() == 0
                || spinnerParameter.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        reportMainLayout.removeAllViews();
        btnView.setEnabled(false);

        downloadTask = new DownloadTask(this);
        downloadTask.setiDownLoadCallback(new DownloadTask.IDownLoadCallback() {
            @Override
            public void onDownloadComplete(ReportLayout reportLayout) {
                bindUi(reportLayout);
            }
        });

        downloadTask.execute(getUrlFromSelection(), getFilePathFromSelection());

    }

    /**
     * Bind ui to report view
     * @param reportLayout
     */
    private void bindUi(final ReportLayout reportLayout) {
        reportMainLayout.addView(reportLayout);
        progressBar.setVisibility(View.GONE);
        btnView.setEnabled(true);
    }

    /**
     * Generate local file path from selected spinner item
     * @return file path
     */
    private String getFilePathFromSelection() {
        return Utility.getDirFromSandbox(this).getAbsolutePath() + File.separator
                + spinnerRegion.getSelectedItem().toString() + "_" + spinnerParameter.getSelectedItem().toString() + ".txt";
    }

    /**
     * Generate url from selected spinner item
     * @return string url
     */
    private String getUrlFromSelection() {
        return String.format("https://www.metoffice.gov.uk/pub/data/weather/uk/climate/datasets/%s/date/%s.txt",
                spinnerParameter.getSelectedItem().toString().trim(),
                spinnerRegion.getSelectedItem().toString().trim());
    }

    private void initView() {
        spinnerRegion = findViewById(R.id.spinner_region);
        spinnerParameter = findViewById(R.id.spinner_parameter);
        reportMainLayout = (LinearLayout) findViewById(R.id.layout_report);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnView = findViewById(R.id.btn_view);
        btnView.setOnClickListener(this);
    }
}
