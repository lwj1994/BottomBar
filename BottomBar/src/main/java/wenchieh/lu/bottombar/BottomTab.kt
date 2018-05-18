package wenchieh.lu.bottombar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.support.annotation.DrawableRes
import android.support.annotation.Px
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View

/**
 * @author Wenchieh.Lu  2018/5/8
 *
 * A child View for [BottomBar].
 * rewrite in kotlin from:
 *                    https://github.com/yingLanNull/AlphaTabsIndicator/blob/6ccff9e1a226755226559c61b593f1a41230813e/library/src/main/java/com/yinglan/alphatabs/BottomTab.java
 *
 */
class BottomTab @JvmOverloads constructor(context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, var text: String = "", var iconNormal: Int = 0,
    var iconSelected: Int = 0, var padding: Float = 0f, var textSize: Float = 0f,
    var textColorNormal: Int = Color.BLACK, var textColorSelected: Int = Color.RED,
    var badgeBackgroundColor: Int = Color.RED, private var badgeNumber: Int = 0,
    private var isShowPoint: Boolean = false, var iconNormalBt: Bitmap? = null,
    var iconSelectedBt: Bitmap? = null) : View(context, attrs,
    defStyleAttr) {

  private var mAlpha = 0
  private lateinit var mIconPaint: Paint
  private lateinit var mTextPaint: Paint
  private lateinit var mIconAvailableRect: Rect
  private lateinit var mIconDrawRect: Rect
  private lateinit var mTextBound: Rect


  private lateinit var badgeBgPaint: Paint
  private lateinit var badgeTextPaint: Paint
  private lateinit var badgeCanvas: Canvas
  private lateinit var badgeRF: RectF

  init {
    Log.d(TAG, "init")
    if (iconNormalBt == null) {
      iconNormalBt = if (iconNormal == 0) null else getDrawable(iconNormal)?.toBitmap()
    }
    if (iconSelectedBt == null) {
      iconSelectedBt = if (iconSelected == 0) null else getDrawable(
          iconSelected)?.toBitmap()
    }
    if (padding == 0f) padding = dp2px(5f)
    if (textSize == 0f) textSize = sp2px(12f)

    initAttrs(context, attrs, defStyleAttr)
    initDrawTools()
  }

  private fun initDrawTools() {
    mIconPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
          isFilterBitmap = true
          alpha = this@BottomTab.mAlpha
        }
    mIconAvailableRect = Rect()
    mIconDrawRect = Rect()
    mTextBound = Rect()
    mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
          textSize = this@BottomTab.textSize
          color = textColorNormal
          isDither = true
          getTextBounds(text, 0, text.length, mTextBound)
        }

    if (TextUtils.isEmpty(text)) {
      padding = 0f
    }
    badgeBgPaint = Paint().apply {
      color = badgeBackgroundColor
      isAntiAlias = true
    }
    badgeTextPaint = Paint().apply {
      color = Color.WHITE
      textSize = dp2px(textSize)
      isAntiAlias = true
      textAlign = Paint.Align.CENTER
      typeface = Typeface.DEFAULT_BOLD
    }
    badgeCanvas = Canvas()
    badgeRF = RectF()
  }

  /**
   * Initialize the Attrs
   * @param context The Context the view is running in, through which it can
   *        access the current theme, resources, etc.
   * @param attrs The attributes of the XML tag that is inflating the view.
   * @param defStyleAttr An attribute in the current theme that contains a
   *        reference to a style resource that supplies default values for
   *        the view. Can be 0 to not look for defaults.
   */
  private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
    val array = context.obtainStyledAttributes(attrs, R.styleable.BottomTab,
        defStyleAttr, 0)

    (0..array.indexCount).asSequence()
        .map {
          array.getIndex(it)
        }
        .forEach {
          when (it) {
            R.styleable.BottomTab_badgeBackgroundColor -> {
              badgeBackgroundColor = array.getColor(it, badgeBackgroundColor)
            }

            R.styleable.BottomTab_paddingTextWithIcon -> {
              padding = array.getDimension(it, padding)
            }

            R.styleable.BottomTab_tabIconNormal -> {
              iconNormalBt = (array.getDrawable(
                  it)?.toBitmap() ?: throw IllegalArgumentException(
                  "you must assign a tabIconNormal in BottomTab's xml or init it in code"))
            }

            R.styleable.BottomTab_tabIconSelected -> {
              iconSelectedBt = (array.getDrawable(
                  it)?.toBitmap() ?: throw IllegalArgumentException(
                  "you must assign a tabIconSelected in BottomTab's xml or init it in code"))
            }

            R.styleable.BottomTab_tabText -> {
              if (TextUtils.isEmpty(text))
                text = array.getString(it)
            }

            R.styleable.BottomTab_tabTextSize -> {
              textSize = array.getDimension(it, textSize)
            }

            R.styleable.BottomTab_textColorNormal -> {
              textColorNormal = array.getColor(it, textColorNormal)
            }

            R.styleable.BottomTab_textColorSelected -> {
              textColorSelected = array.getColor(it, textColorSelected)
            }

          }
        }
    array.recycle()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    Log.d(TAG, "onMeasure")

    val availableWidth = measuredWidth - paddingLeft - paddingRight
    val availableHeight: Int = (measuredHeight - paddingTop - paddingBottom - mTextBound.height() - padding).toInt()


    mIconAvailableRect.set(paddingLeft, paddingTop, paddingLeft + availableWidth,
        paddingTop + availableHeight)

    val textLeft = paddingLeft + (availableWidth - mTextBound.width()) / 2
    val textTop = mIconAvailableRect.bottom + padding.toInt()
    mTextBound.set(textLeft, textTop, textLeft + mTextBound.width(),
        textTop + mTextBound.height())

  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    Log.d(TAG, "onLayout")
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    // draw icon
    mIconAvailableRect.availableToDrawRect(
        iconNormalBt ?: throw IllegalArgumentException("you must set iconNormal"))
    mIconPaint.alpha = 255 - mAlpha
    canvas.drawBitmap(iconNormalBt, null, mIconAvailableRect, mIconPaint)

    mIconAvailableRect.availableToDrawRect(
        iconSelectedBt ?: throw IllegalArgumentException("you must set iconSelected"))
    mIconPaint.alpha = mAlpha
    canvas.drawBitmap(iconSelectedBt, null, mIconAvailableRect, mIconPaint)

    // draw text
    // text's real height  = mTextBound.height() + mFmi.bottom
    if (!TextUtils.isEmpty(text)) {
      mTextPaint.apply {
        color = textColorNormal
        alpha = 255 - mAlpha
      }
      canvas.drawText(text, mTextBound.left.toFloat(),
          (mTextBound.bottom - mTextPaint.fontMetricsInt.bottom / 2).toFloat(), mTextPaint)

      mTextPaint.apply {
        color = textColorSelected
        alpha = mAlpha
      }
      canvas.drawText(text, mTextBound.left.toFloat(),
          (mTextBound.bottom - mTextPaint.fontMetricsInt.bottom / 2).toFloat(), mTextPaint)
    }

    // draw badge
    drawBadge(canvas)
  }

  /**
   * draw badge
   */
  private fun drawBadge(canvas: Canvas) {
    var i = measuredWidth / 14
    val j = measuredHeight / 9
    i = if (i >= j) j else i

    // if showPoint, don't show number
    val left = measuredWidth / 10 * 6f
    val top = dp2px(0.toFloat())
    if (isShowPoint) {
      i = if (i > 10) 10 else i
      val width = dp2px(i.toFloat())
      badgeRF.set(left, top, left + width, top + width)
      canvas.drawOval(badgeRF, badgeBgPaint)
      return
    }

    if (badgeNumber > 0) {
      badgeTextPaint.textSize = dp2px(if (i / 1.5f == 0f) 5f else i / 1.5f)

      val number = if (badgeNumber > 99) "99+" else badgeNumber.toString()
      val width: Int
      val height = dp2px(i.toFloat()).toInt()
      val bitmap: Bitmap
      when {
        number.length == 1 -> {
          width = dp2px(i.toFloat()).toInt()
          bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        number.length == 2 -> {
          width = dp2px((i + 5).toFloat()).toInt()
          bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        else -> {
          width = dp2px((i + 8).toFloat()).toInt()
          bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        }
      }

      badgeRF.set(0f, 0f, width.toFloat(), height.toFloat())
      badgeCanvas.apply {
        setBitmap(bitmap)
        drawRoundRect(badgeRF, 50f, 50f, badgeBgPaint) //画椭圆
      }

      val fontMetrics = badgeTextPaint.fontMetrics
      val x = width / 2f
      val y = height / 2f - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2

      badgeCanvas.drawText(number, x, y, badgeTextPaint)
      canvas.drawBitmap(bitmap, left, top, null)
      bitmap.recycle()
    }
  }


  private fun Rect.availableToDrawRect(bitmap: Bitmap) {
    var dx = 0f
    var dy = 0f
    val wRatio = width() * 1.0f / bitmap.width
    val hRatio = height() * 1.0f / bitmap.height
    if (wRatio > hRatio) {
      dx = (width() - hRatio * bitmap.width) / 2
    } else {
      dy = (height() - wRatio * bitmap.height) / 2
    }
    val left = (left.toFloat() + dx + 0.5f).toInt()
    val top = (top.toFloat() + dy + 0.5f).toInt()
    val right = (right - dx + 0.5f).toInt()
    val bottom = (bottom - dy + 0.5f).toInt()
    set(left, top, right, bottom)
  }

  fun showBadgePoint(show: Boolean) {
    isShowPoint = show
    postInvalidate()
  }

  fun clearBadge() {
    isShowPoint = false
    badgeNumber = -1
    postInvalidate()
  }

  fun showBadgeNumber(num: Int) {
    if (num <= 0) {
      throw IllegalArgumentException("num must > 0")
    }
    badgeNumber = num
    isShowPoint = false
    postInvalidate()
  }

  override fun setSelected(selected: Boolean) {
    super.setSelected(selected)
    mAlpha = if (selected) 255 else 0
    invalidate()
  }

  private fun dp2px(value: Float) =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)


  private fun sp2px(value: Float) =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)

  override fun onSaveInstanceState(): Parcelable {
    Log.d(TAG, "onSaveInstanceState")
    return SavedState(super.onSaveInstanceState()).apply {
      text = this@BottomTab.text
      padding = this@BottomTab.padding
      textSize = this@BottomTab.textSize
      iconNormal = this@BottomTab.iconNormal
      iconSelected = this@BottomTab.iconSelected
      isSelected = this@BottomTab.isSelected
    }
  }


  override fun onRestoreInstanceState(state: Parcelable?) {
    Log.d(TAG, "onRestoreInstanceState")
    if (state == null || state !is SavedState) {
      super.onRestoreInstanceState(state)
      return
    }
    super.onRestoreInstanceState(state.superState)
    restoreState(state)
  }

  private fun restoreState(state: SavedState) {
    Log.d(TAG, state.text)
    this.text = state.text
    this.padding = state.padding
    this.iconNormal = state.iconNormal
    this.iconSelected = state.iconSelected
    this.textSize = state.textSize
    requestLayout()
    isSelected = state.isSelected
  }

  private class SavedState : BaseSavedState {
    lateinit var text: String
    var padding = 0f
    var iconNormal = 0
    var iconSelected = 0
    var textSize = 0f
    var isSelected = false

    constructor(superState: Parcelable) : super(superState)

    constructor(parcel: Parcel) : super(parcel) {
      text = parcel.readString()
      padding = parcel.readFloat()
      iconNormal = parcel.readInt()
      iconSelected = parcel.readInt()
      textSize = parcel.readFloat()
      isSelected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      super.writeToParcel(parcel, flags)
      parcel.writeString(text)
      parcel.writeFloat(padding)
      parcel.writeInt(iconNormal)
      parcel.writeInt(iconSelected)
      parcel.writeFloat(textSize)
      parcel.writeByte(if (isSelected) 1 else 0)
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

  companion object {
    private const val TAG = "BottomTab"
  }

  private fun Drawable.toBitmap(
      @Px width: Int = intrinsicWidth,
      @Px height: Int = intrinsicHeight,
      config: Config? = null
  ): Bitmap {
    if (this is BitmapDrawable) {
      if (config == null || bitmap.config == config) {
        // Fast-path to return original. Bitmap.createScaledBitmap will do this check, but it
        // involves allocation and two jumps into native code so we perform the check ourselves.
        if (width == intrinsicWidth && height == intrinsicHeight) {
          return bitmap
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
      }
    }

    val oldLeft = bounds.left
    val oldTop = bounds.top
    val oldRight = bounds.right
    val oldBottom = bounds.bottom


    val bitmap = Bitmap.createBitmap(width, height, config ?: Config.ARGB_8888)
    setBounds(0, 0, width, height)
    draw(Canvas(bitmap))

    setBounds(oldLeft, oldTop, oldRight, oldBottom)
    return bitmap
  }


  private fun getDrawable(@DrawableRes drawableRes: Int): Drawable? =
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        context.getDrawable(drawableRes)
      } else {
        context.resources.getDrawable(drawableRes)
      }


  class Builder(private val context: Context) {
    private var text: String = ""
    private var textSize = 12f
    private var iconNormal: Int = 0
    private var iconSelected: Int = 0
    private var iconNormalBt: Bitmap? = null
    private var iconSelectedBt: Bitmap? = null
    private var textColorNormal = 0
    private var textColorSelected = 0
    private var padding = 0f
    private var badgeBackgroundColor = 0
    private var badgeNumber = 0
    private var isShowPoint = false


    fun text(text: String) =
        apply {
          this.text = text
        }


    fun textSize(textSize: Float) =
        apply {
          this.textSize = textSize
        }


    fun iconNormal(iconNormal: Int) =
        apply {
          this.iconNormal = iconNormal
        }


    fun iconSelected(iconSelected: Int) =
        apply {
          this.iconSelected = iconSelected
        }


    fun iconNormalBt(iconNormalBt: Bitmap) =
        apply {
          this.iconNormalBt = iconNormalBt
        }


    fun iconSelectedBt(iconSelectedBt: Bitmap) =
        apply {
          this.iconSelectedBt = iconSelectedBt
        }


    fun textColorNormal(textColorNormal: Int) =
        apply {
          this.textColorNormal = textColorNormal
        }


    fun textColorSelected(textColorSelected: Int) =
        apply {
          this.textColorSelected = textColorSelected
        }


    fun padding(padding: Float) =
        apply {
          this.padding = padding
        }

    fun badgeBackgroundColor(badgeBackgroundColor: Int) =
        apply {
          this.badgeBackgroundColor = badgeBackgroundColor
        }


    fun badgeNumber(badgeNumber: Int) =
        apply {
          this.badgeNumber = badgeNumber
        }


    fun isShowPoint(isShowPoint: Boolean) =
        apply {
          this.isShowPoint = isShowPoint
        }


    fun build() = BottomTab(context, iconNormal = iconNormal
        , iconSelected = iconSelected, iconNormalBt = iconNormalBt,
        iconSelectedBt = iconSelectedBt)
        .apply {
          text = this@Builder.text
          textSize = this@Builder.textSize
          textColorNormal = this@Builder.textColorNormal
          textColorSelected = this@Builder.textColorSelected
          padding = this@Builder.padding
          badgeBackgroundColor = this@Builder.badgeBackgroundColor
          badgeNumber = this@Builder.badgeNumber
          isShowPoint = this@Builder.isShowPoint
        }
  }
}

