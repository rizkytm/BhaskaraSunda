package com.rizkytm.bhaskaraquiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    private int id;
    private String pertanyaan;
    private String pil1;
    private String pil2;
    private String pil3;
    private String pil4;
    private int noJawaban;
    private String difficulty;
    private int categoryID;

    public Question() {

    }

    public Question(String pertanyaan, String pil1, String pil2,
                    String pil3, String pil4,
                    int noJawaban, String difficulty, int categoryID) {
        this.pertanyaan = pertanyaan;
        this.pil1 = pil1;
        this.pil2 = pil2;
        this.pil3 = pil3;
        this.pil4 = pil4;
        this.noJawaban = noJawaban;
        this.difficulty = difficulty;
        this.categoryID = categoryID;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        pertanyaan = in.readString();
        pil1 = in.readString();
        pil2 = in.readString();
        pil3 = in.readString();
        pil4 = in.readString();
        noJawaban = in.readInt();
        difficulty = in.readString();
        categoryID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(pertanyaan);
        dest.writeString(pil1);
        dest.writeString(pil2);
        dest.writeString(pil3);
        dest.writeString(pil4);
        dest.writeInt(noJawaban);
        dest.writeString(difficulty);
        dest.writeInt(categoryID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getPil1() {
        return pil1;
    }

    public void setPil1(String pil1) {
        this.pil1 = pil1;
    }

    public String getPil2() {
        return pil2;
    }

    public void setPil2(String pil2) {
        this.pil2 = pil2;
    }

    public String getPil3() {
        return pil3;
    }

    public void setPil3(String pil3) {
        this.pil3 = pil3;
    }

    public String getPil4() {
        return pil4;
    }

    public void setPil4(String pil4) {
        this.pil4 = pil4;
    }

    public int getNoJawaban() {
        return noJawaban;
    }

    public void setNoJawaban(int noJawaban) {
        this.noJawaban = noJawaban;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public static String[] getAllDifficultyLevels() {
        return new String[] {
                DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD
        };
    }
}
