package com.example.todolist;

public class Note {
    int _id;
    String todo;
    boolean completed;

    public Note(int _id, String todo, boolean completed) {
        this._id = _id;
        this.todo = todo;
        this.completed = completed;
    }

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTodo() {
        return this.todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
