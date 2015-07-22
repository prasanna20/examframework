package com.fyshadows.examframework.examframework;

/**
 * Created by Prasanna on 19-03-2015.
 */
public class storequestiondetails {

    int QuestionNo;
    String Question;
    String Choice1;
    String Choice2;
    String Choice3;
    String Choice4;
    String Choice5;
    String Correct_Ans;
    String Category;

    public storequestiondetails(int QuestionNo,String Question,String Choice1,String Choice2,String Choice3,String Choice4,String Choice5,String Correct_Ans,String Category)
    {
        this.QuestionNo=QuestionNo;
        this.Question=Question;
        this.Choice1=Choice1;
        this.Choice2=Choice2;
        this.Choice3=Choice3;
        this.Choice4=Choice4;
        this.Choice5=Choice5;
        this.Correct_Ans=Correct_Ans;
        this.Category=Category;
    }

    public int getQuestionNo()
    {
        return  this.QuestionNo;
    }

    public String getQuestion()
    {
        return  this.Question;
    }

    public String getChoice1()
    {
        return  this.Choice1;
    }

    public String getChoice2()
    {
        return  this.Choice2;
    }

    public String getChoice3()
    {
        return  this.Choice3;
    }

    public String getChoice4()
    {
        return  this.Choice4;
    }

    public String getChoice5()   {   return  this.Choice5;  }

    public String getCategory()   {   return  this.Category;  }

    public String getCorrect_Ans()
    {
        return  this.Correct_Ans;
    }


}
