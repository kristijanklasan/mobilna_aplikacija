package android.unipu.theater.adapter;

import android.unipu.theater.R;
import android.unipu.theater.model.QuestionModel;
import android.unipu.theater.retrofit.IQuestion;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder>  {

    private IQuestion iQuestion;
    private List<QuestionModel> questionList;

    public QuestionRecyclerAdapter (List<QuestionModel> questionList){
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expandable_reycler_view_question ,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final QuestionModel questionModel = questionList.get(position);

        holder.naslov.setText(questionModel.getPitanje());
        holder.opis.setText(questionModel.getOdgovor());
        boolean isExpanded = questionModel.isExpanded();
        holder.relativeOpis.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if (isExpanded) {
            holder.imageArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        } else {
            holder.imageArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new SweetAlertDialog(holder.itemView.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Jeste li sigurni?")
                        .setContentText("Jednom izbrisano pitanje nije moguće vratiti!")
                        .setCancelText("Odustani!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).setConfirmText("Izbriši!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("Izbrisano!")
                                .setContentText("Pitanje je izbrisano!")
                                .setConfirmText("U redu")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        deleteQuestion(questionModel.getId());
                        questionModel.setExpanded(!questionModel.isExpanded());
                        questionList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    public void deleteQuestion(int id){

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iQuestion = retrofitClient.create(IQuestion.class);

        Call<ResponseBody> call = iQuestion.deleteQuestion(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() { return questionList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageButton deleteButton;
        TextView naslov, opis;
        ImageView imageArrow;
        LinearLayout linearLayoutNaslov;
        RelativeLayout relativeOpis;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);

            imageArrow = itemView.findViewById(R.id.imageArrowQuestion);
            naslov = itemView.findViewById(R.id.textNaslovPitanje);
            opis = itemView.findViewById(R.id.textOpisPitanja);
            relativeOpis = itemView.findViewById(R.id.relativeOpisQuestion);
            linearLayoutNaslov = itemView.findViewById(R.id.layoutNaslovQuestion);
            deleteButton = itemView.findViewById(R.id.imageDeleteQuestion);

            linearLayoutNaslov.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    QuestionModel questionModel = questionList.get(getAdapterPosition());
                    questionModel.setExpanded(!questionModel.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
