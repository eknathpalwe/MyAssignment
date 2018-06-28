package com.ek.myassignment.backgroundtask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ek.myassignment.utils.Utility;
import com.ek.myassignment.widgets.ReportLayout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by User on 6/26/2018.
 */

public class DownloadTask extends AsyncTask<String, String, ReportLayout> {

    private Context mContext;
    private IDownLoadCallback iDownLoadCallback;

    public DownloadTask(Context context) {
        mContext = context;
    }

    public IDownLoadCallback getiDownLoadCallback() {
        return iDownLoadCallback;
    }

    public void setiDownLoadCallback(IDownLoadCallback iDownLoadCallback) {
        this.iDownLoadCallback = iDownLoadCallback;
    }

    @Override
    protected ReportLayout doInBackground(String... fileUrl) {
        int count;
        try {
            URL url = new URL(fileUrl[0]);
            String outputFilePath = fileUrl[1];
            File file = new File(outputFilePath);
            if (file == null || !file.exists()) {
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(outputFilePath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            }

            //read file and parse
            String fileContents = Utility.getStringFromFile(outputFilePath);

            String[] headerArr = null;
            String[] contentArr = null;

            String actualReportArr[] = fileContents.split("\n\n")[1].split("\n");
            headerArr = actualReportArr[0].split(" +");
            contentArr = Arrays.copyOfRange(actualReportArr, 1, actualReportArr.length -1);
            ReportLayout reportLayout = new ReportLayout(mContext);
            reportLayout.addTableValues(contentArr, headerArr);

            return reportLayout;

        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(ReportLayout reportLayout) {
        super.onPostExecute(reportLayout);
        if (iDownLoadCallback != null) {
            iDownLoadCallback.onDownloadComplete(reportLayout);
        }
    }

    public interface IDownLoadCallback {
        void onDownloadComplete(ReportLayout reportLayout);
    }
}
