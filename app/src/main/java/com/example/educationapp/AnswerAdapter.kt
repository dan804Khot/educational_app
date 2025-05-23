package com.example.educationapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button

class AnswerAdapter(
    private val context: Context,
    private val answers: List<String>,
    private val onClick: (String) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = answers.size

    override fun getItem(position: Int): String = answers[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.answer_item, parent, false)

        val answerButton = view.findViewById<Button>(R.id.answer_text)
        answerButton.text = answers[position]

        answerButton.setOnClickListener {
            onClick(answers[position])
        }

        return view
    }
}
