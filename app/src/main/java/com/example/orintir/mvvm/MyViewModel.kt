package com.example.orintir.mvvm

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {


    //Рисовка
    //Рисовка
    private fun createBitmapFromView(
        view: View,
        text: String,
        text2: String,
        text3: String,
        text4: String,
        text5: String,
        text6: String,
        userSelectedImage: Bitmap?
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Определяем максимальное количество символов в каждой строке
        val maxCharsPerLine = 28

        // Фио
        val paint = Paint().apply {
            color = Color.RED
            textSize = 23f
        }
        val x = 525f
        var y = 365f
        drawText(canvas, text, paint, x, y, maxCharsPerLine)

        // Город
        val paint2 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 30f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "Г.$text2", paint2, x, y, maxCharsPerLine)

        // Дата рождения
        val paint3 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 30f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "$text3 г.р.", paint3, x, y, maxCharsPerLine)

        // При каких обстоятельствах
        val paintObs = Paint().apply {
            color = Color.RED
            textSize = 20f
        }
        y += 30f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "При каких обстоятельствах пропал(а): ", paintObs, x, y, maxCharsPerLine)

        // Обстоятельства
        val paint4 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 40f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, text4, paint4, x, y, maxCharsPerLine)

        // Приметы основные
        val paintPrim = Paint().apply {
            color = Color.RED
            textSize = 20f
        }
        y += 30f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "Основные приметы: ", paintPrim, x, y, maxCharsPerLine)

        // Приметы
        val paint5 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 40f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, text5, paint5, x, y, maxCharsPerLine)

        // Одежда основная
        val paintCloth = Paint().apply {
            color = Color.RED
            textSize = 20f
        }
        y += 30f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "Одежда: ", paintCloth, x, y, maxCharsPerLine)

        // Одежда
        val paint6 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 40f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, text6, paint6, x, y, maxCharsPerLine)

        // Фото
        userSelectedImage?.let {
            val scaledBitmap = Bitmap.createScaledBitmap(it, 415, 506, false)
            canvas.drawBitmap(scaledBitmap, 104f, 352f, null)
        }

        return bitmap
    }

    private fun drawText(canvas: Canvas, text: String, paint: Paint, x: Float, y: Float, maxCharsPerLine: Int) {
        var remainingText = text
        var currentY = y
        while (remainingText.isNotEmpty()) {
            val charsToDraw = if (remainingText.length > maxCharsPerLine) {
                remainingText.substring(0, maxCharsPerLine)
            } else {
                remainingText
            }
            canvas.drawText(charsToDraw, x, currentY, paint)
            remainingText = remainingText.substring(charsToDraw.length)
            currentY += paint.textSize
        }
    }
}