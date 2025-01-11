package org.robbie.modulareducationenvironment;

public interface QuizQuestion {

    /**
     * Called only once when a quiz is started
     * @param quizState the state of the quiz the student has started
     */
    public void onQuizStart(QuizState quizState);

    /**
     * Called every time a quiz is resumed
     * @param quizState the state of the quiz the student has resumed
     */
    public void onQuizResume(QuizState quizState);

    /**
     * Called every time a quiz is closed
     * @param quizState the state of the quiz the student has closed
     */
    public void onQuizClose(QuizState quizState);

    /**
     * Called only once when the whole quiz is submitted. The save methods are called before submitted.
     * @param quizState the state of the quiz the student has submitted
     */
    public void onQuizSubmit(QuizState quizState);

    /**
     * Called when the whole quiz is saved
     * @param quizState the state of the quiz it's been saved
     */
    public void onQuizSave(QuizState quizState);


    /**
     * Called only once on that question when it is first opened
     * @param questionState the state of this question
     */
    public void onThisQuestionStart(QuestionState questionState);

    /**
     * Called every time only on that question that is resumed
     * @param questionState the state of this question
     */
    public void onThisQuestionResume(QuestionState questionState);

    /**
     * Called every time only on that question that is being closed
     * @param questionState the state of this question
     */
    public void onThisQuestionClose(QuestionState questionState);

    /**
     * Called on that question when it is submitted. The save methods are called before submitted.
     * @param questionState the state of this question
     */
    public void onThisQuestionSubmit(QuestionState questionState);

    /**
     * Called on that question when it is saved
     * @param questionState the new state of this question
     */
    public void onThisQuestionSave(QuestionState questionState);

    /**
     * Invoked when any question within the quiz is first opened by the user.
     *
     * Note: This method may be triggered in addition to the `onThisQuestionStart`
     * method for the same question, so ensure your implementation accounts for
     * potential overlaps.
     * @param questionState the state of the question that has been started
     */
    public void onQuestionStart(QuestionState questionState);

    /**
     * Invoked on all questions within the quiz, when a question is
     * resumed within the quiz.
     *
     * Note: This method may be triggered in addition to the `onThisQuestionResume`
     * method for the same question, so ensure your implementation accounts for
     * potential overlaps.
     * @param questionState the state of the question that has been resumed
     */
    public void onQuestionResume(QuestionState questionState);

    /**
     * Invoked on all questions within the quiz, when a question
     * is closed.
     *
     * Note: This method may be triggered in addition to the `onThisQuestionClose`
     * method for the same question, so ensure your implementation accounts for
     * potential overlaps.
     * @param questionState the state of the question that has been closed
     */
    public void onQuestionClose(QuestionState questionState);

    /**
     * Invoked on all questions within the quiz, when a question is
     * submitted. The save methods are called before submitted.
     *
     * Note: This method may be triggered in addition to the `onThisQuestionSubmit`
     * method for the same question, so ensure your implementation accounts for
     * potential overlaps.
     * @param questionState the state of the question that has been submitted
     */
    public void onQuestionSubmit(QuestionState questionState);

    /**
     * Invoked on all questions within the quiz, when a question is
     * saved.
     *
     * Note: This method may be triggered in addition to the `onThisQuestionSave`
     * method for the same question, so ensure your implementation accounts for
     * potential overlaps.
     * @param questionState the state of the question that has been saved
     */
    public void onQuestionSave(QuestionState questionState);
}
