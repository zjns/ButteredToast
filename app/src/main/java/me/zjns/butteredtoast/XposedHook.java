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

package me.zjns.butteredtoast;

import de.robv.android.xposed.IXposedHookZygoteInit;
import me.zjns.butteredtoast.hooks.HookMIUICompatible;
import me.zjns.butteredtoast.hooks.HookToastShow;

public class XposedHook implements IXposedHookZygoteInit {

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        new HookToastShow().hook();
        if (Util.isMIUIRom()) {
            new HookMIUICompatible().hook();
        }
    }

}
