package lab.main.snow

import java.lang.Math.sin

class Wind {
	private var time = System.currentTimeMillis()
	fun wind(): Float {
		val diff = (System.currentTimeMillis() - time) / 1000.toDouble()
		return sin(diff).toFloat()
	}
}