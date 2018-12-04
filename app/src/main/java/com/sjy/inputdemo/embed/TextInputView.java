package com.sjy.inputdemo.embed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjy.inputdemo.R;



/**
 * 验证码 嵌入式版：封装的是TextView，避免调起手机的输入法，从而可以使用别的外键输入内容。
 * sjy 2018-1204
 */

public class TextInputView extends LinearLayout implements TextWatcher {

    private Context mContext;
    private OnInputFinishListener onCodeFinishListener;
    private LinearLayout layout2;
    private long endTime = 0;

    /**
     * 输入框数量
     */
    private int inputNumber;

    private int defaultInputNumber = 6;

    /**
     * 输入框类型
     */
    private VCInputType inputType;

    /**
     * 输入框的宽度
     */
    private int inputWidth;

    /**
     * 默认尺寸
     */
    private int defaultInputWidth = 88;

    /**
     * 输入框的宽度
     */
    private int inputHeight;

    /**
     * 默认尺寸
     */
    private int defaultInputHeight = 104;

    /**
     * 文字颜色
     */
    private int inputTextColor;

    /**
     * 文字大小
     */
    private float inputTextSize;
    private int defaultInputTextSize = 22;

    /**
     * 输入框背景
     */
    private int inputBackground;



    /**
     * 布局3是否显示
     */
    private boolean islayout3show = true;


    //==============================初始化及布局构建==============================

    /**
     * 布局中初始化
     *
     * @param context
     * @param attrs
     */
    public TextInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.textInputViewStyle);//获取自定义样式
        inputNumber = typedArray.getInteger(R.styleable.textInputViewStyle_inputNumber, defaultInputNumber);
        int mInputType = typedArray.getInt(R.styleable.textInputViewStyle_inputType, VCInputType.NUMBER.ordinal());
        this.inputType = VCInputType.values()[mInputType];
        inputWidth = typedArray.getDimensionPixelSize(R.styleable.textInputViewStyle_inputWidth, defaultInputWidth);
        inputHeight = typedArray.getDimensionPixelSize(R.styleable.textInputViewStyle_inputHeight, defaultInputHeight);
        inputTextColor = typedArray.getColor(R.styleable.textInputViewStyle_inputColor, Color.YELLOW);
        inputTextSize = typedArray.getDimensionPixelSize(R.styleable.textInputViewStyle_inputSize, defaultInputTextSize);
        inputBackground = typedArray.getResourceId(R.styleable.textInputViewStyle_inputBackground, R.drawable.input_bg_style);

        //释放资源
        typedArray.recycle();
        //构建布局
        initView();
    }

    /**
     * 构建布局
     */
    @SuppressLint("ResourceAsColor")
    private void initView() {
        //需要重新构建需要clear
        removeAllViews();
        //初始化布局
        layout2 = new LinearLayout(mContext);
        initLayout();
    }

    /**
     * 创建布局2，该布局是TextView输入框
     */
    private void initLayout() {
        layout2.setOrientation(HORIZONTAL);
        for (int i = 0; i < inputNumber; i++) {
            TextView textView = new TextView(mContext);
            initTextView(textView, i);
            layout2.addView(textView);

            if (i == 0) { //设置第一个textView获取焦点
                textView.setFocusable(true);
            }
        }
        addView(layout2);
    }

    /**
     * 第i个TextView设置
     *
     * @param textView
     * @param i
     */
    private void initTextView(TextView textView, int i) {
        int marginWidth = 20;
        LayoutParams layoutParams = new LayoutParams(inputWidth, inputHeight);

        if (i == 0) {//第一个设置为0
            layoutParams.leftMargin = 0;
        } else {
            if (inputWidth == 120) {
                layoutParams.leftMargin = 30;
            } else {
                layoutParams.leftMargin = 20;
            }
        }

        layoutParams.gravity = Gravity.CENTER;

        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setId(i);
        textView.setCursorVisible(true);
        textView.setMaxEms(1);
        textView.setTextColor(inputTextColor);
        textView.setTextSize(inputTextSize);
        textView.setMaxLines(1);
        textView.setPadding(0, 0, 0, 0);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        //设置过滤
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        switch (inputType) {
            case NUMBER:
                textView.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case NUMBERPASSWORD:
                textView.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case TEXT:
                textView.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case TEXTPASSWORD:
                textView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            default:
                textView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        textView.setBackgroundResource(inputBackground);

    }

    //==============================override==============================

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() != 0) {
            focus1();
        }
    }


    /**
     * 处理layout2子控件相关
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        int childCount = 0;
        childCount = ((LinearLayout) getChildAt(0)).getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }


    //==============================private 布局操作==============================


    /**
     * (软键盘)
     * 处理layout2焦点相关，，layout1变色相关，否已编辑（已编辑和正在编辑，变色，回退的输入框正常色）
     */
    private void focus() {
        int count = getChildCountOf2();
        TextView textView;

        //有值
        for (int i = 0; i < count; i++) {
            //
            textView = getTextView(i);

            if (textView.getText().length() > 0) {//有值 变色
                textView.setSelected(true);
            }
            if (textView.getText().length() < 1) {//正在编辑 变色
                textView.setSelected(true);//setSelected（）在setCursorVisible（）上，不可以颠倒
                textView.setCursorVisible(true);
                textView.requestFocus();
                return;
            }
        }

        //如果最后一个输入框有字符，则返回结果
        TextView lastTextView = getTextView(inputNumber - 1);

        if (lastTextView.getText().length() > 0) {
            lastTextView.setCursorVisible(false);
            lastTextView.setSelected(true);
            getResult();
        } else {
            lastTextView.setSelected(false);
        }
    }

    /**
     * (嵌入式)
     * 处理layout2焦点相关
     */
    private void focus1() {
        int count = getChildCountOf2();
        TextView textView;
        //有值
        for (int i = 0; i < count; i++) {
            //
            textView = getTextView(i);

            if (textView.getText().length() > 0) {//有值 变色
                textView.setSelected(true);
            }
            if (textView.getText().length() < 1) {//正在编辑 变色
                textView.setSelected(true);//setSelected（）在setCursorVisible（）上，不可以颠倒
                textView.setCursorVisible(true);
                textView.requestFocus();
                return;
            }
        }

        //如果最后一个输入框有字符，则返回结果
        TextView lastTextView = getTextView(inputNumber - 1);

        if (lastTextView.getText().length() > 0) {
            lastTextView.setCursorVisible(false);
            lastTextView.setSelected(true);
            getResult();
        } else {
            lastTextView.setSelected(false);
        }
    }

    /**
     * 监听 X号键，有值变色，没值不变色，正在编辑变色
     */
    private void backFocus() {

        long startTime = System.currentTimeMillis();
        TextView textView;
        //循环检测有字符的`textView`，把其置空，并获取焦点。
        for (int i = inputNumber - 1; i >= 0; i--) {
            textView = getTextView(i);
            if (textView.getText().length() >= 1 && startTime - endTime > 100) {
                textView.setText("");
                textView.setCursorVisible(true);
                textView.requestFocus();
                if (i == inputNumber - 1) {
                    getTextView(i).setSelected(true);
                } else {
                    getTextView(i).setSelected(true);
                    getTextView(i + 1).setSelected(false);
                }
                endTime = startTime;
                return;
            }
        }
    }

    /**
     * 输入完成，返回结果
     * 返回两种结果，一种是string [],包含01，另一种返回int[],string数组大小和inputNumber相同，int数组大小和inputNumber相同
     */
    private void getResult() {
        StringBuffer stringBuffer = new StringBuffer();
        TextView textView;

        for (int i = 0; i < inputNumber; i++) {
            textView = getTextView(i);
            stringBuffer.append(textView.getText());

        }

        if (onCodeFinishListener != null) {
            onCodeFinishListener.onComplete(stringBuffer.toString());
        }
    }

    //==============================layout2相关==============================

    /**
     * 获取layout2子控件TextView
     *
     * @return
     */
    private TextView getTextView(int position) {
        return (TextView) ((LinearLayout) getChildAt(0)).getChildAt(position);

    }

    /**
     * 获取layout2子控件个数
     *
     * @return
     */
    private int getChildCountOf2() {
        return ((LinearLayout) getChildAt(0)).getChildCount();
    }


    //==============================外部监听(嵌入式不适用)==============================
    public interface OnInputFinishListener {
        void onComplete(String content);
    }

    public void setOnInputFinishListener(OnInputFinishListener onCodeFinishListener) {
        this.onCodeFinishListener = onCodeFinishListener;
    }

    //==============================enum==============================

    /**
     * 输入样式：数字 文字 数字密码 密码
     */
    public enum VCInputType {
        NUMBER,
        TEXT,
        NUMBERPASSWORD,
        TEXTPASSWORD,
    }


    //==============================public 操作(和嵌入式相关，由外部键盘控制)==============================

    /**
     * 输入值
     * 输入字大于inputNumber，不处理
     *
     * @param string
     */

    public boolean setText(CharSequence string) {
        int lenght = string.length();
        char[] array = string.toString().toCharArray();
        if (lenght >= inputNumber) {
            TextView textView = null;
            for (int i = 0; i < inputNumber; i++) {
                textView = getTextView(i);
                textView.setText(array[i] + "");
                textView.setSelected(true);
            }
            //设置焦点
            focus1();
            return true;
        } else {//lenght < inputNumber
            TextView textView = null;
            //输入框变色

            for (int i = 0; i < inputNumber; i++) {
                textView = getTextView(i);
                if (i < lenght) {
                    textView.setText(array[i] + "");
                    textView.setSelected(true);

                } else {
                    textView.setText("");
                    textView.setSelected(false);
                }

            }
            //设置焦点
            focus1();
            return true;
        }
    }


    /**
     * 获取值
     *
     * @return
     */

    public String getText() {
        TextView textView = null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < inputNumber; i++) {
            textView = getTextView(i);
            builder.append(textView.getText().toString());
        }
        return builder.toString();
    }


    //==============================属性getter setter==============================

    /**
     * 所有set设置完成之后，都要调用build
     */
    public void build() {
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getInputNumber() {
        return inputNumber;
    }


    public TextInputView setInputNumber(int inputNumber) {
        this.inputNumber = inputNumber;
        return this;
    }

    public VCInputType getInputType() {
        return inputType;
    }

    public TextInputView setInputType(VCInputType inputType) {
        this.inputType = inputType;
        return this;
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public TextInputView setInputWidth(int inputWidth) {
        this.inputWidth = inputWidth;
        return this;
    }

    public int getInputHeight() {
        return inputHeight;
    }

    public TextInputView setInputHeight(int inputHeight) {
        this.inputHeight = inputHeight;
        return this;
    }


    public int getInputTextColor() {
        return inputTextColor;
    }

    public TextInputView setInputTextColor(int inputTextColor) {
        this.inputTextColor = inputTextColor;
        return this;
    }

    public float getInputTextSize() {
        return inputTextSize;
    }

    public TextInputView setInputTextSize(float inputTextSize) {
        this.inputTextSize = inputTextSize;
        return this;
    }

    public int getInputBackground() {
        return inputBackground;
    }

    public TextInputView setInputBackground(int inputBackground) {
        this.inputBackground = inputBackground;
        return this;
    }

    public TextInputView showLayout3Tips() {
        this.islayout3show = true;
        return this;
    }

    public TextInputView hideLayout3Tips() {
        this.islayout3show = false;
        return this;
    }


}
