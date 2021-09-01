package android.unipu.theater.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TheaterModel {

    @SerializedName("id")
    @Expose
    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
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

    @SerializedName("datum")
    @Expose
    private String datumPremijere;

    public void setDatumPremijere(String datumPremijere) {
        this.datumPremijere = datumPremijere;
    }

    public String getDatumPremijere(){return datumPremijere;}

    @SerializedName("opis")
    @Expose
    private String opis;

    public void setOpis(String opis){
        this.opis = opis;
    }

    public String getOpis(){
        return opis;
    }

    @SerializedName("redatelj")
    @Expose
    private String redatelj;

    public void setRedatelj(String redatelj){
        this.redatelj = redatelj;
    }

    public String getRedatelj(){
        return redatelj;
    }

    @SerializedName("glumci")
    @Expose

    private String glumci;

    public void setGlumci(String glumci){
        this.glumci = glumci;
    }

    public String getGlumci(){
        return glumci;
    }

    @SerializedName("dramaturgija")
    @Expose
    private String dramaturgija;

    public void setDramaturgija(String dramaturgija){
        this.dramaturgija = dramaturgija;
    }

    public String getDramaturgija(){
        return dramaturgija;
    }

    @SerializedName("kostimografija")
    @Expose
    private String kostimografija;

    public void setKostimografija(String kostimografija){
        this.kostimografija = kostimografija;
    }

    public String getKostimografija(){
        return kostimografija;
    }

    @SerializedName("scenografija")
    @Expose
    private String scenografija;

    public void setScenografija(String scenografija){
        this.scenografija = scenografija;
    }

    public String getScenografija(){
        return scenografija;
    }

    @SerializedName("glazba")
    @Expose
    private String glazba;

    public void setGlazba(String glazba){
        this.glazba = glazba;
    }

    public String getGlazba(){
        return glazba;
    }

    @SerializedName("koreografija")
    @Expose
    private String koreografija;

    public void setKoreografija(String koreografija){
        this.koreografija = koreografija;
    }

    public String getKoreografija(){
        return koreografija;
    }

    @SerializedName("podrska")
    @Expose
    private String tehnickaPodrska;

    public void setTehnickaPodrska(String tehnickaPodrska){
        this.tehnickaPodrska = tehnickaPodrska;
    }

    public String getTehnickaPodrska(){
        return tehnickaPodrska;
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

    @SerializedName("slika2")
    @Expose
    private String slika2;

    public void setSlika2(String slika2){
        this.slika2 = slika2;
    }

    public String getSlika2(){
        return slika2;
    }

    @SerializedName("slika3")
    @Expose
    private String slika3;

    public void setSlika3(String slika3){
        this.slika3 = slika3;
    }

    public String getSlika3(){
        return slika1;
    }

    private List<Uri> list;

    public void setList(List<Uri> list){
        this.list = list;
    }

    public List<Uri> getList(){
        return list;
    }

    private String message;
    private String path;

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<String> predstave;

    public void setNazivPredstave(ArrayList<String> predstave){
        this.predstave = predstave;
    }

    public ArrayList<String> getNazivPredstave(){
        return predstave;
    }

    public LinkedHashMap<Integer,String> hashPredstave;

    public void setHashPredstave(LinkedHashMap<Integer,String> hashPredstave){
        this.hashPredstave = hashPredstave;
    }

    public LinkedHashMap<Integer,String> getHashPredstave(){
        return hashPredstave;
    }

}