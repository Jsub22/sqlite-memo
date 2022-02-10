package com.example.sqliteexample;

public class DiarItem
{
    private int id;             // 게시글의 고유 ID
    private String title;       // 할일 제목
    private String content;     // 작성 내용
    private String writeDate;   // 작성 날짜

    // Alt + Fn lns, cons, OK
    public DiarItem()
    {
    }

    // Alt + Fn lns, getter setter, Ctrl + a, OK
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}
