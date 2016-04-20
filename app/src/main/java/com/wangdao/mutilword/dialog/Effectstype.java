package com.wangdao.mutilword.dialog;


import com.wangdao.mutilword.dialog.effects.BaseEffects;
import com.wangdao.mutilword.dialog.effects.RotateLeft;
import com.wangdao.mutilword.dialog.effects.SlideTop;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Slidetop(SlideTop.class),
    RotateLeft(RotateLeft.class);

    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
