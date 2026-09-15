package com.example.calculator

import java.util.Dictionary
import java.util.Stack
import kotlin.jvm.Throws

class SimpleExpressionSolver() {
    private var stackForInt = Stack<Int>()
    private var stackForChar = Stack<Char>()
    private val dictionary = mapOf(
        '(' to 0,
        '+' to 1,
        '*' to 2,
        )

    @Throws(Exception::class)
    fun toPostfixExpression(expression: String):String{
        var postfixExpression = StringBuilder()
        expression.forEach {
            if(it.isDigit())
                postfixExpression.append(it)
            else if(it=='(')
                stackForChar.push(it)
            else if(it==')'){
                while(stackForChar.count()>0 && stackForChar.peek() != '(')
                    postfixExpression.append(stackForChar.pop())
                stackForChar.pop()
            }else if(dictionary.containsKey(it)){
                while(stackForChar.count()>0 && (dictionary.getValue(stackForChar.peek())>=dictionary.getValue(it)))
                    postfixExpression.append(stackForChar.pop())
                stackForChar.push(it)
            }else throw Exception()
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
                    '+' -> a+b
                    else -> a*b
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
            return "Ошибка ввода"
        }
    }
}