package com.mao.sortletterlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @Describe
 * @anthor zhang
 * @time 2019/9/29 0029 16:37
 */
public class SortLetterView extends View {

    /**
     * 字母选中监听
     */
    public interface OnLetterChangedListener {
        void onChanged(String letter, int position);
    }

    public void setOnLetterChangedListener(OnLetterChangedListener letterChangedListener) {
        mLetterChangedListener = letterChangedListener;
    }


    // 字母默认颜色
    int mLetterColor = 0xff666666;
    // 被选中的字母颜色
    int mSelectLetterColor = 0xff666666;
    // 被选中的字母背景颜色
    int mSelectBackgroundColor = 0xffffece8;
    // 被选中的左边大字母颜色
    int mSelectbigtTextColor = 0xffffffff;
    // 字母默认字体大小
    float mLetterSize = Utils.dp2px(12);
    //  被选中的左边大字母大小
    float leftBigText = Utils.dp2px(26);
    //是否字母是居中显示
    boolean mIsLetterCenter;
    //右边间距
    float paddingRight = Utils.dp2px(12);
    //左边选中放大背景图片宽度
    float iconWidth = Utils.dp2px(58);
    //左边选中放大背景图片高度
    float iconHeight = Utils.dp2px(49);
    //左边放大图标和右边字母的距离
    float leftIconPadding = Utils.dp2px(9);
    //字母上下的距离
    float letterPadding = Utils.dp2px(5);
    //左边背景图片id
    int backgroundIconId;
    //左边放大字母偏移x
    float offSetX = Utils.dp2px(2);
    //模式选择
    private UiMode mode = UiMode.BOTH;
    private int mWidth;
    private int mHeight;
    private int mChoose = -1;// 选中的字母是第几个
    private Paint mLetterPaint;//字母画笔
    private Paint mChooseBackgroundPaint;//选中字母圆形背景画笔
    private Paint mChooseBigTextPaint;//选中字母放大画笔
    private TextView letterText;//显示当前字母的textview
    private String mLetters = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";//默认字符
    private OnLetterChangedListener mLetterChangedListener;// 触摸字母改变事件
    private Bitmap mBitmap;
    private Bitmap newBmp;
    //字母触摸选中宽度范围
    private float dispatchWidth;

    public enum UiMode {
        LEFT_TEXT, RIGHT_SHPAE, BOTH
    }


    public SortLetterView(Context context) {
        super(context);
        init(context, null);
    }

    public SortLetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SortLetterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable
                    .SortLetterView, 0, 0);
            mLetterSize = a.getDimension(R.styleable.SortLetterView_letterSize, mLetterSize);
            leftBigText = a.getDimension(R.styleable.SortLetterView_leftBigText, leftBigText);
            paddingRight = a.getDimension(R.styleable.SortLetterView_paddingRight, paddingRight);
            iconWidth = a.getDimension(R.styleable.SortLetterView_iconWidth, iconWidth);
            iconHeight = a.getDimension(R.styleable.SortLetterView_iconHeight, iconHeight);
            backgroundIconId = a.getResourceId(R.styleable.SortLetterView_backgroundIconId, 0);
            leftIconPadding = a.getDimension(R.styleable.SortLetterView_leftIconPadding, leftIconPadding);
            letterPadding = a.getDimension(R.styleable.SortLetterView_letterPadding, letterPadding);
            offSetX = a.getDimension(R.styleable.SortLetterView_offSetX, offSetX);
            mLetterColor = a.getColor(R.styleable.SortLetterView_letterColor, mLetterColor);
            mSelectLetterColor = a.getColor(R.styleable.SortLetterView_selectLetterColor, mSelectLetterColor);
            mSelectBackgroundColor = a.getColor(R.styleable.SortLetterView_selectBackgroundColor, mSelectBackgroundColor);
            mSelectbigtTextColor = a.getColor(R.styleable.SortLetterView_selectbigtTextColor, mSelectbigtTextColor);
            mIsLetterCenter = a.getBoolean(R.styleable.SortLetterView_isLetterCenter, mIsLetterCenter);
            a.recycle();
        }
        if (backgroundIconId == 0) {
            mBitmap = Utils.getBitmap(getContext(), R.mipmap.conner);
        } else {
            mBitmap = Utils.getBitmap(getContext(), backgroundIconId);
        }
        newBmp = Bitmap.createScaledBitmap(mBitmap, (int) iconWidth, (int) iconHeight, true);


        mLetterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLetterPaint.setTextSize(mLetterSize);
        mLetterPaint.setAntiAlias(true);


        mChooseBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChooseBackgroundPaint.setTypeface(Typeface.DEFAULT);
        mChooseBackgroundPaint.setAntiAlias(true);
        mChooseBackgroundPaint.setColor(mSelectBackgroundColor);

        mChooseBigTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChooseBigTextPaint.setTextSize(leftBigText);
        mChooseBigTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mChooseBigTextPaint.setAntiAlias(true);
        mChooseBigTextPaint.setColor(mSelectbigtTextColor);
        setClickable(true);

    }

    public void setLetters(String letters) {
        this.mLetters = letters;
        requestLayout();
        invalidate();
    }

    public void setLetterText(TextView letterText) {
        this.letterText = letterText;
    }

    public void setMode(UiMode mode) {
        this.mode = mode;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int i = (int) ((mLetterSize + letterPadding) * (mLetters.length()));
        setMeasuredDimension(widthMeasureSpec, (int) (i + letterPadding + iconHeight));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int i = (int) ((mLetterSize + letterPadding) * (mLetters.length()));
        mHeight = i;
        mWidth = w;
        dispatchWidth = mWidth - getPaddingRight() - mLetterSize - paddingRight - Utils.dp2px(14);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mLetters.length(); i++) {
            String letter = mLetters.substring(i, i + 1);
            float letterWidth = mLetterPaint.measureText(letter);
            float letterIconWidth = mChooseBigTextPaint.measureText(letter);
            mLetterPaint.setColor(i == mChoose ? mSelectLetterColor : mLetterColor);
            float xPos = mWidth - getPaddingRight() - letterWidth / 2 - paddingRight;
            float yPos = mHeight / mLetters.length() * (i + 1) + getPaddingTop() + iconHeight / 2;

            if (i == mChoose) {
                float radius = mLetterSize - 10;
                //选中字母圆形背景
                canvas.drawCircle(xPos + letterWidth / 2, yPos - radius / 2, radius, mChooseBackgroundPaint);
                if (mode == UiMode.RIGHT_SHPAE || mode == UiMode.BOTH) {
                    //左边放大字母背景
                    canvas.drawBitmap(newBmp, xPos - leftIconPadding - iconWidth + letterWidth / 2 - radius / 2, yPos - radius / 2 - iconHeight / 2, mLetterPaint);
                    //左边放大字母
                    canvas.drawText(letter, xPos - leftIconPadding - iconWidth / 2 + letterWidth / 2 - radius / 2 - letterIconWidth / 2 - offSetX,
                            yPos + letterPadding / 2, mChooseBigTextPaint);
                }
            }
            //所有右边边字母
            canvas.drawText(letter, xPos, yPos, mLetterPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x < dispatchWidth) {
            mChoose = -1;
            setTextGone();
            invalidate();
            return false;
        }
        int oldChoose = mChoose;
        if (y < getPaddingTop() || y > mHeight + getPaddingTop() + iconHeight) {
            mChoose = -1;
            setTextGone();
        } else {
            // 点击y坐标/(总高度*数组的长度)==点击选中的个数
            mChoose = (int) ((y - getPaddingTop() - iconHeight / 2) / (mHeight) * mLetters.length());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mChoose = -1;
            setTextGone();
        } else {
            try {
                if (oldChoose != mChoose && mChoose != -1) {
                    if (mLetterChangedListener != null) {
                        mLetterChangedListener.onChanged(mLetters.substring(mChoose,
                                mChoose + 1), mChoose);
                    }
                    if (letterText != null) {
                        if (mode == UiMode.LEFT_TEXT || mode == UiMode.BOTH) {
                            letterText.setText(mLetters.substring(mChoose, mChoose + 1));
                            letterText.setVisibility(View.VISIBLE);
                        } else {
                            setTextGone();
                        }
                    }
                }
            } catch (Exception e) {
                mChoose = -1;
                setTextGone();
                e.printStackTrace();
            }
        }
        invalidate();
        return super.dispatchTouchEvent(event);
    }

    private void setTextGone() {
        if (letterText != null) {
            letterText.setVisibility(View.INVISIBLE);
        }
    }
}
