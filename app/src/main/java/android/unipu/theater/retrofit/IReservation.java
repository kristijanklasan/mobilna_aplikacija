package android.unipu.theater.retrofit;

import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.model.TheaterModel;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IReservation {

    @GET("rezervacija/dohvatPredstava")
    Call<List<TheaterModel>> getShowName();

    @GET("rezervacija/dohvatDatuma/{naziv}")
    Call<List<OfferModel>> getDate(@Path("naziv") String naziv);

    @GET("rezervacija/dohvatVremena/{naziv}/{datum}")
    Call<List<OfferModel>> getTime(@Path("naziv") String naziv,
                                   @Path("datum") String datum);

    @GET("rezervacija/dohvatPodataka/{naziv}/{datum}/{vrijeme}")
    Call<List<OfferModel>> getData(@Path("naziv") String naziv,
                                   @Path("datum") String datum,
                                   @Path("vrijeme") String vrijeme);

    @GET("cijene/najnovijeCijene")
    Call<List<PricesModel>> getPrices();

    @GET("rezervacija/dohvatPonude/{id}")
    Call<List<OfferModel>> getAllData(@Path("id") int id);

    @GET("statistika/dohvatPodataka/{id}")
    Call<List<ReservationModel>> getStatisticsData(@Path("id") int id);

    @POST("rezervacija/novaRezervacija")
    @FormUrlEncoded
    Call<ResponseBody> setReservation(@Field("id_korisnik") int id_korisnik,
                                @Field("id_cijena") int id_cijena,
                                @Field("id_raspored") int id_raspored,
                                @Field("kolicina") int kolicina,
                                @Field("cijena") float cijena,
                                @Field("ukupno") float ukupno,
                                @Field("potvrda") String potvrda,
                                @Field("ulaznice") int ulaznice,
                                @Field("datum_dodavanja") String datum_dodavanja);

    @POST("karta/rezervacijaSjedala")
    @FormUrlEncoded
    Call<ResponseBody> setSeat(@Field("id_rezervacije") int id_rezervacije,
                               @Field("red") int red,
                               @Field("sjedalo") int sjedalo,
                               @Field("oznaka") int oznaka);

    @GET("rezervacija/provjeraDostupnosti/{id}")
    Call<List<ReservationModel>> checkAvailability(@Path("id") int id);

    @GET("rezervacije/dohvatRezervacija/{id}")
    Call<List<OfferModel>> getAllReservations(@Path("id") int id);

    @GET("rezervacije/rezerviranePredstave/{id}")
    Call<List<OfferModel>> getNames(@Path("id") int id);

    @GET("rezervacije/filtarRezervacija/{id}/{broj}/{naziv}")
    Call<List<OfferModel>> getFilterData(@Path("id") int id,
                                         @Path("broj") int broj,
                                         @Path("naziv") String naziv);

    @GET("rezervacije/detaljiRezervacije/{id_korisnik}/{id_karta}")
    Call<List<ReservationModel>> getReservationDetail(@Path("id_korisnik") int id_korisnik,
                                                        @Path("id_karta") int id_karta);

    @GET("rezervacije/deleteUlaznica/{id_karta}/{kolicina}")
    Call<ResponseBody> deleteReservation(@Path("id_karta") int id,
                                         @Path("kolicina") int kolicina);

    @GET("rezervacija/podaciUlaznice/{id}")
    Call<List<ReservationModel>> getReservationData(@Path("id") int id);

    //Admin update karata
    @PUT("rezervacija/updatePotvrde/{id}/{potvrda}")
    @FormUrlEncoded
    Call<ResponseBody> updateConfirmation(@Field("id") int id,
                                          @Field("potvrda") String potvrda);

    @GET("rezervacija/dohvatPotvrde/{id}/{potvrda}")
    Call<List<ReservationModel>> getConfirmationData(@Path("id") int id,
                                                     @Path("potvrda") String potvrda);

    @GET("rezervacija/detaljiKarte/{id}")
    Call<List<ReservationModel>> getTicketDetail(@Path("id") int id);

    @GET("rezervacija/sveRezervacije/{id}")
    Call<List<ReservationModel>> ticketSeat(@Path("id") int id);


}
