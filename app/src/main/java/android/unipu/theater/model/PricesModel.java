package android.unipu.theater.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PricesModel {

    private boolean expanded;

    public PricesModel(int id, String datum, float karte_djeca,float karte_odrasli, float karte_studenti, float karte_umirovljenici,
                       float cijenaPremijera, float cijenaDan){
        this.id = id;
        this.datum = datum;
        this.karte_djeca = karte_djeca;
        this.karte_odrasli = karte_odrasli;
        this.karte_studenti = karte_studenti;
        this.karte_umirovljenici = karte_umirovljenici;
        this.premijera = cijenaPremijera;
        this.dan = cijenaDan;
        this.expanded = false;
    }

    public PricesModel(){}

    @SerializedName("id")
    @Expose
    private int id;
    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @SerializedName("datum")
    @Expose
    private String datum;

    public void setDatum(String datum){
        this.datum = datum;
    }

    public String getDatum(){
        return datum;
    }

    @SerializedName("karte_djeca")
    @Expose
    private float karte_djeca;
    public void setCijene_djeca(float karte_djeca){
        this.karte_djeca = karte_djeca;
    }

    public float getCijeneDjeca(){return karte_djeca;}

    @SerializedName("karte_odrasli")
    @Expose
    private float karte_odrasli;
    public void setCijene_odrasli(float karte_odrasli){
        this.karte_odrasli = karte_odrasli;
    }

    public float getCijene_odrasli(){return karte_odrasli;}

    @SerializedName("karte_studenti")
    @Expose
    private float karte_studenti;

    public void setCijene_studenti(float karte_studenti){
        this.karte_studenti = karte_studenti;
    }

    public float getCijene_studenti(){return karte_studenti; }

    @SerializedName("karte_umirovljenici")
    @Expose
    private float karte_umirovljenici;

    public void setCijene_umirovljenici(float karte_umirovljenici){
        this.karte_umirovljenici = karte_umirovljenici;
    }

    public float getCijene_umirovljenici(){return karte_umirovljenici; }

    @SerializedName("premijera")
    @Expose
    private float premijera;

    public void setPremijera(float premijera){
        this.premijera = premijera;
    }

    public float getPremijera(){return premijera; }

    @SerializedName("dan")
    @Expose
    private float dan;

    public void setDan(float dan){
        this.dan = dan;
    }

    public float getDan(){return dan; }

    public boolean isExpanded(){
        return  expanded;
    }

    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }

    private String message;

    public String getMessage(){
        return message;
    }

}
