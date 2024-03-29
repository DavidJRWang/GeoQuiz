package com.davidwang.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER = "cheater";
    private static final String KEY_ARRAY = "array";
    private static final int REQUEST_CODE_CHEAT = 0;    // used for launching CheatActivity

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_canada, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private boolean[] mAnsweredQuestions = new boolean[mQuestionBank.length];    // all elements initialized to false

    private int mCurrentIndex = 0;
    private int mScore = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
            mAnsweredQuestions = savedInstanceState.getBooleanArray(KEY_ARRAY).clone();
        }

        // Link member variables to Views
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mNextButton = (Button) findViewById(R.id.next_button);

        // Set up listeners
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                disableButtons();
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                disableButtons();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
        savedInstanceState.putBooleanArray(KEY_ARRAY, mAnsweredQuestions);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // if app gets rotated, data is returned null
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    // Called when the 'next' button is pressed
    // Updates the text in mQuestionTextView with the next question
    //  and enables/disables the true/false buttons as necessary
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if (mAnsweredQuestions[mCurrentIndex] == false) {   // if the question hasn't been answered...
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
        else {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }


    // Called when either the 'true' or the 'false' button is pressed
    // Determines whether or not the user's answer was correct and sends the appropriate toast
    // Sends a 'cheater' toast if the user cheated
    // Lastly, sends a toast displaying the user's score after all questions are answered
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue(); // saves correct answer
        int messageResId = 0;

        // determines which toast to be displayed
        if (mIsCheater)
            messageResId = R.string.judgement_toast;
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mScore++;
            }
            else
                messageResId = R.string.incorrect_toast;
        }

        mAnsweredQuestions[mCurrentIndex] = true;   // set current question to 'answered'
        boolean done = true;                        // default state is done, unless found otherwise
        for (int x = 0; x < mAnsweredQuestions.length; x++) {
            if (mAnsweredQuestions[x] == false)
                done = false;
        }

        // if done, then display user's score
        if (done) {
            CharSequence message = mScore + "/" + mQuestionBank.length + " correct";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        // otherwise, show the normal toast
        else
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    // Helper method
    // Disables 'true' and 'false' buttons on being pressed
    private void disableButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    // For demonstration purposes of the Activity lifecycle and debugging
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
