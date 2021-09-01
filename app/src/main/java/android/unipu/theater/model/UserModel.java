package android.unipu.theater.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

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

    @SerializedName("telefon")
    @Expose
    private String telefon;

    public void setTelefon(String telefon){
        this.telefon = telefon;
    }

    public String getTelefon(){
        return telefon;
    }

    @SerializedName("privatni_broj")
    @Expose
    private int broj;

    public void setBroj(int broj){
        this.broj = broj;
    }

    public int getBroj(){
        return broj;
    }

    @SerializedName("email")
    @Expose
    private String e_mail;

    public void setEmail(String email){
        e_mail = email;
    }

    public String getEmail(){
        return e_mail;
    }

    @SerializedName("lozinka")
    @Expose
    private String lozinka;

    public void setLozinka(String lozinka){
        this.lozinka = lozinka;
    }

    public String getLozinka(){
        return lozinka;
    }


    @SerializedName("slika")
    @Expose
    private String slika;

    public void setSlika(String slika){
        this.slika = slika;
    }

    public String getSlika(){
        return slika;
    }

    @SerializedName("potvrda")
    @Expose
    private boolean potvrda;

    public void setPotvrda(boolean potvrda){
        this.potvrda = potvrda;
    }

    public boolean getPotvrda(){
        return potvrda;
    }

    @SerializedName("id")
    @Expose
    private String id;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    @SerializedName("kljuc")
    @Expose
    private static String kljuc;

    public void setKljuc(String kljuc_novo){
        kljuc = kljuc_novo;
    }

    public String getKljuc(){
        return kljuc;
    }

    private boolean odgovor;

    public void setOdgovor(boolean odgovor){
        this.odgovor = odgovor;
    }

    public boolean getOdgovor(){return odgovor; }

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
