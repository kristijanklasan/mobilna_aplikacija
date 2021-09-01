package android.unipu.theater.fragmenti.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.unipu.theater.R;
import android.unipu.theater.adapter.TermsAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

public class TermsReservation extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ExpandableListView expandableList;
    ArrayList<String> listaGrupa = new ArrayList<>();
    TermsAdapter adapter;
    HashMap<String, ArrayList<String>> listItem = new HashMap<>();
    private String mParam1;
    private String mParam2;

    public TermsReservation() {
        // Required empty public constructor
    }

    public static TermsReservation newInstance(String param1, String param2) {
        TermsReservation fragment = new TermsReservation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_reservation, container, false);


        expandableList = view.findViewById(R.id.expandable_list);

        listaGrupa.add("Postupak rezervacije ulaznica");
        listaGrupa.add("Registracija");
        listaGrupa.add("Plaćanje ulaznica");
        listaGrupa.add("Uvjeti dostave/isporuke ulaznica");
        listaGrupa.add("Promjene programa");
        listaGrupa.add("Zamjena i reklamacije");

        String [] polje = new String[6];
        polje[0] = "Rezervacija se vrši odabirom željene satnice ispod predstave ili odabirom dana u kalendaru, " +
                "izravno iz izbornika ili u izborniku Rezerviraj ulaznicu. \n\n Nakon izbora predstave i pripadnog termina: \n\n – odaberite željeno mjesto u dvorani \n\n" +
                "– registrirajte se\n \n– provjerite točnost podataka prije rezervacije\n \n– izvršite rezervaciju";

        polje[1] = "Korisnik mora biti registriran kako bi izvršio rezervaciju. Prilikom registracije od korisnika se zahtjeva da unese e-mail adresu i lozinku.";

        polje[2] = "Plaćanje je moguće izvršiti putem kreditne kartice (MasterCard, Visa, Maestro) ili novčanicama prilikom podizanja ulaznica.";

        polje[3] = "U završnom postupku rezervacije ulaznica na sučelju će se prikazati potvrda o rezervaciji sa svim potrebnim podacima.\n";

        polje[4] = "U slučajevima otkazivanja ili promjene rasporeda, " +
                "Kupac za online rezerviranu ulaznicu može na blagajni dobiti povrat novca ili ju zamijeniti za istu predstavu u drugom terminu. " +
                "\n Rok za ostvarivanje prava je najkasnije do vremena na koje se rezervirana ulaznica odnosi.";

        polje[5] = "Zamjenu ulaznica moguće je ostvariti u slučajevima promjene ili otkazivanja predstave.\n" +
                "Rezervirane i ujedno nepreuzete ulaznice, nije moguće tražiti povrat sredstava ili zamjenu za drugu ulaznicu.\n" +
                "Prije nego što rezervaciju konačno potvrdite, provjerite sve podatke o odabranoj predstavi, terminu i vremenu izvođenja.";

        for(int i=0; i<listaGrupa.size(); i++){

            ArrayList<String> lista = new ArrayList<>();
            lista.add(polje[i]);

            listItem.put(listaGrupa.get(i),lista);
            adapter = new TermsAdapter(listaGrupa,listItem);
            expandableList.setAdapter(adapter);
        }
        return view;
    }
}
