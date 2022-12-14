package com.example.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    /**
     * Fragment가 생성될 때 호출되는 메서드
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Fragment가 생성 된 이후 구성될 때 호출되는 메서드 (onCreate 다음으로 호출됨)
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // LayoutInflator로 layout xml을 가져옴
        // inflate 메서드로 fragment_main.xml과 연결
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadNoteListData();
            swipeRefreshLayout.setRefreshing(false);
        });

        // initUI
        initUI(rootView);
        // get Notes
        loadNoteListData();

        return rootView;
    }

    /**
     * layoutManager와 adapter를 이용하여 RecyclerView를 사용하는 역할
     * @param rootView
     */
    private void initUI(ViewGroup rootView){
        // recyclerView 연결
        recyclerView = rootView.findViewById(R.id.recyclerView);

        // linearLayout에 recyclerView를 붙임
        // todo_item들이 세로로 정렬되는 역할
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // adapter 연결
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
    }

    public void loadNoteListData() {
        /**
         * mainThread 사용 방지
         *
         * main thread 에서 바로 네트워킹 작업을 시도하면 에러 발생!
         */
        final NotesTask at = new NotesTask();
        Executors.newSingleThreadExecutor().execute(() -> {
            try{
                ArrayList<Note> notes = at.fetchNotes();
                noteAdapter.setItems(notes);
                noteAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
