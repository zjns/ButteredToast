package me.zjns.butteredtoast.hooks;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class HookMIUICompatible extends XC_MethodHook implements AutoHookable {
    @Override
    public void hook() {
        try {
            Class<?> ToastInjector = ClassLoader.getSystemClassLoader().loadClass("android.widget.ToastInjector");
            XposedHelpers.findAndHookMethod(ToastInjector, "addAppName", Context.class, CharSequence.class, this);
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        param.setResult(param.args[1]);
    }
}
