package ExamFramework_Data;

/**
 * Created by Prasanna on 28-08-2015.
 */
public class DailyExam {

    private int id, QuesNo, answeredFlag, timeTaken, Rank, CorrectAns;
    private String Ques, Choice1, Choice2, Choice3, Choice4, Choice5, Category;

    public DailyExam() {
    }

    public DailyExam(int id, int QuesNo, String Ques, String Choice1, String Choice2, String Choice3, String Choice4, String Choice5, int CorrectAns, String Category) {
        super();
        this.id = id;
        this.QuesNo = QuesNo;
        this.Ques = Ques;
        this.Choice1 = Choice1;
        this.Choice2 = Choice2;
        this.Choice3 = Choice3;
        this.Choice4 = Choice4;
        this.Choice5 = Choice5;
        this.CorrectAns = CorrectAns;
        this.Category = Category;

    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getQuesNo() {
        return QuesNo;
    }

    public void setQuesNo(int QuesNo) {
        this.QuesNo = QuesNo;
    }


    public int getansweredFlag() {
        return answeredFlag;
    }

    public void setansweredFlag(int answeredFlag) {
        this.answeredFlag = answeredFlag;
    }


    public int gettimeTaken() {
        return timeTaken;
    }

    public void settimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int Rank) {
        this.Rank = Rank;
    }

    public String getQues() {
        return Ques;
    }

    public void setQues(String Ques) {
        this.Ques = Ques;
    }


    public String getChoice1() {
        return Choice1;
    }

    public void setChoice1(String Choice1) {
        this.Choice1 = Choice1;
    }


    public String getChoice2() {
        return Choice2;
    }

    public void setChoice2(String Choice2) {
        this.Choice2 = Choice2;
    }


    public String getChoice3() {
        return Choice3;
    }

    public void setChoice3(String Choice3) {
        this.Choice3 = Choice3;
    }


    public String getChoice4() {
        return Choice4;
    }

    public void setChoic4(String Choice4) {
        this.Choice4 = Choice4;
    }

    public String getChoice5() {
        return Choice5;
    }

    public void setChoice5(String Choice5) {
        this.Choice5 = Choice5;
    }

    public int getCorrectAns() {
        return CorrectAns;
    }

    public void setCorrectAns(int CorrectAns) {
        this.CorrectAns = CorrectAns;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

}
