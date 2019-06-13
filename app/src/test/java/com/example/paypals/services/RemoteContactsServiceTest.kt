package com.example.paypals.services

import com.example.paypals.getTestFuelResponse
import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URL

class RemoteContactsServiceTest {

    private val service = RemoteContactsService()

    @Test
    fun `fetchContacts should retrieve a list of contacts from the api when successful`() {

        val client = mockk<Client>()
        val pokemonJson = "{\"count\":964,\"next\":\"https://pokeapi.co/api/v2/pokemon?offset=10&limit=10\",\"previous\":null,\"results\":[{\"name\":\"bulbasaur\",\"url\":\"https://pokeapi.co/api/v2/pokemon/1/\"},{\"name\":\"ivysaur\",\"url\":\"https://pokeapi.co/api/v2/pokemon/2/\"},{\"name\":\"venusaur\",\"url\":\"https://pokeapi.co/api/v2/pokemon/3/\"},{\"name\":\"charmander\",\"url\":\"https://pokeapi.co/api/v2/pokemon/4/\"},{\"name\":\"charmeleon\",\"url\":\"https://pokeapi.co/api/v2/pokemon/5/\"},{\"name\":\"charizard\",\"url\":\"https://pokeapi.co/api/v2/pokemon/6/\"},{\"name\":\"squirtle\",\"url\":\"https://pokeapi.co/api/v2/pokemon/7/\"},{\"name\":\"wartortle\",\"url\":\"https://pokeapi.co/api/v2/pokemon/8/\"},{\"name\":\"blastoise\",\"url\":\"https://pokeapi.co/api/v2/pokemon/9/\"},{\"name\":\"caterpie\",\"url\":\"https://pokeapi.co/api/v2/pokemon/10/\"}]}"

        every { client.executeRequest(any()) } returns getTestFuelResponse(
            url = URL("https://pokeapi.co/api/v2/pokemon?limit=50"),
            jsonString = pokemonJson
        )

        FuelManager.instance.client = client

        val result = service.fetchContacts()
            .blockingFirst()

        assertEquals(result.size, 10)
    }
}