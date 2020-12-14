package lab.main.snow

import android.content.res.Resources
import android.view.View


class RectMover(
	val view: View,
	val rectWidth: Int,
	val rectHeight: Int,
	val topOffset: Int,
	val bottomOffset: Int
) {
	val xCollision: Boolean
		get() {
			if(_xCollision) {
				val tmp = _xCollision
				_xCollision = false
				return tmp
			}
			return _xCollision
		}
	val yCollision: Boolean
		get() {
			if(_yCollision) {
				val tmp = _yCollision
				_yCollision = false
				return tmp
			}
			return _yCollision
		}
	private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
	private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

	private var _xCollision = false
	private var _yCollision = false
	fun move(x: Int, y: Int) {
		var l = x - rectWidth / 2
		var t = y - topOffset - rectHeight / 2
		var r = l + rectWidth
		var b = t + rectHeight
		if(t < 0) {
			t = 0
			b = rectHeight
			_yCollision = true
		}
		if(r > screenWidth) {
			r = screenWidth
			l = r - rectWidth
			_xCollision = true
		}
		if(l < 0) {
			l = 0
			r = rectWidth
			_xCollision = true
		}
		if(b > screenHeight - bottomOffset) {
			b = screenHeight - bottomOffset
			t = b - rectHeight
			_yCollision = true
		}
		view.layout(l, t, r, b)
	}
}