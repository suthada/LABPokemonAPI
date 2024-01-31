package th.ac.kku.cis.lab_pokemon_api

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonViewModel : ViewModel() {

    private val _data: MutableLiveData<List<Pokemon>> = MutableLiveData()
    val pokemonList: LiveData<List<Pokemon>> = _data
    
    init{
        fetchDataFromAPI()
    }



    fun fetchDataFromAPI(){
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(PokemonApi::class.java)
        val call: Call<PokemonResModel> = api.getPokemonList()

        call.enqueue(object : Callback<PokemonResModel> {
            override fun onResponse(
                call: Call<PokemonResModel>,
                response: Response<PokemonResModel>
            ) {
                if(response.isSuccessful){
                    //Log.d("PokemonViewModel", "Success:" + response.body().toString())
                    val _result = response.body()?.results ?: emptyList()
                    _data.postValue(_result)
                }
            }
            override fun onFailure(call: Call<PokemonResModel>, t: Throwable) {
                Log.e("PokemonViewModel", "Failed:" + t.message.toString())
            }
        })
    }
}