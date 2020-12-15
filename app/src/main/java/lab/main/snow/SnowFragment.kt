package lab.main.snow

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import lab.main.R
import kotlin.random.Random

class SnowFragment(
	private val runOnUiThread: (action: Runnable) -> Unit
) :
	Fragment(
		R.layout.snow_fragment
	) {
	private val maxSnowSize = 8
	private val topOffset = 0
	private val bottomOffset = 0
	private val backgroundSnowColor = 0xFFABBDBA.toInt()
	private val foregroundSnowColor = 0xFFD7F4EF.toInt()

	private lateinit var movingThread1: MovingThread
	private lateinit var movingThread2: MovingThread
	private lateinit var foregroundPainter: SnowPainter
	private lateinit var backgroundPainter: SnowPainter
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val layout = view.findViewById<ConstraintLayout>(R.id.snowLayout)
		val background = view.findViewById<ImageView>(R.id.snowBackground)
		val foreground = view.findViewById<ImageView>(R.id.snowForeground)
		val restartButton = view.findViewById<Button>(R.id.snowRestartButton)

		foregroundPainter =
			SnowPainter(
				foreground,
				foregroundSnowColor,
				maxSnowSize,
				1,
				runOnUiThread
			)
		backgroundPainter =
			SnowPainter(
				background,
				backgroundSnowColor,
				maxSnowSize,
				2,
				runOnUiThread
			)

		fun createSnow(type: String, amount: Int): List<SnowParticle> {
			val particles = mutableListOf<SnowParticle>()

			val color = if(type == "Foreground")
				foregroundSnowColor
			else
				backgroundSnowColor

			for(i in 1..amount) {
				val size = Random.nextInt(1, maxSnowSize)
				val snowParticle = this.context?.let {
					SnowParticle(
						size, topOffset, bottomOffset, it, layout, color
					)
				}
				if(snowParticle != null) {
					particles.add(snowParticle)
				}
			}
			return particles.toList()
		}

		val foregroundSnow = createSnow("Foreground", 100)
		val backgroundSnow = createSnow("Background", 100)
		val wind = Wind()
		val speed = 500f

		movingThread1 =
			MovingThread(
				speed * 0.85f, wind, foregroundSnow, runOnUiThread
			)
		movingThread2 = MovingThread(
			speed, wind, backgroundSnow, runOnUiThread
		)

		fun restart() {
			movingThread1.restart()
			movingThread2.restart()
			foregroundPainter.restart()
			backgroundPainter.restart()
		}
		restart()

		restartButton.setOnClickListener {
			restart()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		movingThread2.stop()
		movingThread2.stop()
		foregroundPainter.stop()
		backgroundPainter.stop()
	}
}