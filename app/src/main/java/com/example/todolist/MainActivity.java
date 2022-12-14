package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private long backButtonDuration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

        final InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE) ;

        Button saveButton = findViewById(R.id.saveButton); // 저장 버튼
        saveButton.setOnClickListener(new View.OnClickListener() { // 저장 버튼에 클릭 이벤트 설정
            @Override
            public void onClick(View view) {
                saveTodo();
                Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
            }

            private void saveTodo() {
                EditText inputTodo = findViewById(R.id.inputTodo);
                String todo = inputTodo.getText().toString();

                if(TextUtils.isEmpty(inputTodo.getText()) == false) { // edtitext에 값이 있는 경우
                    inputTodo.setText(""); // edittext 초기화
                    if(getApplicationContext().getResources().getConfiguration().hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
                        // 키보드가 열린 경우
                        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키보드 내리기
                    }

                    NotesTask at = new NotesTask();
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            at.createTodo(todo);
                            getSupportFragmentManager().beginTransaction().detach(mainFragment).attach(mainFragment).commit(); // fragment refresh
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "할 일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 뒤로가기 버튼 두번 연달아 누르면 앱 종료
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backButtonDuration + 2000) {
            backButtonDuration = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}