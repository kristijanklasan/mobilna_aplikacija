package android.unipu.theater.retrofit;

import android.unipu.theater.model.OfferModel;

import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IOffer {

    @POST("ponuda/unosPonude")
    @FormUrlEncoded
    Call<ResponseBody> sendOffer(@Field("id_korisnik") int id_korisnik,
                                    @Field("id_predstava") int id_predstava,
                                    @Field("datum_unosa") String datum_unosa,
                                    @Field("datum") String datum,
                                    @Field("datum_prikazivanja") String datum_prikazivanja,
                                    @Field("vrijeme_prikazivanja") String vrijeme_prikazivanja,
                                    @Field("max_ulaznica") int max_ulaznica,
                                    @Field("trenutno_ulaznica") int trenutno_ulaznica,
                                    @Field("dvorana") String dvorana,
                                    @Field("premijera") String premijera);

    @GET("predstava/dohvatiVrijeme/{datum_prikazivanja}")
    Call<List<OfferModel>> getPrikazivanje(@Path("datum_prikazivanja") String datum_prikazivanja);

    @GET("ponuda/dohvatPonuda/{naziv}")
    Call<List<OfferModel>> getDataForAdapter(@Path("naziv") String naziv);

    @GET("ponuda/dohvatSvihPonuda")
    Call<List<OfferModel>> getAllDataForAdapter();

    @GET("ponuda/dohvatPonudeID/{id}")
    Call<List<OfferModel>> getDataFromID(@Path("id") int id);

    @DELETE("ponuda/brisanjePonude/{id}")
    Call<ResponseBody> deleteOffer(@Path("id") int id);

    @PUT("ponuda/updatePonude/{id}/{datum}/{datum_prikazivanja}/{vrijeme_prikazivanja}/{max_ulaznica}/{trenutno_ulaznica}/{premijera}")
    @FormUrlEncoded
    Call<ResponseBody> updateOffer(@Field("id") int id,
                                   @Field("datum") String datum,
                                   @Field("datum_prikazivanja") String datum_prikazivanja,
                                   @Field("vrijeme_prikazivanja") String vrijeme_prikazivanja,
                                   @Field("max_ulaznica") int max_ulaznica,
                                   @Field("trenutno_ulaznica") int trenutno_ulaznica,
                                   @Field("premijera") String premijera);

    @GET("ulaznice/dohvatUlaznica/{id_predstava}")
    Call<List<OfferModel>> getUlaznice(@Path("id_predstava") int id_predstava);

}
