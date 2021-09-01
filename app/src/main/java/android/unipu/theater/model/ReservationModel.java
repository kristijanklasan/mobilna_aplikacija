package android.unipu.theater.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReservationModel {

    public ReservationModel(){ }

    public ReservationModel(String ime, String prezime, int kolicina, float ukupno,
                            String datum_dodavanja,String potvrda, int id,int id_ponude){
        this.ime = ime;
        this.prezime = prezime;
        this.kolicina = kolicina;
        this.ukupno = ukupno;
        this.datum_dodavanja = datum_dodavanja;
        this.potvrda = potvrda;
        this.id = id;
        this.id_ponuda = id_ponude;
    }

    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){return id;}


    private int id_ponuda;

    public void setId_ponuda(int id_ponuda){
        this.id_ponuda = id_ponuda;
    }

    public int getId_ponuda(){return id_ponuda;}

    private ArrayList<Integer> sjedaloLista;

    public void setSjedaloLista(ArrayList<Integer> sjedaloLista){
        this.sjedaloLista = sjedaloLista;
    }

    public ArrayList<Integer> getSjedaloLista(){return sjedaloLista; }

    private ArrayList<Integer> redLista;

    public void setRedLista(ArrayList<Integer> redLista){
        this.redLista = redLista;
    }

    public ArrayList<Integer> getRedLista(){return redLista; }

    private ArrayList<Integer> oznakaLista;

    public void setOznakaLista(ArrayList<Integer> oznakaLista){
        this.oznakaLista = oznakaLista;
    }

    public ArrayList<Integer> getOznakaLista(){
        return oznakaLista;
    }

    private int red;

    public void setRed(int red){
        this.red = red;
    }

    public int getRed(){return red;}

    private int sjedalo;

    public void setSjedalo(int sjedalo){
        this.sjedalo = sjedalo;
    }

    public int getSjedalo(){return sjedalo; }

    private int oznaka;

    public void setOznaka(int oznaka){
        this.oznaka = oznaka;
    }

    public int getOznaka(){return oznaka; }

    private float ukupno;

    public void setUkupno(float ukupno){
        this.ukupno = ukupno;
    }

    public float getUkupno(){
        return ukupno;
    }

    private float cijena;

    public void setCijena(float cijena){
        this.cijena = cijena;
    }

    public float getCijena(){
        return cijena;
    }

    private int kolicina;

    public void setKolicina(int kolicina){
        this.kolicina = kolicina;
    }

    public int getKolicina(){
        return kolicina;
    }

    private int id_korisnik;

    public void setId_korisnik(int id_korisnik){
        this.id_korisnik = id_korisnik;
    }

    public int getId_korisnik(){return id_korisnik; }

    private int id_raspored;

    public void setId_raspored(int id_raspored){
        this.id_raspored = id_raspored;
    }

    public int getId_raspored(){return id_raspored;}

    private String datum_dodavanja;

    public void setDatum_dodavanja(String datum_dodavanja){
        this.datum_dodavanja = datum_dodavanja;
    }

    public String getDatum_dodavanja(){return datum_dodavanja; }

    @SerializedName("datum_prikazivanja")
    @Expose
    private String datum_prikazivanja;

    public void setDatum_prikazivanja(String datum_prikazivanja){
        this.datum_prikazivanja = datum_prikazivanja;
    }

    public String getDatum_prikazivanja(){
        return datum_prikazivanja;
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

    @SerializedName("kategorija")
    @Expose
    private String kategorija;

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

    @SerializedName("ime")
    @Expose
    private String ime;

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getIme() {
        return ime;
    }

    @SerializedName("prezime")
    @Expose
    private String prezime;

    public void setPrezime(String prezime){
        this.prezime = prezime;
    }

    public String getPrezime(){
        return prezime;
    }

    @SerializedName("potvrda")
    @Expose
    private String potvrda;

    public void setPotvrda(String potvrda){
        this.potvrda = potvrda;
    }

    public String getPotvrda(){return potvrda; }

    @SerializedName("trenutno_ulaznica")
    @Expose
    private int ulaznice;

    public void setUlaznice(int ulaznice) {
        this.ulaznice = ulaznice;
    }

    public int getUlaznice(){
        return ulaznice;
    }

    @SerializedName("max_ulaznica")
    @Expose
    private int maxUlaznica;

    public void setMaxUlaznica(int maxUlaznica) {
        this.maxUlaznica = maxUlaznica;
    }

    public int getMaxUlaznice(){
        return maxUlaznica;
    }

    public float calculatePrice(float cijena, int kolicina){
        return cijena*kolicina;
    }
}
