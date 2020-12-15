package lab.main.snow

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView

class RectColorizer(
	imageView: ImageView,
	rectWidth: Int,
	rectHeight: Int,
	private val color: Int
) {
	private val bitmap =
		Bitmap.createBitmap(rectWidth, rectHeight, Bitmap.Config.ARGB_8888)
	private val canvas = Canvas(bitmap)
	private val paint = Paint()

	init {
		imageView.background = BitmapDrawable(bitmap)
		paintInvisible()
	}

	fun paintInvisible() {
		bitmap.eraseColor(0)
	}

	fun paintColor() {
		paint.color = color
		canvas.drawPaint(paint)
	}
}