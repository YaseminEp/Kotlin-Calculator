
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalculatorScreen()
        }
    }
}

@Composable
fun CalculatorScreen() {

    var display by remember { mutableStateOf("0") }
    var justCalculated by remember { mutableStateOf(false) }

    // Sayı ekleme
    fun addDigit(digit: String) {
        if (display == "Hata" || justCalculated) {
            display = digit
            justCalculated = false
        } else {
            display = if (display == "0") digit else display + digit
        }
    }

    // İşlem işareti ekleme veya değiştirme
    fun addOperator(operator: String) {
        if (display == "Hata") return

        justCalculated = false

        if (display.isNotEmpty() &&
            display.last().isDigit()) {
            display += operator
        } else if (display.isNotEmpty() &&
            display.last() in "+-×÷") {
            display = display.dropLast(1) + operator
        }
    }

    // Ondalık sayı ekleme
    fun addDecimal() {
        if (display == "Hata" || justCalculated) {
            display = "0."
            justCalculated = false
            return
        }

        val lastOperatorIndex = display.indexOfLast {
            it in "+-×÷"
        }

        val currentNumber = display.substring(lastOperatorIndex + 1)

        if (!currentNumber.contains(".")) {
            display += if (currentNumber.isEmpty()) "0." else "."
        }
    }

    // Son karakteri silme
    fun deleteLast() {
        if (display == "Hata" || justCalculated) {
            display = "0"
            justCalculated = false
        } else {
            display = if (display.length > 1) {
                display.dropLast(1)
            } else {
                "0"
            }
        }
    }

    // Ekranı temizleme
    fun clearDisplay() {
        display = "0"
        justCalculated = false
    }

    // Pozitif ve negatif sayı değiştirme
    fun toggleSign() {
        if (display == "Hata") return

        val lastOperatorIndex = display.indexOfLast {
            it in "+-×÷"
        }

        val currentNumber = display.substring(lastOperatorIndex + 1)

        if (currentNumber.isEmpty()) return

        val prefix = display.substring(0, lastOperatorIndex + 1)

        val updatedNumber = if (currentNumber.startsWith("-")) {
            currentNumber.drop(1)
        } else {
            "-$currentNumber"
        }

        display = prefix + updatedNumber
        justCalculated = false
    }

    // Yüzde işlemi
    fun applyPercent() {
        if (display == "Hata" || display.isEmpty()) return

        try {
            val value = display.toDouble() / 100.0
            display = formatResult(value)
            justCalculated = true
        } catch (e: Exception) {
            display = "Hata"
        }
    }

    // Hesaplama
    fun calculate() {
        try {
            val result = calculateExpression(display)
            display = formatResult(result)
            justCalculated = true
        } catch (e: Exception) {
            display = "Hata"
            justCalculated = true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF171A21)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.End
        ) {

            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = "Hesap Makinesi",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = display,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Birinci satır
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("AC", Modifier.weight(1f), Color(0xFF626B78)) {
                    clearDisplay()
                }

                CalculatorButton("DEL", Modifier.weight(1f), Color(0xFF626B78)) {
                    deleteLast()
                }

                CalculatorButton("%", Modifier.weight(1f), Color(0xFF626B78)) {
                    applyPercent()
                }

                CalculatorButton("÷", Modifier.weight(1f), Color(0xFFFF9500)) {
                    addOperator("÷")
                }
            }

            // İkinci satır
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("7", Modifier.weight(1f)) { addDigit("7") }
                CalculatorButton("8", Modifier.weight(1f)) { addDigit("8") }
                CalculatorButton("9", Modifier.weight(1f)) { addDigit("9") }
                CalculatorButton("×", Modifier.weight(1f), Color(0xFFFF9500)) {
                    addOperator("×")
                }
            }

            // Üçüncü satır
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("4", Modifier.weight(1f)) { addDigit("4") }
                CalculatorButton("5", Modifier.weight(1f)) { addDigit("5") }
                CalculatorButton("6", Modifier.weight(1f)) { addDigit("6") }
                CalculatorButton("−", Modifier.weight(1f), Color(0xFFFF9500)) {
                    addOperator("-")
                }
            }

            // Dördüncü satır
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("1", Modifier.weight(1f)) { addDigit("1") }
                CalculatorButton("2", Modifier.weight(1f)) { addDigit("2") }
                CalculatorButton("3", Modifier.weight(1f)) { addDigit("3") }
                CalculatorButton("+", Modifier.weight(1f), Color(0xFFFF9500)) {
                    addOperator("+")
                }
            }

            // Beşinci satır
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("±", Modifier.weight(1f), Color(0xFF626B78)) {
                    toggleSign()
                }

                CalculatorButton("0", Modifier.weight(1f)) {
                    addDigit("0")
                }

                CalculatorButton(".", Modifier.weight(1f)) {
                    addDecimal()
                }

                CalculatorButton("=", Modifier.weight(1f), Color(0xFFFF9500)) {
                    calculate()
                }
            }
        }
    }
}

// Tekrar kullanılabilir buton bileşeni
@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF303744),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 23.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// Sonuç formatlama
fun formatResult(number: Double): String {
    require(number.isFinite()) { "Geçersiz sonuç" }

    return if (number % 1.0 == 0.0 &&
        number >= Long.MIN_VALUE.toDouble() &&
        number <= Long.MAX_VALUE.toDouble()) {
        number.toLong().toString()
    } else {
        number.toString()
    }
}

// Matematiksel ifade çözümleyici
fun calculateExpression(expression: String): Double {

    val input = expression
        .replace("×", "*")
        .replace("÷", "/")
        .replace(" ", "")

    var position = 0

    // Sayıları ve negatif sayıları okuma
    fun parseFactor(): Double {

        if (position < input.length && input[position] == '-') {
            position++
            return -parseFactor()
        }

        if (position < input.length && input[position] == '+') {
            position++
            return parseFactor()
        }

        val start = position

        while (position < input.length &&
            (input[position].isDigit() || input[position] == '.')) {
            position++
        }

        require(start < position) { "Geçersiz sayı" }

        return input.substring(start, position).toDouble()
    }

    // Çarpma ve bölme
    fun parseTerm(): Double {

        var result = parseFactor()

        while (position < input.length) {

            val operator = input[position]

            if (operator != '*' && operator != '/') break

            position++

            val number = parseFactor()

            result = when (operator) {
                '*' -> result * number

                '/' -> {
                    require(number != 0.0) { "Sıfıra bölünemez" }
                    result / number
                }

                else -> result
            }
        }

        return result
    }

    // Toplama ve çıkarma
    fun parseExpression(): Double {

        var result = parseTerm()

        while (position < input.length) {

            val operator = input[position]

            if (operator != '+' && operator != '-') break

            position++

            val number = parseTerm()

            result = when (operator) {
                '+' -> result + number
                '-' -> result - number
                else -> result
            }
        }

        return result
    }

    val result = parseExpression()

    require(position == input.length) { "Geçersiz işlem" }
    require(result.isFinite()) { "Geçersiz sonuç" }

    return result
}
