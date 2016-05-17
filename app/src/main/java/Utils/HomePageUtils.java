package Utils;
import android.content.res.Resources;

/**
 * Created by MonkeyzZi on 2016/4/19.
 */
public class HomePageUtils {
    //密度

    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    public static int dp2px(int dp){
        return  Math.round(dp * DENSITY);
    }
}
