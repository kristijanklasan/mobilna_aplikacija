package android.unipu.theater.model;

public class QuestionModel {

    public QuestionModel(int id, String pitanje, String odgovor){
        this.id = id;
        this.pitanje = pitanje;
        this.odgovor = odgovor;
        this.expanded = false;
    }

    public QuestionModel(){};

    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    private int id_administrator;

    public void setId_administrator(int id_administrator){
        this.id_administrator = id_administrator;
    }

    public int getId_administrator(){
        return id_administrator;
    }

    private String pitanje;

    public void setPitanje(String pitanje){
        this.pitanje = pitanje;
    }

    public String getPitanje(){
        return pitanje;
    }

    private String odgovor;

    public void setOdgovor(String odgovor){
        this.odgovor = odgovor;
    }

    public String getOdgovor(){
        return odgovor;
    }

    private boolean expanded;

    public boolean isExpanded(){
        return  expanded;
    }

    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }
}
