import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import th.ac.kku.cis.lab_pokemon_api.PokemonDetail

import retrofit2.http.GET
import retrofit2.http.Path
interface PokemonApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: String): PokemonResponse
}

data class PokemonResponse(
    val name: String,
    val height: Int
)


class PokemonDetailViewModel : ViewModel() {

    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail>
        get() = _pokemonDetail

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val pokemonApiService = retrofit.create(PokemonApiService::class.java)

    fun getPokemonDetail(pokemonId: String?): LiveData<PokemonDetail> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = pokemonApiService.getPokemon(pokemonId!!)
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
                val name = response.name
                _pokemonDetail.postValue(PokemonDetail(imageUrl, name))
            } catch (e: Exception) {
                _pokemonDetail.postValue(PokemonDetail("", "Pokemon details not available"))
            }
        }

        return pokemonDetail
    }
}