package lab.main.snow

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import lab.main.R

class SnowFragment(
	private val runOnUiThread: (action: Runnable) -> Unit
) :
	Fragment(
		R.layout.snow_fragment
	) {
	private val snowSize = 5

	private val topOffset = 0
	private val bottomOffset = 0

	//	private val topOffset = 195
//	private val bottomOffset = 220
	private val backgroundSnowColor = 0xFFABBDBA.toInt()
	private val foregroundSnowColor = 0xFFD7F4EF.toInt()

	private lateinit var movingThread1: MovingThread
	private lateinit var movingThread2: MovingThread
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val layout = view.findViewById<ConstraintLayout>(R.id.snowLayout)
		val background = view.findViewById<ImageView>(R.id.snowBackground)
		val foreground = view.findViewById<ImageView>(R.id.snowForeground)
		val restartButton = view.findViewById<Button>(R.id.snowRestartButton)

		fun createSnow(type: String, amount: Int): List<SnowParticle> {
			val particles = mutableListOf<SnowParticle>()

			val color = if(type == "Foreground")
				foregroundSnowColor
			else
				backgroundSnowColor

			for(i in 1..amount) {
				val snowParticle = this.context?.let {
					SnowParticle(
						snowSize, topOffset, bottomOffset, it, layout, color
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

		movingThread1.restart()
		movingThread2.restart()

		restartButton.setOnClickListener {
			movingThread1.restart()
			movingThread2.restart()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		movingThread2.stop()
		movingThread2.stop()
	}
}