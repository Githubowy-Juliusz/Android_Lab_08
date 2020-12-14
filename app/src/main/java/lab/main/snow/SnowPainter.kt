package lab.main.snow

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.widget.ImageView
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class SnowPainter(
	view: ImageView, color: Int, size: Int, private val speed: Int,
	private val runOnUiThread: (action: Runnable) -> Unit
) {
	private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
	private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
	private val bitmap =
		Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
	private val canvas = Canvas(bitmap)
	private val paint = Paint()
	private var snowHeight = Array(screenWidth) { 0 }
	private val thread = Thread(Runnable { paintingLoop() })
	private val token: AtomicBoolean = AtomicBoolean(true)

	init {
		view.background = BitmapDrawable(bitmap)
		paint.color = color
		paint.strokeWidth = size.toFloat()
	}

	fun restart() {
		if(thread.isAlive) {
			token.set(false)
			thread.join()
		}
		snowHeight = Array(screenWidth) { 0 }
		bitmap.eraseColor(Color.TRANSPARENT)
		token.set(true)
		thread.start()
	}

	fun stop() {
		token.set(false)
		thread.join()
	}

	private fun paintingLoop() {
		Thread.sleep(4000)
		while(token.get()) {
			val x = Random.nextFloat() * screenWidth
			val y = screenHeight.toFloat() - snowHeight[x.toInt()]
			drawOval(x.toInt(), y.toInt())
			snowHeight[x.toInt()] += 2
			Thread.sleep((16 / speed).toLong())
		}
	}

	private fun drawOval(x: Int, y: Int) {
		val oval = ShapeDrawable(OvalShape())
		val left = x - 100
		val top = y
		val right = x + 100
		val bottom = y + 100
		oval.setBounds(left, top, right, bottom)
		oval.paint.color = paint.color
		runOnUiThread(Runnable { oval.draw(canvas) })
	}
}