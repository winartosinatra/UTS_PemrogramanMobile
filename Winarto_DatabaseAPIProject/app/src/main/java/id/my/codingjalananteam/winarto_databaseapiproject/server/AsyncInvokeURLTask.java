package id.my.codingjalananteam.winarto_databaseapiproject.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AsyncInvokeURLTask extends AsyncTask<Void, Void, String>
{
    public String mNoteItWebUrl ="http://192.168.56.1/wphone/";
    private ArrayList<NameValuePair> mParams;
    private OnPostExecuteListener mPostExecuteListener = null;
    private ProgressDialog dialog;
    public boolean showdialog =false;
    public String message ="Proses Data";
    //Harus diganti dengan IP Server yang digunakan
    public String url_server ="https://siakad-altie.000webhostapp.com/xphone//";
    public Context applicationContext;
    public static interface OnPostExecuteListener{
        void onPostExecute(String result);
    }
    public AsyncInvokeURLTask(
            ArrayList<NameValuePair> nameValuePairs,
            OnPostExecuteListener postExecuteListener) throws Exception
    {
        mParams = nameValuePairs;
        mPostExecuteListener = postExecuteListener;
        if (mPostExecuteListener == null)
            throw new Exception("Param cannot be null.");
    }
    @Override
    public void onPreExecute() {
        if (showdialog)
            this.dialog =
                    ProgressDialog.show(applicationContext,message, "Silakan Menunggu...",
                            true);
    }
    @Override
    public String doInBackground(Void... params) {
        String result = "timeout";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url_server+mNoteItWebUrl);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(mParams));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null){
                InputStream inStream = entity.getContent();
                result = convertStreamToString(inStream);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onPostExecute(String result) {
        if (mPostExecuteListener != null){
            try {
                if (showdialog)this.dialog.dismiss();
                mPostExecuteListener.onPostExecute(result);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
