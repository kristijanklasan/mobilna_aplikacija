package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.unipu.theater.R;
import android.unipu.theater.adapter.QuestionRecyclerAdapter;
import android.unipu.theater.model.QuestionModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IQuestion;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuestionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabQuestion;
    private EditText editPitanje, editOdgovor;
    private BottomSheetDialog bottomSheetDialog;
    private List<QuestionModel> questionList;
    private IQuestion iQuestion;
    private QuestionModel questionModel;
    private RecyclerView recyclerQuestion;
    private UserModel userModel;
    private View  bottomSheetView;
    private TextView closeView;
    private Button btnSpremi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
        setElements();
        getQuestions();
    }

    private void initView(){
        userModel = new UserModel();
        questionModel = new QuestionModel();
        recyclerQuestion = (RecyclerView) findViewById(R.id.recyclerQuestionAdmin);
        recyclerQuestion.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        toolbar = (Toolbar) findViewById(R.id.toolbarQuestion);
        fabQuestion = (FloatingActionButton) findViewById(R.id.fabQuestion);
        bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_bottom_sheet_question,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer),false);
        bottomSheetDialog = new BottomSheetDialog(
                QuestionActivity.this, R.style.BottomSheetDialog);
        bottomSheetView.findViewById(R.id.btnSpremiPitanje);
        bottomSheetDialog.setContentView(bottomSheetView);
        closeView = (TextView) bottomSheetView.findViewById(R.id.closeTextQuestion);
        editPitanje = (EditText) bottomSheetView.findViewById(R.id.editPitanje);
        editOdgovor = (EditText) bottomSheetView.findViewById(R.id.editOdgovor);
        btnSpremi = (Button) bottomSheetView.findViewById(R.id.btnSpremiPitanje);
        Retrofit retrofit = RetrofitClient.getInstance2();
        iQuestion = retrofit.create(IQuestion.class);
    }

    private void setElements(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, AdminWindow.class);
                startActivity(intent);
                finish();
            }
        });

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        fabQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSpremi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean result = checkText();
                        int id = Integer.parseInt(userModel.getKljuc());
                        String pitanje = editPitanje.getText().toString();
                        String odgovor = editOdgovor.getText().toString();

                        if(result){
                            insertQuestion(id,pitanje,odgovor);
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "Popunite sva polja za unos!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });
    }

    private boolean checkText(){
        String pitanje = editPitanje.getText().toString();
        String odgovor = editOdgovor.getText().toString();

        if(TextUtils.isEmpty(pitanje))
            editPitanje.setError("Unesite pitanje!");
        else
            editPitanje.setBackgroundResource(R.drawable.normal_edittext);

        if(TextUtils.isEmpty(odgovor))
            editOdgovor.setError("Unesite odgovor!");
        else editPitanje.setBackgroundResource(R.drawable.normal_edittext);

        if(!TextUtils.isEmpty(pitanje) && !TextUtils.isEmpty(odgovor)){
            return true;
        }else return false;
    }

    private void insertQuestion(int id, String pitanje, String odgovor){
        Call<ResponseBody> call = iQuestion.insertQuestion(id,pitanje, odgovor);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Pitanje je uspješno dodano!",
                            Toast.LENGTH_SHORT).show();
                    editPitanje.getText().clear();
                    editOdgovor.getText().clear();
                }
                else if(response.code() == 400){
                    Toast.makeText(getApplicationContext(),
                            "Greška! Spremanje nije uspješno!",Toast.LENGTH_SHORT).show();
                }
                getQuestions();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void getQuestions(){
        Call<List<QuestionModel>> call = iQuestion.getQuestions();
        call.enqueue(new Callback<List<QuestionModel>>() {
            @Override
            public void onResponse(Call<List<QuestionModel>> call, Response<List<QuestionModel>> response) {
                if(response.isSuccessful()){
                    List<QuestionModel> list = response.body();
                    questionList = new ArrayList<>();

                    for(int i=0; i<list.size(); i++){
                        questionList.add(new QuestionModel(list.get(i).getId(),
                                list.get(i).getPitanje(),list.get(i).getOdgovor()));
                    }

                    QuestionRecyclerAdapter adapter = new QuestionRecyclerAdapter(questionList);
                    adapter.notifyDataSetChanged();
                    recyclerQuestion.setAdapter(adapter);

                }else if (response.code() == 400){
                    Toast.makeText(getApplicationContext(),"Nema podataka za prikazati!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }
}
