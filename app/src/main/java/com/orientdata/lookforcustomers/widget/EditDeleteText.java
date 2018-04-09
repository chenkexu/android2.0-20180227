package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Joke on 2015/8/22.
 */
public class EditDeleteText extends EditText implements View.OnTouchListener, View
		.OnFocusChangeListener, TextWatcher {
	private boolean isDeletable;
	private Drawable deleteDrawable;
	private OnTouchListener otl;
	private OnFocusChangeListener ofcl;

	@Override
	public void onFocusChange(View view, boolean b) {
		updateDeleteDrawable();
		if (ofcl != null) {
			ofcl.onFocusChange(view, b);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
			return false;
		}

		if (getCompoundDrawables()[2] != null && motionEvent.getX() > getWidth() - getPaddingRight
				() - getCompoundDrawables()[2].getIntrinsicWidth()) {
			setText("");
		}

		if (otl != null) {
			return otl.onTouch(view, motionEvent);
		} else {
			return false;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void afterTextChanged(Editable editable) {
		updateDeleteDrawable();
	}

	public boolean isDeletable() {
		return isDeletable;
	}

	public void setIsDeletable(boolean isDeletable) {
		this.isDeletable = this.isDeletable;
	}

	public Drawable getDeleteDrawable() {
		return deleteDrawable;
	}

	public void setDeleteDrawable(Drawable deleteDrawable) {
		this.deleteDrawable = deleteDrawable;
	}

	public EditDeleteText(Context context) {
		super(context);
		init();
	}

	public EditDeleteText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EditDeleteText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		deleteDrawable = getCompoundDrawables()[2];
		super.setOnTouchListener(this);
		super.setOnFocusChangeListener(this);
		addTextChangedListener(this);
		updateDeleteDrawable();
	}

	private void updateDeleteDrawable() {
		if (isFocused() && getText().length() > 0) {
			showDeleteDrawable();
		} else {
			hideDeleteDrawable();
		}
	}

	private void showDeleteDrawable() {
		super.setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
				deleteDrawable, getCompoundDrawables()[3]);
	}

	private void hideDeleteDrawable() {
		super.setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null,
				getCompoundDrawables()[3]);
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable
			bottom) {
		super.setCompoundDrawables(left, top, right, bottom);
		deleteDrawable = right;
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		this.otl = l;
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		this.ofcl = l;
	}
}
