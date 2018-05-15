package wenchieh.lu.bottombar

import android.content.Context
import android.graphics.Color
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

  private var mCurrentPosition = -1
  private var mPreviousPosition = -1

  init {
    orientation = HORIZONTAL
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
  fun setupTab(padding: Float, textColorNormal: Int = Color.GRAY, textColorSelected: Int = Color.DKGRAY,
      vararg tabs: BottomTab) {
    if (tabs.isEmpty()) return
    for (i in tabs.indices) {
      addView(tabs[i].apply {
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).apply {
          weight = 1f
        }
        this.padding = padding
        this.textColorNormal = textColorNormal
        this.textColorSelected = textColorSelected
        setOnClickListener {
          triggerListener(tabs, i)
        }
        id = if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
          View.generateViewId()
        } else {
          generateViewIdV16()
        }
      })

    }
  }

  /**
   * change positions and trigger listener
   * @param nextPosition the next position to go
   */
  private fun triggerListener(tabs: Array<out BottomTab>, toIndex: Int) {

    if (mCurrentPosition == tabs[toIndex].position && mPreviousPosition != -1) {
      onReSelectedListener(mCurrentPosition)
      return
    }
    onSelected(toIndex, mCurrentPosition, tabs[toIndex].position, onSelectedListener)
    mPreviousPosition = mCurrentPosition
    mCurrentPosition = tabs[toIndex].position
  }

  private fun onSelected(selected: Int, prePosition: Int, position: Int,
      onSelectedListener: (prePosition: Int, position: Int) -> Unit) {

    for (i in 0 until childCount) {
      getChildAt(i).isSelected = i == selected
    }

    onSelectedListener(prePosition, position)
  }

  /**
   * save some state
   */
  override fun onSaveInstanceState(): Parcelable {
    return SavedState(super.onSaveInstanceState()).apply {
      prePosition = mPreviousPosition
      curPosition = mCurrentPosition
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
    mPreviousPosition = savedState.prePosition
    mCurrentPosition = savedState.curPosition
    post {
      onSelected(mCurrentPosition, mPreviousPosition, mCurrentPosition, onSelectedListener)
    }
  }

  private class SavedState : BaseSavedState {
    var prePosition = -1
    var curPosition = 0


    constructor(superState: Parcelable) : super(superState)

    private constructor(parcel: Parcel) : super(parcel) {
      prePosition = parcel.readInt()
      curPosition = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      super.writeToParcel(parcel, flags)
      parcel.writeInt(prePosition)
      parcel.writeInt(curPosition)
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
