package lab.main.snow

import android.content.res.Resources
import android.graphics.PointF
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class MovingThread(
	private val speed: Float,
	private val wind: Wind,
	private val snowParticles: List<SnowParticle>,
	private val runOnUiThread: (action: Runnable) -> Unit
) {
	private val thread = Thread(Runnable {
		threadLoop()
	})
	private val token: AtomicBoolean = AtomicBoolean(true)
	private val frameTime = 16L
	private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
	private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
	fun restart() {
		if(thread.isAlive) {
			token.set(false)
			thread.join()
		}
		token.set(true)
		thread.start()
	}

	fun stop() {
		token.set(false)
		thread.join()
	}

	private fun threadLoop() {
		val location = IntArray(2)
		val topOffset = snowParticles[0].mover.topOffset
		val bottomOffset = snowParticles[0].mover.bottomOffset
		val positions = List<PointF>(snowParticles.size) { index: Int ->
			val x = Random.nextInt(screenWidth)
			val y = Random.nextInt(topOffset, screenHeight - bottomOffset)
			runOnUiThread(Runnable {
				snowParticles[index].paintInivisible()
				snowParticles[index].mover.move(x, y)
			})
			PointF(x.toFloat(), y.toFloat())
		}

		var last = System.currentTimeMillis()
		while(token.get()) {
			val xmovement = Random.nextFloat() + 0.2f
			val now = System.currentTimeMillis()
			val xsign = wind.wind()

			snowParticles.forEachIndexed { index, snowParticle ->
				if(!token.get())
					return
				positions[index].x += speed * ((now - last) / 1000f) * xmovement * xsign
				positions[index].y += speed * ((now - last) / 1000f)
				val dx =
					(positions[index].x + snowParticle.mover.rectWidth / 2f).toInt()
				val dy =
					(positions[index].y + snowParticle.mover.rectHeight / 2f).toInt()
				runOnUiThread(Runnable { snowParticle.mover.move(dx, dy) })
			}
			last = now
			val difference = System.currentTimeMillis() - now
			if(difference < frameTime)
				Thread.sleep(difference)

			snowParticles.forEachIndexed { index, snowParticle ->
				if(!token.get())
					return
				if(snowParticle.mover.xCollision) {
					snowParticle.view.getLocationOnScreen(location)
					if(location[0].toFloat() > screenWidth / 2)
						positions[index].x = 0f
					else
						positions[index].x =
							screenWidth - snowParticle.mover.rectWidth.toFloat()
				}
				if(snowParticle.mover.yCollision) {
					positions[index].x = Random.nextInt(screenWidth).toFloat()
					positions[index].y =
						snowParticle.mover.topOffset.toFloat() + snowParticle.mover.rectHeight
					+Random.nextInt(0, 20)
					runOnUiThread(Runnable { snowParticle.paint() })
				}
			}
		}
	}
}