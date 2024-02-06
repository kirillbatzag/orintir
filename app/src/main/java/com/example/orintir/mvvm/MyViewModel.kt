package com.example.orintir.mvvm

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {


    //Рисовка
    fun createBitmapFromView(
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

        // Фио
        val paint = android.graphics.Paint()
        paint.color = Color.YELLOW
        paint.textSize = 23f
        val x = 150f
        val y = 600f
        canvas.drawText(text, x ,y, paint)

        //Город
        val paint2 = android.graphics.Paint()
        paint2.color = Color.BLACK
        paint2.textSize = 20f
        val x2 = 155f
        val y2 = 90f
        canvas.drawText("$text2,", x2,  y2, paint2)

        // Дата рождения
        val paint3 = android.graphics.Paint()
        paint3.color = Color.BLACK
        paint3.textSize = 20f
        val x3 = 255f
        val y3 = 90f
        canvas.drawText("$text3 г.р.", x3,  y3, paint3)

        // При каких осбст.
        val paintObs = android.graphics.Paint()
        paintObs.color = Color.RED
        paintObs.textSize = 15f
        val xO = 50f
        val yO = 200f
        canvas.drawText("При каких обстоятельствах пропал(а): ", xO,  yO, paintObs)

        // Обстоятельства
        val paint4 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x4 = 50f
        val y4 = 215f
        canvas.drawText(text4, x4,  y4, paint4)

        //Приметы осн.
        val paintPrim = android.graphics.Paint()
        paintPrim.color = Color.RED
        paintPrim.textSize = 15f
        val xP = 50f
        val yP = 240f
        canvas.drawText("Основные приметы: ", xP, yP, paintPrim)

        //Приметы
        val paint5 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x5 = 50f
        val y5 = 255f
        canvas.drawText(text5, x5,  y5, paint5)

        //Одежда осн.
        val paintCloth = android.graphics.Paint()
        paintCloth.color = Color.RED
        paintCloth.textSize = 15f
        val xC = 50f
        val yC = 280f
        canvas.drawText("Одежда: ", xC, yC, paintCloth)

        //Одежда
        val paint6 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x6 = 50f
        val y6 = 295f
        canvas.drawText(text6, x6,  y6, paint6)

        //Текст
        val paintText = android.graphics.Paint()
        paintText.color = Color.BLACK
        paintText.textSize = 20f
        val xT = 27f
        val yT = 350f
        canvas.drawText("Просим сообщить любую информацию по тел: ", xT, yT, paintText)

        //Номера
        val paintNumber = android.graphics.Paint()
        paintNumber.color = Color.BLACK
        paintNumber.textSize = 20f
        val xN = 130f
        val yN = 375f
        canvas.drawText("8(924)991-11-62 или 112", xN, yN, paintNumber)

        //Фото
        userSelectedImage?.let {
            val scaledBitmap = Bitmap.createScaledBitmap(it, 127, 147, false)
            canvas.drawBitmap(scaledBitmap, 20f, 19f, null)
        }

        return bitmap
    }
}