package com.bignerdranch.android.geoquiz;

import java.io.Serializable;

public class Question implements Serializable{
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mCompleted;
    private boolean mCheated;

    public Question(int textResId, boolean answerTrue, boolean completed, boolean cheated){
        this.mTextResId = textResId;
        this.mAnswerTrue = answerTrue;
        this.mCompleted = completed;
        this.mCheated = cheated;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isCompleted(){
        return this.mCompleted;
    }

    public void setCompleted(boolean completed){
        this.mCompleted = completed;
    }

    public boolean isCheated(){
        return this.mCheated;
    }

    public void setCheated(boolean cheated){
        this.mCheated = cheated;
    }
}
