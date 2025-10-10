package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DetailFragment
import com.example.myapplication.ListFragment
import com.example.myapplication.R

class FragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val listFragment = ListFragment()
        val detailFragment = DetailFragment.newInstance("Выберите элемент")

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_list_container, listFragment)
            .add(R.id.fragment_detail_container, detailFragment)
            .commit()

        // Взаимодействие между фрагментами через Activity
        listFragment.setOnItemSelectedListener { selectedItem ->
            val newDetailFragment = DetailFragment.newInstance(selectedItem)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_detail_container, newDetailFragment)
                .commit()
        }
    }
}