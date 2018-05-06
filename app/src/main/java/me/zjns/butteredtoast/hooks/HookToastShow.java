/*
 * This file is part of ButteredToast.
 *
 * Copyright 2013-2014 Gabriel Castro (c)
 *
 *     ButteredToast is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ButteredToast is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with ButteredToast.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.zjns.butteredtoast.hooks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zjns.butteredtoast.Util;

public class HookToastShow extends XC_MethodHook implements AutoHookable {

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        CharSequence delimiter = Util.isUseChinese() ? "：" : ": ";

        Toast t = (Toast) param.thisObject;
        try {
            View view = t.getView();
            Context context = view.getContext();
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = context.getApplicationInfo();
            String appName = pm.getApplicationLabel(info).toString();
            List<TextView> list = new ArrayList<>();
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                list.add(textView);
                if (textView.getText().toString().startsWith(appName)) {
                    return;
                }
                //XposedBridge.log("[ButteredToast]TextView is toast view");
                textView.setText(new SpannableStringBuilder(appName)
                        .append(delimiter)
                        .append(textView.getText())
                );
                return;
            } else if (view instanceof ViewGroup) {
                Util.findAllTextView(list, (ViewGroup) view);
            }
            if (list.size() != 1) {
                throw new RuntimeException("number of TextViews in toast is not 1");
            }
            TextView text = list.get(0);
            if (text.getText().toString().startsWith(appName)) {
                return;
            }
            text.setText(new SpannableStringBuilder(appName)
                    .append(delimiter)
                    .append(text.getText()));
        } catch (RuntimeException e) {
            XposedBridge.log(e);
        }
    }

    @Override
    public void hook() {
        //XposedBridge.log("[ButteredToast]Toast.show() hooked!");
        XposedHelpers.findAndHookMethod(Toast.class, "show", this);
    }
}