package com.example.calculator

import java.util.Dictionary
import java.util.Stack
import kotlin.jvm.Throws
import kotlin.math.pow

class SimpleExpressionSolver() {
    private var stackForInt = Stack<Int>()
    private var stackForChar = Stack<Char>()
    private val dictionary = mapOf(
        '(' to 0,
        '+' to 1,
        '-' to 1,
        '*' to 2,
        '/' to 2,
        '^' to 3
        )

    @Throws(Exception::class)
    fun toPostfixExpression(expression: String):String{
        var postfixExpression = StringBuilder()
        var prev: Char? = null
        expression.forEach {
            if(it.isDigit())
                postfixExpression.append(it)
            else if(it=='^'||it=='(')
                stackForChar.push(it)
            else if(dictionary.containsKey(it)){
                if(it=='-' && (prev==null||prev=='('))
                    postfixExpression.append('0')
                while(stackForChar.count()>0 && (dictionary.getValue(stackForChar.peek())>=dictionary.getValue(it)))
                    postfixExpression.append(stackForChar.pop())
                stackForChar.push(it)
            } else if(it==')'){
                while(stackForChar.count()>0 && stackForChar.peek() != '(')
                    postfixExpression.append(stackForChar.pop())
                stackForChar.pop()
            }else throw IllegalArgumentException("Неизвестный оператор: ${it}")
            prev = it
        }
        while (stackForChar.isNotEmpty()) {
            postfixExpression.append(stackForChar.pop())
        }
        return postfixExpression.toString()
    }

    fun answer(postfixExpression: String):Int{
        postfixExpression.forEach {
            if(it.isDigit())
                stackForInt.push(it.digitToInt())
            else {
                val a = stackForInt.pop()
                val b = stackForInt.pop()
                val c = when (it) {
                    '+' -> b+a
                    '-' -> b-a
                    '*' -> b*a
                    '/' -> { if (a == 0) throw ArithmeticException("Деление на ноль"); b/a }
                    '^' -> { if (b < 0) throw ArithmeticException("Отрицательная степень"); a.toDouble().pow(b).toInt() }
                    else -> throw IllegalArgumentException("Неизвестный оператор: ${it}")
                }
                stackForInt.push(c)
            }
        }
        return stackForInt.pop()
    }

    fun resolve(expression: String):String{
        try {
            return answer(toPostfixExpression(expression)).toString()
        }catch (e: Exception) {
            return e.message!!
        }
    }
}