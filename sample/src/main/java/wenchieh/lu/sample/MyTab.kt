package wenchieh.lu.sample

import android.content.Context
import android.view.View
import wenchieh.lu.bottombar.IBottomTab

/**
 * @author luwenjie on 2019-08-19 16:50:11
 */
class MyTab(context: Context):View(context),IBottomTab {
  override fun showBadgePoint(show: Boolean) {
  }


  companion object {
    private const val TAG = "MyTab"
  }
}