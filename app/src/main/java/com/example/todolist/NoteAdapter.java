package com.example.todolist;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    public static final String TAG = "NoteAdapter";

    ArrayList<Note> items = new ArrayList<>(); // todo items

    /**
     * NoteAdapter 내부에 정의 된 ViewHolder 클래스
     * ViewHolder의 역할을 하기 위해 RecyclerView.ViewHolder를 상속한 static class ViewHolder를 만들어주고
     * 안에 ViewHolder 메서드를 만들고 super메서드를 이용하여 View 생성
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder는 item을 갖고 있으므로 todo_item.xml에서 만들었던
        // LinearLayout, CheckBox, Button 변수를 선언 및 각 id에 맞게 연결, 역할 정의
        LinearLayout layoutTodo;
        CheckBox checkBox;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutTodo = itemView.findViewById(R.id.layoutTodo);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(view -> {
                NotesTask at = new NotesTask();
                Executors.newSingleThreadExecutor().execute(() -> {
                    try{
                        at.updateTodo(checkBox.getId(), checkBox.isChecked());
                        postToastMessage(view.getContext(), "변경되었습니다.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });

            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(view -> {
                NotesTask at = new NotesTask();
                Executors.newSingleThreadExecutor().execute(() -> {
                    try{
                        at.deleteTodo(checkBox.getId());
                        postToastMessage(view.getContext(), "삭제되었습니다.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        public void setItem(Note item){
            Log.d(TAG, item.get_id()+ " "+item.getTodo()+" "+item.getCompleted());
            checkBox.setId(item.get_id());
            checkBox.setText(item.getTodo());
            checkBox.setChecked(item.getCompleted());
        }

        public void setLayout(){
            layoutTodo.setVisibility(View.VISIBLE);
        }

        private void postToastMessage(Context context, final String message) {
            //
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // todo_item.xml 인플레이션
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        // ViewHolder에 item bind
        Note item = items.get(position);
        holder.setItem(item);
        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Note> items) {
        this.items = items;
    }
}
