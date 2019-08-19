package wenchieh.lu.bottombar

interface IBottomTab {

  fun isSelected():Boolean
  /**
   * 是否选中
   */
  fun setSelected(selected: Boolean)

  /**
   * 是否显示通知小圆点
   */
  fun showBadgePoint(show: Boolean)
}