package omfarid.com.legendlocator.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by farid on 1/6/2017.
 */

public class FaridHelpers {
    public static List<String> importSQL(Context context, int resId) {

        BufferedReader reader = null;
        List<String> data = new ArrayList<>();

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getResources().openRawResource(resId)));
            while (reader.ready()) {
                //Log.i("sigpodes", reader.readLine());
                data.add(reader.readLine());
            }
        } catch (IOException e) {
            Log.e("sigpodes", e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("sigpodes", e.toString());
                }
            }
        }

        return data;
    }

    public static String readFile(Context context, int resId) {
        BufferedReader reader = null;
        String data = new String();

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getResources().openRawResource(resId)));
            while (reader.ready()) {
                //Log.i("sigpodes", reader.readLine());
                data += reader.readLine();
            }
        } catch (IOException e) {
            Log.e("sigpodes", e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("sigpodes", e.toString());
                }
            }
        }

        return data;
    }

    public static void setPreference(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("sigpodes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreferenceByKey(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("sigpodes", Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static String joinList(List<String> list) {
        String join = "";
        for(int i=0;i<list.size();i++) {
            join+=("- "+list.get(i)+"\n");
        }
        return join;
    }

    public static void showAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    public static void setLog(String msg) {
        Log.i("sigpodes", msg);
    }
}
