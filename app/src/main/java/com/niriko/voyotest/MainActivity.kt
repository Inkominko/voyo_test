package com.niriko.voyotest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.niriko.voyotest.view.MainScreen
import com.niriko.voyotest.ui.theme.VoyoTheme
import com.niriko.voyotest.model.ApiResponse
import com.niriko.voyotest.services.ApiService
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val apiService by lazy {
        createApiService()
    }

    private var currentPart by mutableIntStateOf(1)
    private var mediaList by mutableStateOf(emptyList<Pair<String, List<String>>>())
    private var isNewDataAvailable by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VoyoTheme {
                MainScreen(
                    mediaList = mediaList,
                    isNewDataAvailable = isNewDataAvailable,
                    addNextPage = { addNextPage() }
                )
            }
            loadPage()
        }
    }

    private fun createApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gql.voyo.si/graphql/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    private fun loadPage() {
        val query = "onl_web_basic_front"
        val url = "/"
        val baseUrl = "https://gql.voyo.si/graphql/"
        val queryParams = "query=$query(url:\"$url\" siteId:30005 part:$currentPart)"

        val fullUrl = "$baseUrl?$queryParams"

        // avoid auto encoding
        val httpUrl = fullUrl.toHttpUrl()

        val call = apiService.fetchUrls(httpUrl.toString())

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val newData = getData(response.body())
                    val partData = response.body()?.data?.front
                    if (newData.isNotEmpty() && partData != null) {
                        mediaList += newData
                        isNewDataAvailable = currentPart < partData.nbDataParts
                    }
                } else {
                    Log.e("DebugTag", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("DebugTag", "Network request failed", t)
            }
        })
    }

    private fun getData(apiResponse: ApiResponse?): List<Pair<String, List<String>>> {
        val dataList = mutableListOf<Pair<String, List<String>>>()

        if (apiResponse?.data?.front?.data?.isNotEmpty() == true) {
            for (dataItem in apiResponse.data.front.data) {
                val categoryName = dataItem.name ?: continue
                val imageUrls = mutableListOf<String>()

                for (payloadItem in dataItem.payload) {
                    val imageUrl = payloadItem.portraitImage?.getFormattedUrl() ?: continue
                    imageUrls.add(imageUrl)
                }
                dataList.add(Pair(categoryName, imageUrls))
            }
        }
        return dataList
    }

    private fun addNextPage() {
        currentPart++
        loadPage()
    }
}