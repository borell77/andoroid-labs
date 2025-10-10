package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class ListFragment : Fragment() {

    private var listener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = listOf("Элемент 1", "Элемент 2", "Элемент 3")
        val listView = view.findViewById<ListView>(R.id.listView)
        listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            listener?.invoke(selectedItem)
        }
    }

    fun setOnItemSelectedListener(listener: (String) -> Unit) {
        this.listener = listener
    }
}