package android.unipu.theater;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class ShareMethods {

    public boolean validateEmail(Context context, EditText email){
        String emailString = email.getText().toString();

        if(!emailString.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            return true;
        }else{
            Toast.makeText(context,"E-mail adresa nije ispravna!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
