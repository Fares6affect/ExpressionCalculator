package com.example.calculator

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    companion object{
        private const val DATA = "data"
        private const val EXPRESSION = "expression"
        private const val SOLUTION = "solution"
    }

    private lateinit var sharedPrefData: SharedPreferences
    private val solver = SimpleExpressionSolver()
    private lateinit var expressionView: EditText
    private lateinit var answerView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        expressionView = findViewById<EditText>(R.id.expressionView)
        answerView = findViewById<TextView>(R.id.answerView)

        sharedPrefData = getSharedPreferences(DATA,MODE_PRIVATE)

        expressionView.setText(savedInstanceState?.getString(EXPRESSION)?:sharedPrefData.getString(EXPRESSION,""))
        answerView.setText(savedInstanceState?.getString(SOLUTION)?:sharedPrefData.getString(SOLUTION,""))

        expressionView.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val expression = expressionView.text.toString().trim()
                val solution = solver.resolve(expression)
                sharedPrefData.edit()
                    .putString(EXPRESSION,expression)
                    .putString(SOLUTION,solution)
                    .apply()
                answerView.setText(solution)
                true
            }
            else
                false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXPRESSION,expressionView.text.toString())
        outState.putString(SOLUTION,answerView.text.toString())
    }
}