package com.rizkytm.bhaskaraquiz;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {}

    public static class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_categories";
        public static final String COLUMN_NAME = "name";
    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_PERTANYAAN = "pertanyaan";
        public static final String COLUMN_PIL1 = "pil1";
        public static final String COLUMN_PIL2 = "pil2";
        public static final String COLUMN_PIL3 = "pil3";
        public static final String COLUMN_PIL4 = "pil4";
        public static final String COLUMN_NO_JAWABAN = "noJawaban";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_CATEGORY_ID = "category_id";
    }
}
