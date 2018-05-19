package wenchieh.lu.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.widget.TextView
import wenchieh.lu.bottombar.BottomBar
import wenchieh.lu.bottombar.BottomTab
import wenchieh.lu.bottombar.sample.R
import wenchieh.lu.bottombar.sample.R.mipmap
import java.util.Arrays

class SampleActivity : AppCompatActivity() {

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sample)
    val bottomBar = findViewById<BottomBar>(R.id.bottomBar)
    val message = findViewById<TextView>(R.id.message)
    val xml = findViewById<TextView>(R.id.xml)

    xml.setOnClickListener {
      startActivity(Intent(this@SampleActivity, SampleActivityXML::class.java))
    }

    val home = BottomTab.Builder(this).text("首页")
        .iconNormal(mipmap.ic_home_unselected)
        .iconSelected(mipmap.ic_home)
        .padding(500f)
        .badgeNumber(8)
        .build()

    val tabs = Arrays.asList(home,
        BottomTab(this, text = "发现", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, badgeNumber = 22),
        BottomTab(this, iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, badgeNumber = 888),
        BottomTab(this, text = "消息", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, isShowPoint = true),
        BottomTab(this, text = "我的", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home))

    val array = tabs.toTypedArray()
    bottomBar.setupTab(dp2px(9f), Color.GRAY, Color.RED, *array)

    bottomBar.setOnSelectedListener { pre, cur ->
      message.text = (bottomBar.getChildAt(
          cur) as BottomTab).text + "\n prePosition = $pre, select curPosition = $cur"
    }

    bottomBar.setOnReSelectedListener {
      message.text = (bottomBar.getChildAt(it) as BottomTab).text + "\n reSelect curPosition = $it"
    }


    bottomBar.select(0)

  }

  private fun dp2px(value: Float) =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
          resources.displayMetrics)

}
