import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: String): PokemonResponse
}

data class PokemonResponse(
    val name: String,
    val height: Int,
    val weight: Int,
    //val type: String
)

data class PokemonDetail(
    val imageUrl: String,
    val name: String,
    val height: Int,
    val weight: Int,
    //val type: String
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

    fun getPokemonDetail(pokemonId: String?) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = pokemonApiService.getPokemon(pokemonId!!)
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
                val name = response.name
                val height = response.height
                val weight = response.weight
                //val type = response.type
                _pokemonDetail.postValue(PokemonDetail(imageUrl, name, height, weight ))
            } catch (e: Exception) {
                _pokemonDetail.postValue(PokemonDetail("", "Pokemon details not available", 0,0))
            }
        }
    }
}
