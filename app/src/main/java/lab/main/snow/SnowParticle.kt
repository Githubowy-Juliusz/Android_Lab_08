package lab.main.snow

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class SnowParticle(
	size: Int,
	topOffset: Int,
	bottomOffset: Int,
	context: Context,
	layout: ConstraintLayout,
	color: Int
) {
	private var invisible = true
	val view: View
	val mover: RectMover
	val colorizer: RectColorizer


	init {
		view = ImageView(context)
		mover = RectMover(view, size, size, topOffset, bottomOffset)
		layout.addView(view)
		colorizer =
			RectColorizer(view, size, size, color)
		colorizer.paintInvisible()
	}

	fun paint() {
		if(invisible) {
			colorizer.paintColor()
			invisible = false
			return
		}
		val location = IntArray(2)
		location[0]

	}
}