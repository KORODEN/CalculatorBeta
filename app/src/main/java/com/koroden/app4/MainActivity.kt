package com.koroden.app4

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var trueAnswers = 0
    var falseAnswers = 0
    var levelUp = 0
    var min = 0
    var max = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            trueAnswers = savedInstanceState.getInt("trueAnswers")
            falseAnswers = savedInstanceState.getInt("falseAnswers")
            levelUp = savedInstanceState.getInt("level_up")
            min = savedInstanceState.getInt("min")
            max = savedInstanceState.getInt("max")
        }

        if (levelUp == 0) {
            val toast =
                Toast.makeText(applicationContext, "Уровень сложности 1", Toast.LENGTH_SHORT)
            toast.show()
        }

        generator()
    }

    private fun generator(){
        val expression : TextView = findViewById(R.id.expression)
        var expressionLine : String = ""

        val trueAnswer : TextView = findViewById(R.id.trueAnswer)
        val trueAnswerText : String = resources.getString(R.string.true_name)
        trueAnswer.text = String.format(trueAnswerText, trueAnswers)

        val falseAnswer : TextView = findViewById(R.id.falseAnswer)
        val falseAnswerText : String = resources.getString(R.string.false_name)
        falseAnswer.text = String.format(falseAnswerText, falseAnswers)

        var numberA = 0
        var numberB = 0

        if (levelUp == 1) {
            if ((Math.random() * 100).toInt() > 50) {
                numberA = (Math.random() * (max - min + 1) + min).toInt()
                numberB = (Math.random() * 10).toInt()
            } else {
                numberB = (Math.random() * (max - min + 1) + min).toInt()
                numberA = (Math.random() * 10).toInt()
            }
        } else {
            numberA = (Math.random() * (max - min + 1) + min).toInt()
            numberB = (Math.random() * (max - min + 1) + min).toInt()
        }
        expressionLine += numberA

        val signNumber = (Math.random() * 3).toInt()
        var sign = ' '

        var rightAnswer = 0
        var firstAnswer = 0
        var secondAnswer = 0
        var thirdAnswer = 0

        val numbers : Array<Int> = arrayOf(0, 0, 0, 0)
        when (signNumber) {
            0 -> {
                sign = '+'
                rightAnswer = numberA + numberB
                secondAnswer = numberA + numberB + 2
                firstAnswer = numberA + numberB - 1
                thirdAnswer = numberA - numberB - 2
            }
            1 -> {
                sign = '-'
                rightAnswer = numberA - numberB
                secondAnswer = numberA - numberB - 2
                firstAnswer = numberA - numberB + 1
                thirdAnswer = numberA + numberB + 2
            }
            2 -> {
                sign = '*'
                rightAnswer = numberA * numberB
                secondAnswer = numberB * 2 - 1
                firstAnswer = numberA * numberB - 2
                thirdAnswer = numberA * numberB + 2
            }
        }
        expressionLine += sign
        expressionLine += "$numberB="
        expression.text = expressionLine

        when ((Math.random() * 4).toInt()) {
            0 -> {
                numbers[0] = rightAnswer
                numbers[1] = secondAnswer
                numbers[2] = firstAnswer
                numbers[3] = thirdAnswer
            }
            1 -> {
                numbers[0] = thirdAnswer
                numbers[1] = rightAnswer
                numbers[2] = secondAnswer
                numbers[3] = firstAnswer
            }
            2 -> {
                numbers[0] = firstAnswer
                numbers[1] = thirdAnswer
                numbers[2] = rightAnswer
                numbers[3] = secondAnswer
            }
            3 -> {
                numbers[0] = secondAnswer
                numbers[1] = firstAnswer
                numbers[2] = thirdAnswer
                numbers[3] = rightAnswer
            }
        }

        val listView : ListView = findViewById(R.id.listAnswers)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        val adapter = ArrayAdapter<Int>(this,
                android.R.layout.simple_list_item_single_choice, numbers)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            if (((view as TextView).text as String).toInt() == rightAnswer) {
                trueAnswers++
                if (levelUp == 0 && trueAnswers >= 5) {
                    levelUp = 1
                    max *= 10
                    min = 10
                    val toast = Toast.makeText(applicationContext,
                            "Уровень сложности 2", Toast.LENGTH_SHORT)
                    toast.show()
                }else if (levelUp == 1 && trueAnswers >= 10) {
                    levelUp = 2
                    val toast = Toast.makeText(applicationContext,
                            "Уровень сложности 3", Toast.LENGTH_SHORT)
                    toast.show()
                } else
                    Toast.makeText(applicationContext, "Верный ответ",
                            Toast.LENGTH_SHORT).show()

                generator()
            } else {
                Toast.makeText(applicationContext, "Неверный ответ",
                        Toast.LENGTH_SHORT).show()
                falseAnswers++
                generator()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("trueAnswers", trueAnswers)
        outState.putInt("falseAnswers", falseAnswers)
        outState.putInt("level_up", levelUp)
        outState.putInt("max", max)
        outState.putInt("min", min)
    }
}