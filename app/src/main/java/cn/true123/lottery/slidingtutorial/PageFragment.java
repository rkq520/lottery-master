/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Cleveroad
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */
package cn.true123.lottery.slidingtutorial;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.cleveroad.slidingtutorial.LayersHolder;

import cn.true123.lottery.R;

/**
 * Implementation of {@link Fragment} that contains {@link LayersHolder} and
 * implement create view functionality. Also provide {@link PageFragment#getBackgroundColorResId()}
 * and {@link PageFragment#getRootResId()} for {@link PresentationPagerFragment}
 */
public abstract class PageFragment extends Fragment {

//	private cn.true123.lottery.slidingtutorial.LayersHolder holder;
//
//	protected abstract TransformItem[] provideTransformItems();

	@LayoutRes
	protected abstract int getLayoutResId();

	@IdRes
	@Deprecated
	protected int getRootResId() {
		return 0;
	}

	@ColorRes
	@Deprecated
	protected int getBackgroundColorResId() {
		return 0;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutResId(), container, false);
		//holder = new cn.true123.lottery.slidingtutorial.LayersHolder(view, provideTransformItems());
		view.setTag(R.id.st_page_fragment, this);
		return view;
	}

	//void transformPage(View view, float position) {
	//	holder.transformPage(view.getWidth(), position);
	//}
}
