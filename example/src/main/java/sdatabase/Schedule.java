package sdatabase;

public class Schedule {
    private String stitle;   //标题
    private String scontent; //内容
    private String stimes;   //时间
    private int sids;        //编号
    private String stype;
    public Schedule(String ti,int id,String con ,String time ,String stype){
        this.sids=id;
        this.stitle=ti;
        this.scontent=con;
        this.stimes=time;
        this.stype=stype;
    }
    public Schedule(String ti,int id,String con ,String time){
        this.sids=id;
        this.stitle=ti;
        this.scontent=con;
        this.stimes=time;
    }
    public Schedule(String ti,String con,String time){
        this.stitle=ti;
        this.scontent=con;
        this.stimes=time;
    }

    public Schedule(int id,String ti,String time){
        this.sids=id;
        this.stitle=ti;
        this.stimes=time;
    }

    public Schedule(String ti,String con){
        this.stitle=ti;
        this.scontent=con;
    }
    public Schedule(int sids, String stitle){
        this.sids=sids;
        this.stitle=stitle;
    }

    public int getIds() {
        return sids;
    }

    public String getTitle() {
        return stitle;
    }

    public String getContent() {
        return scontent;
    }

    public String getTimes() {
        return stimes;
    }
    public String getStype(){
        return stype;
    }

}
