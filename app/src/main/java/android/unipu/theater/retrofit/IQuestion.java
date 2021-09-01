package android.unipu.theater.retrofit;

import android.unipu.theater.model.QuestionModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IQuestion {

    @POST("pitanja/unosPitanja")
    @FormUrlEncoded
    Call<ResponseBody> insertQuestion(@Field("id_administrator") int id_administrator,
                                    @Field("pitanje") String pitanje,
                                    @Field("odgovor") String odgovor);

    @GET("pitanja/dohvatPitanja")
    Call<List<QuestionModel>> getQuestions();

    @DELETE("pitanja/brisanjePitanja/{id}")
    Call<ResponseBody> deleteQuestion(@Path("id") int id);
}
