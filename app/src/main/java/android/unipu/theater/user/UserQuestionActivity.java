package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.adapter.TermsAdapter;
import android.unipu.theater.model.QuestionModel;
import android.unipu.theater.retrofit.IQuestion;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserQuestionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ExpandableListView expandableList;
    private IQuestion iQuestion;
    HashMap<String, ArrayList<String>> listItem;
    private ArrayList<String> listQuestion, listAnswer;
    TermsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_question);

        initView();
        setElements();
        getQuestion();
    }

    private void initView(){
        listItem = new HashMap<>();
        expandableList = findViewById(R.id.expandable_list);
        toolbar = (Toolbar) findViewById(R.id.toolbarQuestionUser);
        expandableList = (ExpandableListView) findViewById(R.id.expandableQuestionUser);
    }

    private void setElements(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserQuestionActivity.this, MainWindow.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getQuestion(){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iQuestion = retrofit.create(IQuestion.class);

        Call<List<QuestionModel>> call = iQuestion.getQuestions();
        call.enqueue(new Callback<List<QuestionModel>>() {
            @Override
            public void onResponse(Call<List<QuestionModel>> call, Response<List<QuestionModel>> response) {
                if(response.isSuccessful()){
                    listQuestion = new ArrayList<>();
                    listAnswer = new ArrayList<>();

                    List<QuestionModel> list = response.body();

                    for(int i=0; i<list.size(); i++){
                        listQuestion.add(list.get(i).getPitanje());
                        listAnswer.add(list.get(i).getOdgovor());
                    }

                    for(int i=0; i<listQuestion.size(); i++){
                        ArrayList<String> lista = new ArrayList<>();
                        lista.add(listAnswer.get(i));

                        listItem.put(listQuestion.get(i),lista);
                        adapter = new TermsAdapter(listQuestion,listItem);
                        expandableList.setAdapter(adapter);
                    }
                }
                else if (response.code() == 400){
                    Toast.makeText(getApplicationContext(),"Nema podataka za prikazati!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
