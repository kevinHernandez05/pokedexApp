package kev.practice.pokedex.pokeApi;

import retrofit2.Call;
import kev.practice.pokedex.model.PokemonRespuesta;
import retrofit2.http.GET;

public interface pokeApiService {

    @GET("pokemon")
    Call<PokemonRespuesta> ObtenerListaPokemon();


}
