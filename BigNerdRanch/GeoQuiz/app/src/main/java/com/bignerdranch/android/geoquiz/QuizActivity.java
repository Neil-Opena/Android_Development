package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWERED = "answered";
    private static final String KEY_CORRECT = "correct";
    private static final String KEY_LIST = "list";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private ArrayList<Question> mQuestionArrayList;

    private int mCurrentIndex = 0;
    private int mAnsweredQuestions = 0;
    private int mCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnsweredQuestions = savedInstanceState.getInt(KEY_ANSWERED, 0);
            mCorrect = savedInstanceState.getInt(KEY_CORRECT, 0);
            mQuestionArrayList = (ArrayList<Question>) savedInstanceState.getSerializable(KEY_LIST);
        }else{
            mQuestionArrayList = new ArrayList<>();
            mQuestionArrayList.add(new Question(R.string.question_australia, true, false));
            mQuestionArrayList.add(new Question(R.string.question_oceans, true, false));
            mQuestionArrayList.add(new Question(R.string.question_mideast, false, false));
            mQuestionArrayList.add(new Question(R.string.question_africa, false, false));
            mQuestionArrayList.add(new Question(R.string.question_americas, true, false));
            mQuestionArrayList.add(new Question(R.string.question_asia, true, false));
        }


        mQuestionTextView = (TextView) findViewById((R.id.question_text_view));
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getNextQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        if(mQuestionArrayList.get(mCurrentIndex).isCompleted()){
            disableButtons();
        }else{
            enableButtons();
        }

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getNextQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrevQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //start cheat activity
                boolean answerIsTrue = mQuestionArrayList.get(mCurrentIndex).isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivity(intent);
            }
        });

        //initialize the first question
        updateQuestion();
    }

    private void disableButtons(){
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void enableButtons(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void getPrevQuestion(){
        mCurrentIndex--;
        if(mCurrentIndex < 0) mCurrentIndex = mQuestionArrayList.size() - 1;
        updateQuestion();
    }

    private void getNextQuestion(){
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionArrayList.size();
        updateQuestion();
    }

    private void updateQuestion(){
        Question curr = mQuestionArrayList.get(mCurrentIndex);
        int question = curr.getTextResId();
        mQuestionTextView.setText(question);
        if(curr.isCompleted()){
            disableButtons();
        }else{
            enableButtons();
        }
    }

    private void checkAnswer(boolean userPressedTrue){
        Question curr = mQuestionArrayList.get(mCurrentIndex);
        boolean answerIsTrue = curr.isAnswerTrue();

        int messageResId;

        if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
            mCorrect++;
        }else{
            messageResId = R.string.incorrect_toast;
        }

        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, toast.getXOffset(),toast.getYOffset());
        toast.show();

        curr.setCompleted(true);
        mAnsweredQuestions++;
        disableButtons();
        checkAllCompleted();
    }

    private void checkAllCompleted(){
        if(mAnsweredQuestions == mQuestionArrayList.size()){
            double percentage = ((double) mCorrect) / mQuestionArrayList.size() * 100;
            Toast toast = Toast.makeText(this, "Grade: " + String.format("%.2f",percentage) + "%", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, toast.getXOffset(),toast.getYOffset());
            toast.show();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_ANSWERED, mAnsweredQuestions);
        savedInstanceState.putInt(KEY_CORRECT, mCorrect);
        savedInstanceState.putSerializable(KEY_LIST, mQuestionArrayList);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
