package android.unipu.theater.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferModel {


    public OfferModel(String naziv, String kategorija, String datum, String vrijeme, int id){
        this.naziv = naziv;
        this.kategorija = kategorija;
        this.datum_prikazivanja = datum;
        this.vrijeme_prikazivanja = vrijeme;
        this.id = id;
    }

    public OfferModel(String naziv, String kategorija, String datum, String vrijeme, int id,int kolicina){
        this.naziv = naziv;
        this.kategorija = kategorija;
        this.datum_prikazivanja = datum;
        this.vrijeme_prikazivanja = vrijeme;
        this.kolicina = kolicina;
        this.id = id;
    }

    public OfferModel(String datum, String vrijeme){
        this.datum_prikazivanja = datum;
        this.vrijeme_prikazivanja = vrijeme;
    }

    public OfferModel(){}

    @SerializedName("id")
    @Expose
    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @SerializedName("id_predstava")
    @Expose
    private int id_predstava;

    public void setId_predstava(int id_predstava){
        this.id_predstava = id_predstava;
    }

    public int getId_predstava(){
        return id_predstava;
    }

    @SerializedName("id_korisnik")
    @Expose
    private int id_korisnik;

    public void setId_korisnik(int id_korisnik){
        this.id_korisnik = id_korisnik;
    }

    public int getId_korisnik(){
        return id_korisnik;
    }

    @SerializedName("datum_prikazivanja")
    @Expose
    private String datum_prikazivanja;

    public void setDatum_prikazivanja(String datum_prikazivanja){
        this.datum_prikazivanja = datum_prikazivanja;
    }

    public String getDatum_prikazivanja(){
        return datum_prikazivanja;
    }

    @SerializedName("datum_unosa")
    @Expose
    private String datum_unosa;

    public void setDatum_unosa(String datum_unosa){
        this.datum_unosa = datum_unosa;
    }

    public String getDatum_unosa(){
        return datum_unosa;
    }

    @SerializedName("vrijeme_prikazivanja")
    @Expose
    private String vrijeme_prikazivanja;

    public void setVrijeme_prikazivanja(String vrijeme_prikazivanja){
        this.vrijeme_prikazivanja = vrijeme_prikazivanja;
    }

    public String getVrijeme_prikazivanja(){
        return vrijeme_prikazivanja;
    }

    @SerializedName("max_ulaznica")
    @Expose
    private int max_ulaznica;

    public void setMax_ulaznica(int max_ulaznica){
        this.max_ulaznica = max_ulaznica;
    }

    public int getMax_ulaznica(){
        return max_ulaznica;
    }

    @SerializedName("trenutno_ulaznica")
    @Expose

    private int trenutno_ulaznica;

    public void setTrenutno_ulaznica(int trenutno_ulaznica){
        this.trenutno_ulaznica = trenutno_ulaznica;
    }

    public int getTrenutno_ulaznica(){
        return trenutno_ulaznica;
    }

    @SerializedName("dvorana")
    @Expose
    private String dvorana;

    public void setDvorana(String dvorana){
        this.dvorana = dvorana;
    }

    public String getDvorana(){
        return dvorana;
    }

    @SerializedName("premijera")
    @Expose
    private String premijera;

    public void setPremijera(String premijera){
        this.premijera = premijera;
    }

    public String getPremijera(){return premijera; }

    private boolean zauzetost ;

    public void setZauzetost(boolean zauzetost){
        this.zauzetost = zauzetost;
    }

    public boolean getZauzetost(){
        return zauzetost;
    }

    private int uvjet;

    public void setUvjet(int uvjet){
        this.uvjet = uvjet;
    }

    public int getUvjet(){
        return uvjet;
    }

    private int pozicija;

    public void setPozicija(int pozicija){
        this.pozicija = pozicija;
    }

    public int getPozicija(){
        return pozicija;
    }

    @SerializedName("kategorija")
    @Expose
    private String kategorija;

    public enum kategorija{
        drama, komedija, tragikomedija, vodvilj, glazba, melodrama, balet, parodija,opera
    };

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getKategorija() {
        return kategorija;
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

    private int kolicina;

    public void setKolicina(int kolicina){
        this.kolicina = kolicina;
    }

    public int getKolicina(){
        return kolicina;
    }

    private int razlika;

    public void setRazlika(int razlika){
        this.razlika = razlika;
    }

    public int getRazlika(){return razlika;}

}
