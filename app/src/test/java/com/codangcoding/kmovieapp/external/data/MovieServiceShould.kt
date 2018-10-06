package com.codangcoding.kmovieapp.external.data

import com.codangcoding.kmovieapp.domain.entity.Movie
import com.codangcoding.kmovieapp.util.loadJSON
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class MovieServiceShould {

    private val mapper = ObjectMapper()
        .registerKotlinModule()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    private val okHttpClient = OkHttpClient()

    private val webServer = MockWebServer()

    private lateinit var service: MovieService

    @Before
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(webServer.url(""))
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        service = retrofit.create(MovieService::class.java)
    }

    @Test
    fun return_popular_movies_on_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(200)
            .setBody(loadJSON(this.javaClass, "json/movie_response.json"))
        webServer.enqueue(mockReponse)

        val expectedMovies = listOf(
            Movie(
                title = "Venom",
                vote = 6.4,
                overview = "When Eddie Brock acquires the powers of a symbiote, he will have to release his alter-ego Venom to save his life.",
                releaseDate = "2018-10-03",
                posterPath = "/2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                backdropPath = "/VuukZLgaCrho2Ar8Scl9HtV3yD.jpg"
            ),
            Movie(
                title = "Ant-Man and the Wasp",
                vote = 6.9,
                overview = "Just when his time under house arrest is about to end, Scott Lang puts again his freedom at risk to help Hope van Dyne and Dr. Hank Pym dive into the quantum realm and try to accomplish, against time and any chance of success, a very dangerous rescue mission.",
                releaseDate = "2018-07-04",
                posterPath = "/rv1AWImgx386ULjcf62VYaW8zSt.jpg",
                backdropPath = "/6P3c80EOm7BodndGBUAJHHsHKrp.jpg"
            )
        )

        val actualMovies = service.getPopularMovies().await().movies
        assertEquals(expectedMovies, actualMovies)
    }

    @Test(expected = HttpException::class)
    fun throw_http_exception_when_get_popular_movies_on_non_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(403)
        webServer.enqueue(mockReponse)

        service.getPopularMovies().await()

        Unit // to hide warning, because test case should return unit
    }

    @Test
    fun return_now_playing_movies_on_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(200)
            .setBody(loadJSON(this.javaClass, "json/movie_response.json"))
        webServer.enqueue(mockReponse)

        val expectedMovies = listOf(
            Movie(
                title = "Venom",
                vote = 6.4,
                overview = "When Eddie Brock acquires the powers of a symbiote, he will have to release his alter-ego Venom to save his life.",
                releaseDate = "2018-10-03",
                posterPath = "/2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                backdropPath = "/VuukZLgaCrho2Ar8Scl9HtV3yD.jpg"
            ),
            Movie(
                title = "Ant-Man and the Wasp",
                vote = 6.9,
                overview = "Just when his time under house arrest is about to end, Scott Lang puts again his freedom at risk to help Hope van Dyne and Dr. Hank Pym dive into the quantum realm and try to accomplish, against time and any chance of success, a very dangerous rescue mission.",
                releaseDate = "2018-07-04",
                posterPath = "/rv1AWImgx386ULjcf62VYaW8zSt.jpg",
                backdropPath = "/6P3c80EOm7BodndGBUAJHHsHKrp.jpg"
            )
        )

        val actualMovies = service.getNowPlayingMovies().await().movies
        assertEquals(expectedMovies, actualMovies)
    }

    @Test(expected = HttpException::class)
    fun throw_http_exception_when_get_now_playing_movies_on_non_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(403)
        webServer.enqueue(mockReponse)

        service.getNowPlayingMovies().await()

        Unit // to hide warning, because test case should return unit
    }
}