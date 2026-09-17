package com.example.calculator

import java.util.Stack
import kotlin.math.pow

class SimpleExpressionSolver() {
    private val dictionary = mapOf(
        '(' to 0,
        '+' to 1,
        '-' to 1,
        '*' to 2,
        '/' to 2,
        '^' to 3
        )

    fun toPostfixExpression(expression: String?):String{
        var stackForChar = Stack<Char>()
        if(expression.isNullOrEmpty())
            throw Exception("Пустая строка")
        var postfixExpression = StringBuilder()
        var indexStart = 0
        for(index in expression.indices){
            val c = expression.get(index)
            if(c.isDigit()||c=='.')
                continue
            else{
                if(indexStart!=index)
                    postfixExpression.append(" ")
                        .append(expression.substring(indexStart,index))
                indexStart=index+1
                if(c=='^'||c=='(')
                    stackForChar.push(c)
                else if(dictionary.containsKey(c)){
                    if(c=='-' && (index==0||expression.get(index-1)=='(')) {
                        postfixExpression.append(" 0")

                    }
                    while(stackForChar.count()>0 && (dictionary.getValue(stackForChar.peek())>=dictionary.getValue(c)))
                        postfixExpression.append(" ")
                            .append(stackForChar.pop())
                    stackForChar.push(c)
                } else if(c==')'){
                    while(stackForChar.count()>0 && stackForChar.peek() != '(')
                        postfixExpression.append(" ")
                            .append(stackForChar.pop())
                    stackForChar.pop()
                }else throw IllegalArgumentException("Неизвестный оператор: ${c}")
            }
        }
        postfixExpression.append(" ")
            .append(expression.substring(indexStart))
        while (stackForChar.isNotEmpty()) {
            postfixExpression.append(" ").append(stackForChar.pop())
        }
        return postfixExpression.toString().trim()
    }

    fun answer(postfixExpression: String): Double{
        var stackForDouble = Stack<Double>()
        postfixExpression.split(" ").forEach {
            if(!"+-*/^".contains(it))
                stackForDouble.push(it.toDouble())
            else {
                val b = stackForDouble.pop()
                val a = stackForDouble.pop()
                val c = when (it) {
                    "+" -> a+b
                    "-" -> a-b
                    "*" -> a*b
                    "/" -> { if (b == 0.0) throw ArithmeticException("Деление на ноль"); a/b }
                    else -> { if (b < 0) throw ArithmeticException("Отрицательная степень"); a.pow(b)}
                }
                stackForDouble.push(c)
            }
        }
        return stackForDouble.pop()
    }

    fun resolve(expression: String?):String{
        try {
            if(expression==null) throw Exception("Передано null значение")
            return answer(toPostfixExpression(expression.replace(" ",""))).toString()
        }catch (e: Exception) {
            return e.message!!
        }
    }
}