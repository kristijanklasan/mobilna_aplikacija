package android.unipu.theater.retrofit;

import android.unipu.theater.model.ImageModel;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.model.TheaterModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ITheater {

    // Administrator
    @POST("image/insert")
    @FormUrlEncoded
    Call<ResponseBody> insertImage(@Field("naziv") String naziv,
                                  @Field("slika") String slika);

    @POST("images/upload")
    @FormUrlEncoded
    Call<TheaterModel> uploadImage(@Field("naziv") String naziv,
                                   @Field("kategorija") String kategorija,
                                   @Field("datum") String datum,
                                   @Field("opis") String opis,
                                   @Field("redatelj") String redatelj,
                                   @Field("glumci") String glumci,
                                   @Field("dramaturgija") String dramaturgija,
                                   @Field("kostimografija") String kostimografija,
                                   @Field("scenografija") String scenografija,
                                   @Field("glazba") String glazba,
                                   @Field("koreografija") String koreografija,
                                   @Field("podrska") String podrska,
                                   @Field("slika1") String slika1,
                                   @Field("slika2") String slika2);

    @DELETE("predstava/brisanje/{id}")
    Call<ResponseBody> deleteShow(@Path("id") int id);

    @GET("predstava/getPredstava/{kategorija}")
    Call<List<TheaterModel>> getPredstava(@Path("kategorija") String kategorija);

    @GET("predstava/getPredstavaDetalji/{id}")
    Call<List<TheaterModel>> getPredstavaDetalji(@Path("id") int id);

    @PUT("predstava/updatePodaci")
    @FormUrlEncoded
    Call<TheaterModel> updateDetaljiPredstave (@Field("id") int id,
                                               @Field("naziv") String naziv,
                                               @Field("kategorija") String kategorija,
                                               @Field("datum") String datum,
                                               @Field("opis") String opis,
                                               @Field("glumci") String glumci,
                                               @Field("redatelj") String redatelj,
                                               @Field("dramaturgija") String dramaturgija,
                                               @Field("kostimografija") String kostimografija,
                                               @Field("scenografija") String scenografija,
                                               @Field("glazba") String glazba,
                                               @Field("koreografija") String koreografija,
                                               @Field("podrska") String podrska,
                                               @Field("slika1") String slika1,
                                               @Field("slika2") String slika2);
    @GET("predstava/dohvatPredstava")
    Call<List<TheaterModel>> dohvatPredstava();

    @GET("predstava/dohvatID/{naziv}")
    Call<List<TheaterModel>> dohvatIdPredstave(@Path("naziv") String naziv);

    @GET("image/getData")
    Call<List<ImageModel>> getImages();

    @DELETE("image/delete/{id}")
    Call<ResponseBody> deleteImages(@Path("id") int id);

}
