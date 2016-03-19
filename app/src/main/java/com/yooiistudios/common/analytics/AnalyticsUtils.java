package com.yooiistudios.common.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sulga.yooiitable.TimeTableApplication;

/**
 * Created by Wooseong Kim in News from Yooii Studios Co., LTD. on 16. 03. 19.
 *
 * AnalyticsUtils
 * Google Analytics 를 사용하기 위한 클래스
 */
public class AnalyticsUtils {
    private AnalyticsUtils() { throw new AssertionError("You must not create this class!"); }

    // News Kit 과 달리 여기서는 Auto Activity Tracking 을 사용할 예정. 이벤트 보내기 용으로 사용하자
    // 새 버전의 GA에서는 Auto Activity Tracking을 지원을 안하는 듯 모든 액티비티에서 사용 예정
    public static void startAnalytics(TimeTableApplication application, int resId) {
        // Get tracker.
        Tracker t = application.getDefaultTracker();

        // Set screen name.
        String screenName = application.getString(resId);
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    /*
    // screenName 은 global_tracker 를 참고해서 같은 이름을 사용할 것
    // 사진 보낸 횟수, 갯수 체크
    public static void trackNumberOfPhotosAtDressing(Activity activity, int number) {
        TimeTableApplication application = (TimeTableApplication) activity.getApplication();

        // Get tracker
        Tracker t = application.getTracker(TimeTableApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("Dressing")
                .setAction("Number of photos")
                .setLabel(String.valueOf(number))
                .build());
    }

    public static void trackNumberOfPhotoShare(Activity activity, int photoShareCount) {
        TimeTableApplication application = (TimeTableApplication) activity.getApplication();

        // Get tracker
        Tracker t = application.getTracker(TimeTableApplication.TrackerName.APP_TRACKER);

        for (int i = 0; i < photoShareCount; i++) {
            // Build and send an Event.
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("General")
                    .setAction("Photo Share")
                    .setLabel("Share Count")
                    .build());
        }
    }

    public static void trackNumberOfDecoratingHistory(Activity activity, int historyCount) {
        TimeTableApplication application = (TimeTableApplication) activity.getApplication();

        // Get tracker
        Tracker t = application.getTracker(TimeTableApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("Decorate")
                .setAction("Decorating")
                .setLabel("Decorate History")
                .setValue(historyCount)
                .build());
    }

    /*
    // 각 액티비티 회전 체크
    public static void trackActivityOrientation(NewsApplication application, String activityName, int orientation) {
        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(activityName)
                .setAction("Orientation")
                .setLabel(orientation == Configuration.ORIENTATION_PORTRAIT ? "Portrait" : "Landscape")
                .build());
    }

    // 풀버전 광고가 불려질 때 풀버전인지 아닌지 체크
    public static void trackInterstitialAd(NewsApplication application, String TAG) {
        List<String> ownedSkus = IabProducts.loadOwnedIabProducts(application);

        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(TAG)
                .setAction("Showing Ad when have PRO version")
                .setLabel(ownedSkus.contains(IabProducts.SKU_PRO_VERSION) ? "YES" : "NO")
                .build());
    }

    // 뉴스피드 디테일에서 뒤로 나가는 것을 up 버튼으로 하는지 back 버튼으로 하는지 조사하기 위해서 도입
    public static void trackNewsFeedDetailQuitAction(NewsApplication application, String TAG, String quitViewName) {
        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(TAG)
                .setAction("Quit NewsFeedDetailActivity with")
                .setLabel(quitViewName)
                .build());
    }

    // 세팅화면에서 뒤로 나가는 것을 up 버튼으로 하는지 back 버튼으로 하는지 조사하기 위해서 도입
    public static void trackSettingsQuitAction(NewsApplication application, String TAG, String quitViewName) {
        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(TAG)
                .setAction("Quit SettingActivity with")
                .setLabel(quitViewName)
                .build());
    }

    // 세팅 - 패널 매트릭스 선택을 트래킹
    public static void trackNewsPanelMatrixSelection(NewsApplication application, String TAG, String panelMatrix) {
        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(TAG)
                .setAction("Newsfeed Panel Matrix Selection")
                .setLabel(panelMatrix)
                .build());
    }

    // 세팅 - 언어 선택을 트래킹
    public static void trackLanguageSelection(NewsApplication application, String TAG, String language) {
        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(TAG)
                .setAction("Language Selection on Settings")
                .setLabel(language)
                .build());
    }

    // 첫 설치시 디바이스 언어 트래킹
    public static void trackDefaultLanguage(NewsApplication application, String language) {
        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("General")
                .setAction("Default Language on first app launch")
                .setLabel(language)
                .build());
    }

    // 첫 설치시 디바이스 언어 트래킹
    public static void trackDefaultCountry(NewsApplication application, String country) {
        // Get tracker.
        Tracker t = application.getTracker(NewsApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("General")
                .setAction("Default Country on first app launch")
                .setLabel(country)
                .build());
    }
    */
}
