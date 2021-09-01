package android.unipu.theater.retrofit;

import android.unipu.theater.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IKorisnik {

    @GET("")
    Call<ResponseBody> get();

    @POST("register")
    @FormUrlEncoded
    Call<Object> registerUser(@Field("ime") String ime,
                                    @Field("prezime") String prezime,
                                    @Field("telefon") String telefon,
                                    @Field("email") String email,
                                    @Field("lozinka") String lozinka);

    @GET("korisnik/login/{email}/{lozinka}")
    Call<List<UserModel>> getUser(@Path("email") String email,
                                  @Path("lozinka") String lozinka);

    @GET("korisnik/getEmail/{email}")
    Call<List<UserModel>> getEmail(@Path("email") String email);

    @GET("korisnik/getProfileData/{id}")
    Call<List<UserModel>> getProfileData(@Path("id") int id);

    @PUT("korisnik/updateProfil1")
    @FormUrlEncoded
    Call<ResponseBody> updateProfil1(@Field("id") int id,
                                     @Field("potvrda") boolean potvrda,
                                     @Field("ime") String ime,
                                     @Field("prezime") String prezime,
                                     @Field("telefon") String telefon,
                                     @Field("email") String email);

    @PUT("korisnik/updateProfil2")
    @FormUrlEncoded
    Call<ResponseBody> updateProfil2(@Field("id") int id,
                                     @Field("potvrda") boolean potvrda,
                                     @Field("ime") String ime,
                                     @Field("prezime") String prezime,
                                     @Field("telefon") String telefon,
                                     @Field("email") String email,
                                     @Field("lozinka") String lozinka);


}
