package com.example.todolist;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotesTask extends Thread {
    private static final String TAG = "NotesTask";
    private String note = "/todos";
    private String baseUrl = BuildConfig.SERVER_URL + note;

    public ArrayList<Note> fetchNotes() {
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);

            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");

            conn.setRequestMethod("GET");
            conn.setDoOutput(false); // true: POST, false: GET
            conn.setDoInput(true);

            BufferedReader bufferedReader;
            if(conn.getResponseCode() == conn.HTTP_OK){
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                //connection error
                Log.d(TAG, "Fetch Error...");
                return null;
            }

            Log.d(TAG, "Buffer read success");

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            String result = stringBuilder.toString();

            Log.d(TAG, "String read success");

            conn.disconnect();

            Log.d(TAG, "Http disconnect success");

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            ArrayList<Note> notes = new ArrayList<>();
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Log.d(TAG, "id: "+item.getInt("id")+" todo: "+item.getString("todo")+" completed: "+item.getBoolean("completed"));
                Note note = new Note(item.getInt("id"), item.getString("todo"), item.getBoolean("completed"));
                notes.add(note);
            }
            return notes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public void fetchNote(int id) {
//        try {
//            URL url = new URL(baseUrl + "/" + id);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void createTodo(String todo) {
        Log.d(TAG, "createTodo with: "+todo);
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // true: POST, false: GET
//            conn.setDoInput(true);

            String jsonBody = "{ \"todo\": \"" + todo + "\"}";
            conn.getOutputStream().write(jsonBody.getBytes(StandardCharsets.UTF_8));

            if(conn.getResponseCode() == conn.HTTP_CREATED){
                Log.d(TAG, "Note Created!!!");
            } else {
                //connection error
                Log.d(TAG, "Create Error...");
                return;
            }

            conn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTodo(int id, boolean completed) {
        /**
         * HttpURLConnection은 오래된 클래스
         * https://developer.android.com/reference/java/net/HttpURLConnection#http-methods
         */

        Log.d(TAG, "id: "+id+" completed: "+completed);

        try{
            URL url = new URL(baseUrl + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);

            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // true: POST, false: GET
//            conn.setDoInput(true);

            String jsonBody = "{ \"completed\": " + completed + "}";
            conn.getOutputStream().write(jsonBody.getBytes(StandardCharsets.UTF_8));

            Log.d(TAG, "Method: "+conn.getRequestMethod()+" code: "+conn.getResponseCode());
            if(conn.getResponseCode() == conn.HTTP_OK){
                Log.d(TAG, "Note updated!!!");
            } else {
                //connection error
                Log.d(TAG, "Create Error...");
                return;
            }
            conn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteTodo(int id) {
        try {
            URL url = new URL(baseUrl + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);

            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(false); // true: POST, false: GET - body에 데이터를 담는경우!
            conn.setDoInput(true);

            conn.setRequestMethod("DELETE");

            if(conn.getResponseCode() == conn.HTTP_OK){
                Log.d(TAG, "Note Deleted!!!");
            } else {
                //connection error
                Log.d(TAG, "Delete Error...");
                return;
            }
            conn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}