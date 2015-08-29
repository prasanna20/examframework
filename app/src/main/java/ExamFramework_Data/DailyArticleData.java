package ExamFramework_Data;

/**
 * Created by Prasanna on 30-08-2015.
 */
public class DailyArticleData {

    public DailyArticleData() {
    }

    private int ArticleNo;

    private String ArticleDate, Topic, ArticleDesc;

    public int getArticleNo() {return ArticleNo;}

    public void setArticleNo(int id) {this.ArticleNo = ArticleNo;}

    public String getArticleDate() {return ArticleDate;}

    public void setArticleDate(String ArticleDate) {this.ArticleDate = ArticleDate;}

    public String getTopic() {return Topic;}

    public void setTopic(String Topic) {this.Topic = Topic;}

    public String getArticleDesc() {return ArticleDesc;}

    public void setArticleDesc(String ArticleDesc) {this.ArticleDesc = ArticleDesc;}

    public DailyArticleData(int ArticleNo, String ArticleDate, String Topic, String ArticleDesc)
    {
        super();
        this.ArticleNo=ArticleNo;
        this.ArticleDate=ArticleDate;
        this.Topic=Topic;
        this.ArticleDesc=ArticleDesc;

    }

}
