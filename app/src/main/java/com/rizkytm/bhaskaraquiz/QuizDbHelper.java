package com.rizkytm.bhaskaraquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rizkytm.bhaskaraquiz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BhaskaraQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_PERTANYAAN + " TEXT, " +
                QuestionsTable.COLUMN_PIL1 + " TEXT," +
                QuestionsTable.COLUMN_PIL2 + " TEXT," +
                QuestionsTable.COLUMN_PIL3 + " TEXT," +
                QuestionsTable.COLUMN_PIL4 + " TEXT," +
                QuestionsTable.COLUMN_NO_JAWABAN + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT," +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Bahasa Sunda");
        addCategory(c1);
        Category c2 = new Category("Aksara Sunda");
        addCategory(c2);
        Category c3 = new Category("Terjemahan");
        addCategory(c3);
    }

    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("Abdi nuju .... di ruang makan",
                "Neda", "Tuang", "Nyatu", "Maca", 1,
                Question.DIFFICULTY_EASY, Category.BAHASA_SUNDA);
        addQuestion(q1);
        Question q2 = new Question("Bapa nuju .... di kolam renang",
                "Lumpat", "Maca", "Ngojay", "Sare", 3,
                Question.DIFFICULTY_EASY, Category.BAHASA_SUNDA);
        addQuestion(q2);
        Question q3 = new Question("Terjemahkeun kalimah ieu\nᮃᮘ᮪ᮓᮤ ᮅᮛᮀ ᮞᮥᮔ᮪ᮓ",
                "Abdi Urang Bandung", "Abdi Urang Jakarta", "Abdi Urang Jawa", "Abdi Urang Sunda", 4,
                Question.DIFFICULTY_MEDIUM, Category.AKSARA_SUNDA);
        addQuestion(q3);
        Question q4 = new Question("Eusian titik-titik dina kalimah ieu\nᮃᮘ᮪ᮓᮤ ... ᮞᮥᮔ᮪ᮓ",
                "ᮅᮒ", "ᮅᮛᮀ", "ᮅᮛ", "ᮅᮑ", 2,
                Question.DIFFICULTY_MEDIUM, Category.AKSARA_SUNDA);
        addQuestion(q4);
        Question q5 = new Question("Terjemahkeun kalimah ieu ka Bahasa Indonesia\nAbdi nuju maca",
                "Saya sedang membaca", "Saya sedang tidur", "Saya sedang berlari", "Saya sedang makan", 1,
                Question.DIFFICULTY_HARD, Category.TERJEMAHAN);
        addQuestion(q5);
        Question q6 = new Question("Terjemahkeun kalimah ieu ka Basa Sunda\nBapa nuju tuang",
                "Bapak sedang minum", "Bapak sedang makan", "Bapak sedang tidur", "Bapak sedang mandi", 2,
                Question.DIFFICULTY_HARD, Category.TERJEMAHAN);
        addQuestion(q6);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_PERTANYAAN, question.getPertanyaan());
        cv.put(QuestionsTable.COLUMN_PIL1, question.getPil1());
        cv.put(QuestionsTable.COLUMN_PIL2, question.getPil2());
        cv.put(QuestionsTable.COLUMN_PIL3, question.getPil3());
        cv.put(QuestionsTable.COLUMN_PIL4, question.getPil4());
        cv.put(QuestionsTable.COLUMN_NO_JAWABAN, question.getNoJawaban());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM  " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setPertanyaan(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PERTANYAAN)));
                question.setPil1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL1)));
                question.setPil2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL2)));
                question.setPil3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL3)));
                question.setPil4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL4)));
                question.setNoJawaban(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_NO_JAWABAN)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(QuestionsTable.TABLE_NAME, null, selection,
                selectionArgs, null, null, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setPertanyaan(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PERTANYAAN)));
                question.setPil1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL1)));
                question.setPil2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL2)));
                question.setPil3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL3)));
                question.setPil4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_PIL4)));
                question.setNoJawaban(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_NO_JAWABAN)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
