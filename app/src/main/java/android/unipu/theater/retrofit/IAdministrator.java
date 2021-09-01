package android.unipu.theater.retrofit;

import android.unipu.theater.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAdministrator {

    @GET("admin/login/{email}/{lozinka}")
    Call<List<UserModel>> getAdmin(@Path("email") String email,
                                  @Path("lozinka") String lozinka);

    @GET("admin/dohvatPodataka/{id}")
    Call<List<UserModel>> getAdminData(@Path("id") int id);

    @PUT("profil/update/{potvrda}/{id}/{ime}/{prezime}/{privatni_broj}/{email}")
    @FormUrlEncoded
    Call<ResponseBody> updateAdminData(@Field("potvrda") boolean potvrda,
                                       @Field("id") int id,
                                       @Field("ime") String ime,
                                       @Field("prezime") String prezime,
                                       @Field("privatni_broj") int broj,
                                       @Field("email") String email);

    @PUT("profil/update/sve/{potvrda}/{id}/{ime}/{prezime}/{privatni_broj}/{email}/{lozinka}")
    @FormUrlEncoded
    Call<ResponseBody> updateAdminAllData(@Field("potvrda") boolean potvrda,
                                          @Field("id") int id,
                                          @Field("ime") String ime,
                                          @Field("prezime") String prezime,
                                          @Field("privatni_broj") int broj,
                                          @Field("email") String email,
                                          @Field("lozinka") String lozinka);

    @POST("profil/insertAdmin")
    @FormUrlEncoded
    Call<ResponseBody> insertAdmin(@Field("ime") String ime,
                                    @Field("prezime") String prezime,
                                    @Field("privatni_broj") int telefon,
                                    @Field("email") String email,
                                    @Field("lozinka") String lozinka);





}
