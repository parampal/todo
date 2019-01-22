package com.parampal.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.parampal.todo.ui.main.TodoFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TodoFragment.newInstance(1))
                .commitNow()
        }
    }

}
