package com.example.relaxation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.relaxation.databinding.FragmentChatsBinding
import com.example.relaxation.ui.utils.USER
import com.example.relaxation.ui.utils.UserAdapter

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var adapter: UserAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        val list = USER.userList
        recyclerView = binding.recyclerView
        adapter = UserAdapter()
        recyclerView.adapter = adapter
        adapter.setList(list)
    }

    public fun OnItemClick(){
        
    }
}