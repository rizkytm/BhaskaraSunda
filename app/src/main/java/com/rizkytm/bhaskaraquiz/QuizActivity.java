package com.rizkytm.bhaskaraquiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewPertanyaan;
    private TextView textViewSkor;
    private TextView textViewNoPertanyaan;
    private TextView textViewKategori;
    private TextView textViewKesulitan;
    private TextView textViewWaktu;
    private RadioGroup rbGroup;
    private RadioButton pil1;
    private RadioButton pil2;
    private RadioButton pil3;
    private RadioButton pil4;
    private Button pilihJawaban;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewPertanyaan = findViewById(R.id.text_view_question);
        textViewSkor = findViewById(R.id.text_view_score);
        textViewNoPertanyaan = findViewById(R.id.text_view_question_count);
        textViewKategori = findViewById(R.id.text_view_category);
        textViewKesulitan = findViewById(R.id.text_view_difficulty);
        textViewWaktu = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        pil1 = findViewById(R.id.radio_button1);
        pil2 = findViewById(R.id.radio_button2);
        pil3 = findViewById(R.id.radio_button3);
        pil4 = findViewById(R.id.radio_button4);
        pilihJawaban = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = pil1.getTextColors();
        textColorDefaultCd = textViewWaktu.getTextColors();

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);

        textViewKategori.setText("Kategori: " + categoryName);
        textViewKesulitan.setText("Tingkat Kesulitan: " + difficulty);

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        pilihJawaban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (pil1.isChecked() || pil2.isChecked() || pil3.isChecked() || pil4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        pil1.setTextColor(textColorDefaultRb);
        pil2.setTextColor(textColorDefaultRb);
        pil3.setTextColor(textColorDefaultRb);
        pil4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewPertanyaan.setText(currentQuestion.getPertanyaan());
            pil1.setText(currentQuestion.getPil1());
            pil2.setText(currentQuestion.getPil2());
            pil3.setText(currentQuestion.getPil3());
            pil4.setText(currentQuestion.getPil4());

            questionCounter++;
            textViewNoPertanyaan.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            pilihJawaban.setText("Pilih Jawaban");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewWaktu.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewWaktu.setTextColor(Color.RED);
        } else {
            textViewWaktu.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int noJawaban = rbGroup.indexOfChild(rbSelected) + 1;

        if (noJawaban == currentQuestion.getNoJawaban()) {
            score++;
            textViewSkor.setText("Skor: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        pil1.setTextColor(Color.RED);
        pil2.setTextColor(Color.RED);
        pil3.setTextColor(Color.RED);
        pil4.setTextColor(Color.RED);

        switch (currentQuestion.getNoJawaban()) {
            case 1:
                pil1.setTextColor(Color.GREEN);
                textViewPertanyaan.setText("Jawaban 1 Benar");
                break;
            case 2:
                pil2.setTextColor(Color.GREEN);
                textViewPertanyaan.setText("Jawaban 2 Benar");
                break;
            case 3:
                pil3.setTextColor(Color.GREEN);
                textViewPertanyaan.setText("Jawaban 3 Benar");
                break;
            case 4:
                pil4.setTextColor(Color.GREEN);
                textViewPertanyaan.setText("Jawaban 4 Benar");
                break;
        }

        if (questionCounter < questionCountTotal) {
            pilihJawaban.setText("Selanjutnya");
        } else {
            pilihJawaban.setText("Selesai");
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}
