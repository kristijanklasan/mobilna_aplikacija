package android.unipu.theater.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavoritesModel {

    @SerializedName("id")
    @Expose
    private int id;
    public void setId(int id){ this.id = id; }

    public int getId(){ return id; }

    @SerializedName("id_korisnik")
    @Expose
    private int id_korisnik;
    public void setId_korisnik(int id_korisnik){
        this.id_korisnik = id_korisnik;
    }

    public int getId_korisnik(){ return id_korisnik; }

    @SerializedName("id_predstava")
    @Expose
    private int id_predstava;
    public void setId_predstava(int id_predstava){
        this.id_predstava = id_predstava;
    }

    public  int getId_predstava(){ return id_predstava; }

    @SerializedName("datum_dodavanja")
    @Expose
    private String datum;

    public void setDatum(String datum){
        this.datum = datum;
    }

    public String getDatum(){
        return datum;
    }

    private boolean dostupnost;

    public void setDostupnost(boolean dostupnost){
        this.dostupnost = dostupnost;
    }

    public boolean getDostupnost(){
        return dostupnost;
    }

    @SerializedName("slika1")
    @Expose
    private String slika1;

    public void setSlika1(String slika1){
        this.slika1 = slika1;
    }

    public String getSlika1(){
        return slika1;
    }

    @SerializedName("naziv")
    @Expose
    private String naziv;

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv(){
        return naziv;
    }

    @SerializedName("kategorija")
    @Expose
    private String kategorija;

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getKategorija() {
        return kategorija;
    }
}
