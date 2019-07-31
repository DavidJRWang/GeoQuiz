package com.davidwang.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {
    private static final String TAG = "CheatActivity";
    private static final String KEY_SHOWN = "shown";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.davidwang.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.davidwang.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private boolean mAnswerIsShown = false;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerIsShown = savedInstanceState.getBoolean(KEY_SHOWN, false);
            Intent data = new Intent();
            data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerIsShown);
            setResult(RESULT_OK, data);
        }

        // retrieve from Intent that created the activity
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);

        // set text for mAnswerTextView based on the retrieved answer
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue)
                    mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);

                setAnswerShownResult(true);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_SHOWN, mAnswerIsShown);
    }

    // Creates Intent that can hold a boolean as an extra
    // This boolean is used to store the correct answer to a question
    // This method is used in MainActivity
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    // Called when the 'show answer' button is pressed
    // Sets 'answer was shown' to true and packages that info into Result
    // Result is sent back after CheatActivity ends
    private void setAnswerShownResult(boolean isAnswerShown) {
        mAnswerIsShown = isAnswerShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerIsShown);
        setResult(RESULT_OK, data);
    }

    // Helper method that extracts data from the Result Intent
    // This method is used in MainActivity
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}
