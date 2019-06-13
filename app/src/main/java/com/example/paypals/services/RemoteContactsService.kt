package com.example.paypals.services

import com.example.paypals.data.Contact
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rxObject
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import io.reactivex.Observable

class RemoteContactsService {

    fun fetchContacts(): Observable<List<Contact>> {
        return "https://pokeapi.co/api/v2/pokemon?limit=50"
            .httpGet()
            .rxObject(PokemonResponse.Deserializer())
            .flatMapObservable {
                when(it) {
                    is Result.Success -> Observable.just(it.value.toContacts())
                    else -> Observable.error(Throwable("Failed to fetch contacts"))
                }
            }
    }

    data class PokemonResponse(
        val count: Int,
        val next: String,
        val previous: String,
        val results: List<Pokemon>
    ) {
        class Deserializer : ResponseDeserializable<PokemonResponse> {
            override fun deserialize(content: String): PokemonResponse {
                return Gson().fromJson(content, PokemonResponse::class.java)
            }
        }

        fun toContacts() = results.mapIndexed { index, pokemon -> pokemon.toContact(index) }
    }

    data class Pokemon(
        val name: String,
        val url: String
    ) {
        fun toContact(index: Int) = Contact(
            name = name.capitalize(),
            number = Contact.generateRandomNumber(),
            avatarUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${index + 1}.png"
        )
    }

}