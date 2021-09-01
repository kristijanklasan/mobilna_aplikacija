package android.unipu.theater.retrofit;

import android.unipu.theater.model.FavoritesModel;

import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IFavorites {

    @GET("izbor/provjeraIzbora/{id_korisnik}/{id_predstava}")
    Call<List<FavoritesModel>> checkAvailability(@Path("id_korisnik") int id_korisnik,
                                                 @Path("id_predstava") int id_predstava);

    @POST("izbor/noviIzbor")
    @FormUrlEncoded
    Call<ResponseBody> insertFavorites(@Field("datum_dodavanja") String datum_dodavanja,
                                       @Field("id_korisnik") int id_korisnik,
                                       @Field("id_predstava") int id_predstava);

    @DELETE("izbor/izbrisiIzbor/{id}")
    Call<ResponseBody> deleteFavorites(@Path("id") int id);

    @GET("izbor/dohvatIzvora/{id_korisnik}")
    Call<List<FavoritesModel>> getData(@Path("id_korisnik") int id_korisnik);

    @GET("izbor/dohvatDatum/{id_korisnik}/{datum_dodavanja}")
    Call<List<FavoritesModel>> getDesiredData(@Path("id_korisnik") int id_korisnik,
                                              @Path("datum_dodavanja") String datum_dodavanja);

}
