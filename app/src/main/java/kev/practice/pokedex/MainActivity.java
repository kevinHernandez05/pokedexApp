package kev.practice.pokedex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creando instancia de RetroFit
        retrofit = new Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


        ObtenerDatos();
    }

    public void ObtenerDatos(){

        pokeApiService service = retrofit.create(pokeApiService.class);

        final Call<PokemonRespuesta> pokemonRespuestaCall = service.ObtenerListaPokemon();

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {

                //Validar respuesta
                if(response.isSuccessful()){

                    PokemonRespuesta pokemonrespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonrespuesta.getResults();
                    Log.i(TAG, "Respuesta completada");

                    for (int a = 0; a < listaPokemon.size(); a++){

                        Pokemon p = listaPokemon.get(a);
                        Log.i(TAG, "Pokemon: " + p.getName());
                    }
                }
                else{
                    Log.e(TAG, "OnResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {

            }
        });

    }
}
