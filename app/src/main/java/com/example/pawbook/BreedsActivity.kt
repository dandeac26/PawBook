package com.example.pawbook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pawbook.API.RetrofitClient
import com.example.pawbook.DTO.BreedsApiResponse
import com.example.pawbook.adapter.BreedsAdapter
import com.example.pawbook.model.BreedsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedsActivity : AppCompatActivity() {

    private lateinit var breedsList: ListView
    private val dataSource = mutableListOf<BreedsItem>()
    private lateinit var breedsAdapter : BreedsAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breeds)

        initializeView()
        initDatasource()

        breedsAdapter = BreedsAdapter(this, dataSource)
        breedsList.adapter = breedsAdapter

        setListeners()

        registerForContextMenu(breedsList)

        fetchBreedsData()
    }



    private fun initializeView(){
        breedsList = findViewById(R.id.breedsListView)
    }

    private fun initDatasource() {
        // Use Retrofit to fetch the breeds from the API
        RetrofitClient.instance.getAllBreeds().enqueue(object : Callback<BreedsApiResponse> {
            override fun onResponse(call: Call<BreedsApiResponse>, response: Response<BreedsApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!

                    dataSource.clear()

                    apiResponse.message.forEach { (breed, subBreeds) ->
                        if (subBreeds.isEmpty()) {
                            dataSource.add(BreedsItem(breed.capitalize()))
                        } else {
                            subBreeds.forEach { subBreed ->
                                val breedName = "${breed.capitalize()} $subBreed".capitalize()
                                dataSource.add(BreedsItem(breedName))
                            }
                        }
                    }

                    breedsAdapter.notifyDataSetChanged() // Make sure you have a reference to your adapter
                } else {
                    Log.d("API Response", "Error, response unsuccessful")
                }
            }

            override fun onFailure(call: Call<BreedsApiResponse>, t: Throwable) {
                Log.d("onFailure", "Error, failed")
            }
        })
    }

    private fun setListeners(){
        breedsList.setOnItemClickListener { adapterView, view, position, id ->
            val selectedBreed = dataSource[position].title
            val intent = Intent(this@BreedsActivity, BreedsImageActivity::class.java).apply {
                putExtra("breed", selectedBreed)
            }
            startActivity(intent)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v!!.id == R.id.breedsListView)
        {
            val info = menuInfo as AdapterView.AdapterContextMenuInfo
            menu!!.setHeaderTitle ( dataSource[menuInfo.position].title )

            menuInflater.inflate ( R.menu.breeds_item_menu, menu )
        }
    }

    private fun fetchBreedsData() {
        RetrofitClient.instance.getAllBreeds().enqueue(object : Callback<BreedsApiResponse> {
            override fun onResponse(call: Call<BreedsApiResponse>, response: Response<BreedsApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val breedsList = parseBreeds(response.body()!!)
                    updateBreedsListView(breedsList)
                } else {
                    Log.d("fetchBreedsData", "Error, response unsuccessful")
                }
            }

            override fun onFailure(call: Call<BreedsApiResponse>, t: Throwable) {
                Log.d("onFailure", "Error, failed")
            }
        })
    }

    private fun parseBreeds(apiResponse: BreedsApiResponse): List<String> {
        val breedsList = mutableListOf<String>()
        apiResponse.message.forEach { (breed, subBreeds) ->
            if (subBreeds.isEmpty()) {
                breedsList.add(breed.capitalize())
            } else {
                subBreeds.forEach { subBreed ->
                    breedsList.add("${breed.capitalize()} $subBreed".capitalize())
                }
            }
        }
        return breedsList.sorted()
    }

    private fun updateBreedsListView(breedsList: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.breeds_list_item,R.id.breedsTitleTextView, breedsList)
        val listView: ListView = findViewById(R.id.breedsListView)
        listView.adapter = adapter
    }
}

