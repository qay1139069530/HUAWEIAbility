package com.qbase.huaweiability.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.qbase.huaweiability.R;

/**
 * 修改状态栏颜色
 */
@SuppressLint("NewApi")
public class StatusBarCompat {

    private static View mStatusBarView;

    public static final int DEFAULT_STATUS_BAR_ALPHA = 0;

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setColor(Activity activity, int color) {
        setColor(activity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */
    public static void setColor(Activity activity, int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            try {
                //window.setNavigationBarColor(ContextCompat.getColor(activity, color));
                View view = window.getDecorView();
                view.setBackgroundColor(ContextCompat.getColor(activity, R.color.White));
                ViewGroup winContent = activity.findViewById(android.R.id.content);
                if (winContent != null && winContent.getChildCount() > 0) {
                    ViewGroup rootView = (ViewGroup) winContent.getChildAt(0);
                    if (rootView != null) {
                        rootView.setFitsSystemWindows(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("-------", "setColor error");
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("-------", "Build.LOLLIPOP.M");
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View decorView = activity.getWindow().getDecorView();
            //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏颜色为透明
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusBarView(activity, color, statusBarAlpha);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            setRootView(activity);
        }
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, int color,
                                            int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0);
        if (null != rootView) {
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 简单型状态栏(ToolBar)
     *
     * @param activity
     */
    public static void setOrdinaryToolBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            setKKStatusBar(activity, R.color.colorPrimaryDark);
        }
    }


    /**
     * 图片全屏透明状态栏（图片位于状态栏下面）
     * 状态栏字体颜色为黑色
     *
     * @param activity
     */
    public static void setImageWhiteTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 图片全屏透明状态栏（图片位于状态栏下面）
     * 状态栏字体颜色为黑色
     *
     * @param activity
     */
    public static void setImageTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            try {
                window.setNavigationBarColor(Color.TRANSPARENT);
                View view = window.getDecorView();
                view.setBackgroundColor(ContextCompat.getColor(activity, R.color.White));
                ViewGroup winContent = activity.findViewById(android.R.id.content);
                if (winContent.getChildCount() > 0) {
                    ViewGroup rootView = (ViewGroup) winContent.getChildAt(0);
                    if (rootView != null) {
                        rootView.setFitsSystemWindows(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setKKStatusBar(activity, R.color.alpha_all);
        }
    }

    /**
     * 图片全屏半透明状态栏（图片位于状态栏下面）
     *
     * @param activity
     */
    public static void setImageTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.alpha_all));
        } else {
            setKKStatusBar(activity, R.color.alpha_all);
        }
    }

    /**
     * ToolBar+TabLayout状态栏(ToolBar可伸缩)
     *
     * @param activity
     */
    public static void setToolbarTabLayout(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
    }

    /**
     * DrawerLayout+ToolBar+TabLayout状态栏(ToolBar可伸缩)
     *
     * @param activity
     * @param coordinatorLayout
     */
    public static void setDrawerToolbarTabLayout(Activity activity, CoordinatorLayout coordinatorLayout) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            coordinatorLayout.setFitsSystemWindows(true);
            setKKStatusBar(activity, R.color.alpha_all);
        }
    }

    /**
     * DrawerLayout+ToolBar+TabLayout状态栏(ToolBar可伸缩)
     *
     * @param activity
     */
    public static void setDrawerToolbarLayout(Activity activity, LinearLayout ll) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            ll.setFitsSystemWindows(true);
            setKKStatusBar(activity, R.color.colorPrimary);
        }
    }

    /**
     * DrawerLayout+ToolBar+TabLayout状态栏(ToolBar可伸缩)
     *
     * @param activity
     * @param coordinatorLayout
     */
    public static void setDrawerToolbarTabLayout2(Activity activity, CoordinatorLayout coordinatorLayout, ImageView imageView, Toolbar toolbar) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            toolbar.setFitsSystemWindows(false);
            coordinatorLayout.setFitsSystemWindows(false);
            imageView.setFitsSystemWindows(true);
            setKKStatusBar(activity, R.color.colorPrimary);
        }
    }

    /**
     * DrawerLayout+ToolBar+TabLayout状态栏(ToolBar可伸缩)
     *
     * @param activity
     */
    public static void setDrawerToolbarImage(Activity activity, ImageView imageView) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            imageView.setFitsSystemWindows(true);
            setKKStatusBar(activity, R.color.colorPrimary);
        }
    }


    /**
     * DrawerLayout+ToolBar型状态栏
     *
     * @param activity
     */
    public static void setDrawerToolbar(Activity activity) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            setKKStatusBar(activity, R.color.colorPrimary);
        }
    }

    /**
     * CollapsingToolbarLayout状态栏(可折叠图片)
     *
     * @param activity
     * @param coordinatorLayout
     * @param appBarLayout
     * @param imageView
     * @param toolbar
     */
    public static void setCollapsingToolbar(Activity activity, CoordinatorLayout coordinatorLayout,
                                            AppBarLayout appBarLayout, ImageView imageView, Toolbar toolbar) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            coordinatorLayout.setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            imageView.setFitsSystemWindows(false);
            toolbar.setFitsSystemWindows(true);
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height = (int) (getStatusBarHeight(activity) +
                    activity.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
            toolbar.setLayoutParams(lp);
            setKKStatusBar(activity, R.color.colorPrimary);
            setCollapsingToolbarStatus(appBarLayout);
        }
    }

    /**
     * Android4.4上CollapsingToolbar折叠时statusBar显示和隐藏
     *
     * @param appBarLayout
     */
    private static void setCollapsingToolbarStatus(AppBarLayout appBarLayout) {
        ViewCompat.setAlpha(mStatusBarView, 1);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(mStatusBarView, percentage);
            }
        });
    }

    private static void setKKStatusBar(Activity activity, int statusBarColor) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        mStatusBarView = contentView.getChildAt(0);
        //改变颜色时避免重复添加statusBarView
        if (mStatusBarView != null && mStatusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
            mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
            return;
        }
        mStatusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
        contentView.addView(mStatusBarView, lp);
    }

    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

}
