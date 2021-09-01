package android.unipu.theater.retrofit;

import android.unipu.theater.model.PricesModel;

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

public interface IPrices {

    // Administrator
    @POST("cijene/unosCijene")
    @FormUrlEncoded
    Call<ResponseBody> insertPrices(@Field("datum") String datum,
                                    @Field("karte_djeca") float karte_djeca,
                                    @Field("karte_odrasli") float karte_odrasli,
                                    @Field("karte_studenti") float karte_studenti,
                                    @Field("karte_umirovljenici") float karte_umirovljenici,
                                    @Field("premijera") float premijera,
                                    @Field("dan") float dan);
    @GET("cijene/dohvatCijena/{broj}")
    Call<List<PricesModel>> getPrices(@Path("broj") int broj);

    @DELETE("cijene/izbrisiCijenu/{id}")
    Call<ResponseBody> deletePrices(@Path("id") int id);

    @GET("cijene/najnovijeCijene")
    Call<List<PricesModel>> getPricesData();
}
