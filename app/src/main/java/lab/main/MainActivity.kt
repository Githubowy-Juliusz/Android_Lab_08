package lab.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lab.main.snow.SnowFragment

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val snowFragment = SnowFragment(::runOnUiThread)
		
		supportFragmentManager.beginTransaction().apply {
			replace(R.id.frameLayout, snowFragment)
			commit()
		}
	}
}