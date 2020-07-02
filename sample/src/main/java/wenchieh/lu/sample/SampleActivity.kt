package wenchieh.lu.sample

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import wenchieh.lu.bottombar.BottomBar
import wenchieh.lu.bottombar.BottomTab
import wenchieh.lu.bottombar.sample.R
import wenchieh.lu.bottombar.sample.R.mipmap


class SampleActivity : AppCompatActivity() {

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sample)
    val bottomBar = findViewById<BottomBar>(R.id.bottomBar)
    val message = findViewById<TextView>(R.id.message)
    val xml = findViewById<TextView>(R.id.xml)

    val home = BottomTab.Builder(this).text("home")
        .iconNormal(-1)
        .iconSelected(mipmap.ic_home)
        .textColorNormal(color(android.R.color.black))
        .textColorSelected(color(android.R.color.holo_red_dark))
        .textSize(dp2px(15f))
        .badgeNumber(99)
        .tabPadding(dp2px(11F).toInt(), dp2px(11F).toInt())
        .badgeTextColor(Color.RED)
        .badgeBackgroundColor(Color.CYAN)
        .build()


    // only icon without text
    val find = BottomTab.Builder(this)
        .iconNormal(mipmap.ic_home_unselected)
        .iconSelected(mipmap.ic_home)
        .textColorNormal(color(android.R.color.black))
        .textColorSelected(color(android.R.color.holo_red_dark))
        .isShowPoint(true)
        .textSize(dp2px(15f))
        .tabPadding(dp2px(11F).toInt(), dp2px(15F).toInt())
//        .badgeGravity(Gravity.BOTTOM)
        .badgeWidth(dp2px(5F))
        .badgeBackgroundColor(Color.RED)
        .build()


    val profile = BottomTab.Builder(this).text("profile")
        .iconNormal(mipmap.ic_home_unselected)
        .iconSelected(mipmap.ic_home)
        .padding(dp2px(5f))
        .textSize(dp2px(15f))
        .textColorNormal(color(android.R.color.black))
        .textColorSelected(color(android.R.color.holo_red_dark))
        .build()



    bottomBar.addTab(home, profile)
    bottomBar.addTab(find)



    // listeners
    bottomBar.setOnSelectedListener { pre, cur ,isManual->
      message.text = (bottomBar.getChildAt(
          cur) as BottomTab).text + "\n prePosition = $pre, select curPosition = $cur isManual = $isManual"
    }

    bottomBar.setOnReSelectedListener {
      message.text = (bottomBar.getChildAt(it) as BottomTab).text + "\n reSelect curPosition = $it"
    }

    // default to select the no.0
    bottomBar.selectItem(0)

//    val newHomeTab = home.newBuilder()
//        .iconNormalBt(normalBt)
//        .iconSelectedBt(selectedBt).build()
//    bottomBar.update(0, newHomeTab)
  }

  private fun dp2px(value: Float) =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
          resources.displayMetrics)

}

fun Activity.color(color: Int) = ContextCompat.getColor(this, color)

