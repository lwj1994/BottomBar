package wenchieh.lu.sample

import android.annotation.SuppressLint
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

    val tabs = Arrays.asList(
        BottomTab(this, position = 0, text = "首页", iconNormal = mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, mBadgeNumber = 8),
        BottomTab(this, position = 1, text = "发现", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, mBadgeNumber = 22),
        BottomTab(this, position = 2, iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, mBadgeNumber = 888),
        BottomTab(this, position = 3, text = "消息", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, isShowPoint = true),
        BottomTab(this, position = 4, text = "我的", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home))

    val array = tabs.toTypedArray()
    bottomBar.setupTab(dp2px(9f), Color.GRAY, Color.RED, *array)

    bottomBar.setOnSelectedListener { pre, cur ->
      message.text = (bottomBar.getChildAt(cur)as BottomTab).text +"\n prePosition = $pre, select curPosition = $cur"
    }

    bottomBar.setOnReSelectedListener {
      message.text = (bottomBar.getChildAt(it)as BottomTab).text +"\n select curPosition = $it"
    }


    bottomBar.select(0)

  }

  private fun dp2px(value: Float) =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
          resources.displayMetrics)

}
