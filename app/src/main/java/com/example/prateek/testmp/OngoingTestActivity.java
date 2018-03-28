package com.example.prateek.testmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OngoingTestActivity extends AppCompatActivity {

    TextView textViewQuestion;
    RadioGroup radioGroupOptions;
    RadioButton radioButtonOptionA, radioButtonOptionB, radioButtonOptionC, radioButtonOptionD;
    Button buttonNext, buttonPrevious, buttonMark;

    String test_uid;

    int questionNo = 0;

    ArrayList<TestQuestionDetails> testQuestionDetailsArrayList = new ArrayList<>();

    public FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_test);

        XMLReferences();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        test_uid = intent.getStringExtra("test_uid");
        Log.i("####Value===", test_uid);

        getQuestions();
    }

    private void displayQuestion() {
        if(questionNo<(testQuestionDetailsArrayList.size()-1))
            buttonNext.setText("Next");
        try {
            TestQuestionDetails testQuestionDetails = testQuestionDetailsArrayList.get(questionNo);
            textViewQuestion.append(testQuestionDetails.question);
            radioButtonOptionA.setText("A. " + testQuestionDetails.a);
            radioButtonOptionB.setText("B. " + testQuestionDetails.b);
            radioButtonOptionC.setText("C. " + testQuestionDetails.c);
            radioButtonOptionD.setText("D. " + testQuestionDetails.d);
        }
        catch (Exception e){
            questionNo=0;
            textViewQuestion.setText("");
            displayQuestion();
            e.printStackTrace();}
    }

    private void getQuestions() {


        try {
            mDatabase.child("tests").child(test_uid).child("testQuestionDetails").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        TestQuestionDetails testQuestionDetails = ds.getValue(TestQuestionDetails.class);

                        ds.child("");
                        try {

                            testQuestionDetailsArrayList.add(testQuestionDetails);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    Log.i("size of array: ", testQuestionDetailsArrayList.size()+"");
                    displayQuestion();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }


    public void onNextButtonClicked(View view){
        if(buttonNext.getText().equals("Submit")){

        }
        else
            questionNo++;
        if(questionNo < (testQuestionDetailsArrayList.size()-1)) {
            textViewQuestion.setText("");
            displayQuestion();
        }
        else{
            textViewQuestion.setText("");
            buttonNext.setText("Submit");
            displayQuestion();
        }
    }

    public void onPreviousButtonClicked(View view){

        if(questionNo>=1){
            questionNo--;
            textViewQuestion.setText("");
            displayQuestion();
        }

    }

    private void XMLReferences() {

        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        radioButtonOptionA = findViewById(R.id.radioButtonOptionA);
        radioButtonOptionB = findViewById(R.id.radioButtonOptionB);
        radioButtonOptionC = findViewById(R.id.radioButtonOptionC);
        radioButtonOptionD = findViewById(R.id.radioButtonOptionD);
        buttonNext = findViewById(R.id.buttonNext);
    }
}
