package kev.practice.pokedex;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import kev.practice.pokedex.model.Pokemon;
import kev.practice.pokedex.model.PokemonRespuesta;
import kev.practice.pokedex.pokeApi.pokeApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final String TAG = "POKEDEX";
    private Retrofit retrofit;

    private int offset;

    private boolean aptoParaCargar;

    //RecyclerView

    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurando el RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        //Ver si el recyclerView llegó al final para hacer otro request y traer 20 pkmns mas a la pokedex

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemcount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (aptoParaCargar){
                        if(visibleItemCount + pastVisibleItems >= totalItemcount){

                            aptoParaCargar = false;
                            offset += 20;
                            ObtenerDatos(offset);
                        }
                    }
                }
            }
        });


        //creando instancia de RetroFit
        retrofit = new Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        aptoParaCargar = true;
        offset = 0;
        ObtenerDatos(offset);
    }

    public void ObtenerDatos(final int offset){

        pokeApiService service = retrofit.create(pokeApiService.class);

        final Call<PokemonRespuesta> pokemonRespuestaCall = service.ObtenerListaPokemon(20, offset);

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {


            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {

                aptoParaCargar = true;

                //Validar respuesta
                if(response.isSuccessful()){

                    PokemonRespuesta pokemonrespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonrespuesta.getResults();
                    Log.i(TAG, "Respuesta completada");

                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);
                    Toast.makeText(MainActivity.this, "Pokemons cargados: " + offset, Toast.LENGTH_SHORT).show();

                }
                else{
                    Log.e(TAG, "OnResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar = true;

            }
        });

    }
}
