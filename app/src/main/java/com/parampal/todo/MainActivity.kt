package com.parampal.todo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.parampal.todo.ui.main.TodoFragment
import android.widget.Spinner
import com.parampal.todo.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {
    private var spinner: Spinner? = null
    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel!!.loadUsers()
        mainViewModel!!.users.observe(this, Observer {
            this.let {
                updateSpinnerValues()
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TodoFragment.newInstance(1))
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)

        val item = menu.findItem(R.id.spinner)
        spinner = item.actionView as Spinner // get the spinner
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mainViewModel!!.loadTodosForPosition(position.dec())
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun updateSpinnerValues() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, this.mainViewModel!!.getUserList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.spinner -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
