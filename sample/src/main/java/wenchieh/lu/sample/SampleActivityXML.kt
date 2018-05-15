package wenchieh.lu.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import wenchieh.lu.bottombar.BottomBar
import wenchieh.lu.bottombar.BottomTab
import wenchieh.lu.bottombar.sample.R

class SampleActivityXML : AppCompatActivity() {

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sample_xml)
    val bottomBar = findViewById<BottomBar>(R.id.bottomBar)
    val message = findViewById<TextView>(R.id.message)


    bottomBar.setOnSelectedListener { pre, cur ->
      message.text = (bottomBar.getChildAt(cur)as BottomTab).text +"\n prePosition = $pre, select curPosition = $cur"
    }

    bottomBar.setOnReSelectedListener {
      message.text = (bottomBar.getChildAt(it)as BottomTab).text +"\n select curPosition = $it"
    }
    bottomBar.select(0)

  }

}
