package com.example.sfmscanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sfmscanner.databinding.FragmentFirstBinding
import android.R

import android.widget.EditText




/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            //System.out.println(reverser("Gold"))
            var nstr = ""
            var editTextHello = findViewById(R.id.editTextTextPersonName) as EditText
            val editText = findViewById<EditText>(R.id.cost_of_service_edit_text)

            var str = editTextTextPersonName.text.toString()
            var ch: Char
            for (i in 0 until str.length) {
                ch = str[i] //extracts each character
                nstr = ch.toString() + nstr //adds each character in front of the existing string
            }
            System.out.println(nstr)
            System.out.println("This is a test2")
        }
    }

//    @JvmStatic
//    override fun reverser(str: String) {
//        var nstr = ""
//        var ch: Char
//        for (i in 0 until str.length) {
//            ch = str[i] //extracts each character
//            nstr = ch.toString() + nstr //adds each character in front of the existing string
//        }
//        return(nstr)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}