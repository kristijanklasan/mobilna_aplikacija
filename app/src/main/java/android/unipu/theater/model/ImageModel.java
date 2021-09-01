package android.unipu.theater.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageModel {

    public ImageModel(){}

    private String imageUrl;

    public ImageModel(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    @SerializedName("id")
    @Expose
    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @SerializedName("naziv")
    @Expose
    private String naziv;

    public void setNaziv(String naziv){
        this.naziv = naziv;
    }

    public String getNaziv(){
        return naziv;
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
}
