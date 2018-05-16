package wenchieh.lu.bottombar

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Wenchieh.Lu  2018/5/9
 *
 */
class BottomBar @JvmOverloads constructor(context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var onSelectedListener: (prePosition: Int, position: Int) -> Unit = { _, _ -> },
    private var onReSelectedListener: (position: Int) -> Unit = { }
) : LinearLayout(context, attrs, defStyleAttr) {

  private var mCurrentIndex = -1
  private var mPreviousIndex = -1
  private var isSetup = false

  init {
    orientation = HORIZONTAL
    post {
      if (!isSetup)
        setupTab()
    }
  }

  /**
   * set a listener to trigger something when bottomTab is selected
   * @param prePosition the position of selected button previously
   * @param position the position of selected button just now
   */
  fun setOnSelectedListener(onSelectedListener: (prePosition: Int, position: Int) -> Unit) {
    this.onSelectedListener = onSelectedListener
  }

  /**
   * set a listener to trigger something when bottomTab is reSelected
   * @param position the position of reSelected button just now
   */
  fun setOnReSelectedListener(
      onReSelectedListener: (position: Int) -> Unit) {
    this.onReSelectedListener = onReSelectedListener
  }

  /**
   * init BottomTab
   */
  fun setupTab(padding: Float = 0f, textColorNormal: Int = 0, textColorSelected: Int = 0,
      vararg tabs: BottomTab) {
    fun applyView(view: BottomTab?) =
        view?.apply {
          layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            weight = 1f
          }
          if (padding != 0f)
            this.padding = padding
          if (textColorNormal != 0)
            this.textColorNormal = textColorNormal
          if (textColorSelected != 0)
            this.textColorSelected = textColorSelected

          if (id == View.NO_ID)
            id = if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
              View.generateViewId()
            } else {
              generateViewIdV16()
            }
        }

    // add view in xml
    if (tabs.isEmpty()) {
      for (i in 0 until childCount) {
        val view = getChildAt(i) as? BottomTab ?: return
        applyView(view)
        view.setOnClickListener {
          triggerListener(i)
        }
      }
    }
    // add view in code
    else {
      for (i in tabs.indices) {
        addView(applyView(tabs[i]))
        tabs[i].setOnClickListener {
          triggerListener(tabs, i)
        }
      }
    }
    isSetup = true
  }

  /**
   * change positions and trigger listener
   * @param nextPosition the next position to go
   */
  private fun triggerListener(tabs: Array<out BottomTab>, toIndex: Int) {
    if (tabs.isEmpty()) return
    if (mCurrentIndex == toIndex && mPreviousIndex != -1) {
      onReSelectedListener(mCurrentIndex)
      return
    }
    onSelected( mCurrentIndex, toIndex, onSelectedListener)
    mPreviousIndex = mCurrentIndex
    mCurrentIndex = toIndex
  }

  /**
   * change positions and trigger listener
   * @param nextPosition the next position to go
   */
  private fun triggerListener(toIndex: Int) {
    if (childCount == 0) return
    if (mCurrentIndex == toIndex && mPreviousIndex != -1) {
      onReSelectedListener(mCurrentIndex)
      return
    }
    onSelected(mCurrentIndex, toIndex, onSelectedListener)
    mPreviousIndex = mCurrentIndex
    mCurrentIndex = toIndex
  }

  private fun onSelected(preIndex: Int, curIndex: Int,
      onSelectedListener: (prePosition: Int, position: Int) -> Unit) {

    for (i in 0 until childCount) {
      getChildAt(i).isSelected = i == curIndex
    }

    onSelectedListener(preIndex, curIndex)
  }

  /**
   * save some state
   */
  override fun onSaveInstanceState(): Parcelable {
    return SavedState(super.onSaveInstanceState()).apply {
      preIndex = mPreviousIndex
      curIndex = mCurrentIndex
    }
  }

  /**
   * restore some state
   */
  override fun onRestoreInstanceState(state: Parcelable?) {
    if (state == null || state !is SavedState) {
      super.onRestoreInstanceState(state)
      return
    }
    super.onRestoreInstanceState(state.superState)
    restoreState(state)
  }

  private fun restoreState(savedState: SavedState) {
    mPreviousIndex = savedState.preIndex
    mCurrentIndex = savedState.curIndex
    post {
      onSelected(mPreviousIndex, mCurrentIndex, onSelectedListener)
    }
  }

  private class SavedState : BaseSavedState {
    var preIndex = -1
    var curIndex = 0


    constructor(superState: Parcelable) : super(superState)

    private constructor(parcel: Parcel) : super(parcel) {
      preIndex = parcel.readInt()
      curIndex = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      super.writeToParcel(parcel, flags)
      parcel.writeInt(preIndex)
      parcel.writeInt(curIndex)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Creator<SavedState> {
      override fun createFromParcel(parcel: Parcel): SavedState {
        return SavedState(parcel)
      }

      override fun newArray(size: Int): Array<SavedState?> {
        return arrayOfNulls(size)
      }
    }
  }

  fun select(index: Int) {
    post {
      getChildAt(index)?.performClick()
    }
  }

  private val sNextGeneratedId = AtomicInteger(1)
  private fun generateViewIdV16(): Int {
    while (true) {
      val result = sNextGeneratedId.get()
      // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
      var newValue = result + 1
      if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
      if (sNextGeneratedId.compareAndSet(result, newValue)) {
        return result
      }
    }
  }

  companion object {
    private const val TAG = "BottomBar"
  }
}
