package com.jnr.buscamarvel.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jnr.buscamarvel.R
import com.jnr.buscamarvel.adapters.CharactersAdapter
import com.jnr.buscamarvel.viewmodels.CharactersActivityViewModel

class CharactersActivity : AppCompatActivity() {


    private lateinit var recyclerViewCharacters: RecyclerView
    private lateinit var textSearchCharacters: EditText
    private lateinit var progressCharacters: ProgressBar
    private lateinit var textPageCounter: TextView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var adapter: CharactersAdapter
    private lateinit var mCharactersActivityViewModel: CharactersActivityViewModel
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters)

        viewManager = LinearLayoutManager(this)

        recyclerViewCharacters = findViewById<RecyclerView>(R.id.recyclerViewCharacters).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }

        textSearchCharacters = findViewById(R.id.textSearchCharacters)
        progressCharacters = findViewById(R.id.progressCharacters)
        textPageCounter = findViewById(R.id.textPageCounter)

        mCharactersActivityViewModel = ViewModelProviders.of(this).get(CharactersActivityViewModel::class.java)

        mCharactersActivityViewModel.initialize(this) {
            mCharactersActivityViewModel.getCharacters(0).observe(this, Observer {
                if (recyclerViewCharacters.adapter != null) {
                    recyclerViewCharacters.adapter!!.notifyDataSetChanged()
                } else {
                    recyclerViewCharacters.adapter = CharactersAdapter(it)
                }
            })

            mCharactersActivityViewModel.getPages().observe(this, Observer {
                val text = "1/$it"
                textPageCounter.text = text
            })

            mCharactersActivityViewModel.getIsUpdating().observe(this, Observer {
                if (it) {
                    startLoading()
                } else {
                    finishLoading()
                }
            })
            initRecyclerView()

            textSearchCharacters.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    recyclerViewCharacters.adapter = CharactersAdapter(mCharactersActivityViewModel.searchCharacters(s.toString()).value!!)
                    val pages = mCharactersActivityViewModel.getPages().value
                    val text = "1/$pages"
                    textPageCounter.text = text
                }

            })
        }
    }

    fun initRecyclerView() {
        adapter = CharactersAdapter(mCharactersActivityViewModel.getCharacters(0).value!!)
        recyclerViewCharacters.adapter = adapter
    }

    fun startLoading() {
        recyclerViewCharacters.visibility = View.INVISIBLE
        progressCharacters.visibility = View.VISIBLE
    }

    fun finishLoading() {
        recyclerViewCharacters.visibility = View.VISIBLE
        progressCharacters.visibility = View.GONE
    }

    fun clickRight(view: View) {
        val pages = mCharactersActivityViewModel.getPages().value!!
        if (page < (mCharactersActivityViewModel.getPages().value!!) - 1) {
            page++
            val text = "${page+1}/$pages"
            textPageCounter.text = text
            recyclerViewCharacters.adapter =
                CharactersAdapter(mCharactersActivityViewModel.getCharacters(page).value!!)
        }
    }

    fun clickLeft(view: View) {
        val pages = mCharactersActivityViewModel.getPages().value!!

        if (page > 0) {
            page--
            val text = "${page+1}/$pages"
            textPageCounter.text = text
            recyclerViewCharacters.adapter =
                CharactersAdapter(mCharactersActivityViewModel.getCharacters(page).value!!)
        }
    }
}
