package com.ads.sapp.util;

import static com.applovin.impl.sdk.n.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ads.sapp.R;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.funtion.BannerCallback;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.Locale;

public class CheckAds {

    private static CheckAds instance;
    private static final String SPACE = "_____";
    private static final String TEXT_ADS_EN = "Test Ad";

    //Check test native
    public static Boolean isTest = false;
    public static Boolean isTestLanguage = false;
    public static final String NA = "NA";
    public static Boolean isTestIntro = false;
    public static final String IN = "IN";
    public static Boolean isTestPermission = false;
    public static final String PE = "PE";
    public static Boolean isTestOther = false;
    public static final String OT = "OT";

    //Check banner
    public static Boolean isTestBanner = false;

    public  static int countCheck = 0;

    //If check
    public static Boolean checkAd = true;

    //List drive
    private static ArrayList<String> listDriveID = new ArrayList<>();
    //Drive Test
    private static String driveID = "";

    //List test common
    private static ArrayList<String> listTextAds = listTextTestAds();

    //Store
    private String testAd = "testAd";

    public static CheckAds getInstance() {
        if (instance == null) {
            instance = new CheckAds();
        }
        return instance;
    }

    public void init(Context context, ArrayList<String> listDriveIDs, Boolean checkAds){
        listDriveID = listDriveIDs;
        checkAd = checkAds;
        isTest = false;
        isTestBanner = false;
        countCheck = 0;
        if(driveID.equals("")){
            driveID = getDeviceIdTest(context);
        }
    }

    public boolean isShowAds(Context context){
        if(checkAd){
            if(getTestAd(context)){
                if(isDriveTest(context)){
                    Log.d("checkAds","Share: isShowAds: true");
                    return true;
                }else {
                    Log.d("checkAds","Share: isShowAds: false");
                    return false;
                }
            }

            if(isTestBanner){
                if(isDriveTest(context)){
                    Log.d("checkAds","isTestBanner: isShowAds: true");
                    return true;
                }else {
                    Log.d("checkAds","isTestBanner: isShowAds: false");
                    return false;
                }
            }

            if(isTest){
                if(isDriveTest(context)){
                    Log.d("checkAds","isTest: isShowAds: true");
                    return true;
                }else {
                    Log.d("checkAds","isTest: isShowAds: false");
                    return false;
                }
            } else return true;
        }else return true;
    }

    public static ArrayList<String> setDataDriveID(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a95848c5c33cda2b");
        return arrayList;
    }

    public static Boolean isDriveTest(Context context){
        Boolean isTestDrive = false;
        if(listDriveID != null){
            for(String s: listDriveID){
                if(s.equals(driveID)){
                    isTestDrive = true;
                    break;
                }
            }
            Log.d("checkAds","isTestDrive: " +isTestDrive.toString());
        }
        return isTestDrive;
    }

    public static Boolean isShowAdsTest(Context context, String screenNameCheck){

        try{
            if(screenNameCheck.equals("")){
            }else if(screenNameCheck.equals(NA)){
                if(isTestLanguage){
                    if(isDriveTest(context)){
                        return true;
                    }else {
                        return false;
                    }
                }else return true;
            }else if(screenNameCheck.equals(IN)){
                if(isTestIntro){
                    if(isDriveTest(context)){
                        return true;
                    }else {
                        return false;
                    }
                }else return true;
            }else if(screenNameCheck.equals(PE)){
                if(isTestPermission){
                    if(isDriveTest(context)){
                        return true;
                    }else {
                        return false;
                    }
                }else return true;
            }else if(screenNameCheck.equals(OT)){
                if(isTestOther){
                    if(isDriveTest(context)){
                        return true;
                    }else {
                        return false;
                    }
                }else return true;
            }

            if(isTest){
                if(isDriveTest(context)){
                    return true;
                }else {
                    return false;
                }
            }else return true;
        }catch (Exception e){
            Log.d("checkAds","Exception: " +e.getMessage());

            return true;
        }
    }

    public static void checkAds(NativeAdView adView,String screenName){
        Boolean isTestAd = false;

        try {
            if(!checkAd){
                return;
            }

            //Location
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {
                locale = Resources.getSystem().getConfiguration().locale;
            }
            String locationCode = locale.getLanguage();

            //Content Check
            String text = getNativeInfo(adView);
            Log.d("checkAds","text: " +text);

            if(text != null){
                if(text.length() > 0){
                    String[] stringTexts = text.split(":");
                    if(stringTexts.length  > 0){
                        String textAds = stringTexts[0].toString().trim();
                        Log.d("checkAds","locationCode: " + locationCode);
                        Log.d("checkAds","textAds0: " + textAds);
                        if(textAds.equals(TEXT_ADS_EN)){
                            isTestAd = true;
                        }else {
                            for(String textDefault: listTextAds){
                                String[] contentHead = textDefault.split(SPACE);
                                if(contentHead.length > 0){
                                    if(contentHead[0].equals(locationCode)){
                                        if(contentHead[1] != null){
                                            String[] textcontentHead  = contentHead[1].split(":");
                                            if(textcontentHead[0] !=null){
                                                if(textAds.equals(textcontentHead[0].trim())){
                                                    isTestAd = true;
                                                    Log.d("checkAds","textAds: " +textAds + ", Text common: " +contentHead[1].trim());
                                                    break;
                                            }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Re Check
            if(!isTestAd){
                for(String textDefault: listTextAds){
                    String[] contentHead = textDefault.split(SPACE);
                    if(contentHead.length > 0){
                        if(contentHead[0].equals(locationCode)){
                            if(text.contains(contentHead[1].trim())){
                                isTestAd = true;
                                break;
                            }
                        }
                    }
                }
            }

            if(isTestAd){
                if(screenName.equals(NA)){
                    isTestLanguage = true;
                } else if (screenName.equals(IN)) {
                    isTestIntro = true;
                }else if(screenName.equals(PE)){
                    isTestPermission = true;
                }else if(screenName.equals(OT)){
                    isTestOther = true;
                }
                isTest = true;
                Log.d("checkAds", "isTest: " + isTestAd.toString());

            }
        }catch (Exception ex){
            Log.d("checkAds","Error");
            Log.d("checkAds",ex.getMessage());
        }
    }

    public static String getDeviceIdTest(Context context) {
        @SuppressLint("HardwareIds") String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("checkAds","getDeviceIdTest: " + id);

        return id;
    }

    public static ArrayList<String> listTextTestAds(){
        ArrayList<String> list = new ArrayList<>();
        list.add("en_____Test Ad");
        list.add("de_____Testanzeige");
        list.add("vi_____Quảng cáo thử nghiệm");
        list.add("in_____Tes Iklan");
        list.add("fr_____Annonce test");
        list.add("bs_____Probni oglas");
        list.add("ca_____Anunci de prova");
        list.add("da_____Testannonce");
        list.add("et_____Testreklaam");
        list.add("es_____Anuncio de prueba");
        list.add("et_____Testreklaam");
        list.add("fil_____Pansubok na Ad");
        list.add("gl_____Probar anuncio");
        list.add("zh_____測試廣告");
        list.add("zh_____测试广告");
        list.add("km_____ពាណិជ្ជកម្ម​សាកល្បង");
        list.add("lo_____ໂຄສະນາທົດສອບ");
        list.add("th_____โฆษณาทดสอบ");
        list.add("si_____පරීක්ෂණ වෙළඳ දැන්වීම");
        list.add("ml_____പരസ്യം പരീക്ഷിക്കുക");
        list.add("en_____પરીક્ષણની જાહેરાત");
        list.add("bn_____পরীক্ষামূলক বিজ্ঞাপন");
        list.add("ne_____परीक्षण विज्ञापन");
        list.add("am_____የሙከራ ማስታወቂያ");
        list.add("fa_____آگهی آزمایشی");
        list.add("ar_____إعلان تجريبي");
        list.add("ar_____إعلان تجريبي");
        list.add("zh_____測試廣告");
        list.add("cs_____Zkušební reklama");
        list.add("cs_____Zkušební reklama");
        list.add("hr_____Testni oglas");
        list.add("it_____Annuncio di testo");
        list.add("sw_____Tangazo la Jaribio");
        list.add("lt_____Bandomasis skelbimas");
        list.add("vi_____Quảng cáo thử nghiệm");
        list.add("da_____Testannonce");
        list.add("nl_____Testadvertentie");
        list.add("nb_____Testannonse");
        list.add("uz_____Test reklama");
        list.add("pl_____Reklama testowa");
        list.add("pt_____Anúncio de teste");
        list.add("ro_____Anunț de probă");
        list.add("sq_____Reklamë test");
        list.add("sq_____Reklamë test");
        list.add("el_____Δοκιμαστική διαφήμιση");
        list.add("sq_____Reklamë test");
        list.add("sl_____Preizkusni oglas");
        list.add("sr_____Probni oglas");
        list.add("fi_____Testimainos");
        list.add("sv_____Testannons");
        list.add("tr_____Test Reklamı");
        list.add("bg_____Тестова реклама");
        list.add("mk_____Тестирај ја рекламата");
        list.add("ru_____Тестовое объявление");
        list.add("sr_____Пробни оглас");
        list.add("sr_____Пробни оглас");
        list.add("uk_____Тестове оголошення");
        list.add("kk_____Сынақ жарнама");
        list.add("iw_____מודעת בדיקה");
        list.add("ar_____מודעת בדיקה");
        list.add("mr_____चाचणी जाहिरात");
        list.add("as_____পৰীক্ষণ বিজ্ঞাপন");
        list.add("or_____ପରୀକ୍ଷାମୂଳକ ବିଜ୍ଞାପନ");
        list.add("ta_____சோதனை விளம்பரம்");
        list.add("te_____ప్రకటనను పరీక్షించండి");
        list.add("my_____စမ်းသပ်ကြော်ငြာ");
        list.add("kn_____ಪರೀಕ್ಷಾ ಜಾಹೀರಾತು");
        list.add("hi_____जाँच विज्ञापन");
        list.add("ar_____إعلان تجريبي");
        list.add("bs_____Probni oglas");
        return list;
    }

    public static String getNativeInfo(NativeAdView adView){
        try{
            TextView headlineView = adView.findViewById(R.id.ad_headline);
            return headlineView.getText().toString();
        }catch (Exception e){
            return "";
        }
    }

    public static Boolean getIsTestLanguage() {
        return isTestLanguage;
    }

    public static void setIsTestLanguage(Boolean isTestLanguage) {
        CheckAds.isTestLanguage = isTestLanguage;
    }

    public static Boolean getIsTestIntro() {
        return isTestIntro;
    }

    public static void setIsTestIntro(Boolean isTestIntro) {
        CheckAds.isTestIntro = isTestIntro;
    }

    public static Boolean getIsTestPermission() {
        return isTestPermission;
    }

    public static void setIsTestPermission(Boolean isTestPermission) {
        CheckAds.isTestPermission = isTestPermission;
    }

    public static Boolean getIsTestOther() {
        return isTestOther;
    }

    public static void setIsTestOther(Boolean isTestOther) {
        CheckAds.isTestOther = isTestOther;
    }

    public static void setListDriveID(ArrayList<String> listDriveID) {
        CheckAds.listDriveID = listDriveID;
    }

    public static String imageToText(Bitmap bitmap) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        Frame imageFrame = new Frame.Builder()
                .setBitmap(bitmap)
                .build();
        String imageText = "";
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            imageText = imageText + "" + textBlock.getValue();
        }
        Log.d("imageToText", "imageToText: " + imageText);
        return imageText;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public void checkBanner(Context context, final FrameLayout adContainer, final BannerCallback callback, int timeDelay){
        try{
            if(!checkAd){
                (new Handler(context.getMainLooper())).postDelayed(new Runnable() {
                    public void run() {
                        callback.onCheckComplete();
                    }
                }, (long) timeDelay);
            }

            // Next when ads store
            if(getTestAd(context)){
                Log.d("checkAds", "Share: Skip check");

                (new Handler(context.getMainLooper())).postDelayed(new Runnable() {
                    public void run() {
                        callback.onCheckComplete();
                    }
                }, (long) timeDelay);

                return;
            }

            if(countCheck > 10){
                countCheck = 1;
            }

            countCheck += 1;

            Log.d("checkAds", "countCheck: " + countCheck);

            // Next when ads recheck
            if(isTestBanner == true && countCheck > 1){
                Log.d("checkAds", "Skip check");
                return;
            }
            Log.d("checkAds", "Next check");

            // Stop check if limit 5 times
            if(countCheck > 5){
                Log.d("checkAds", "Stop check");
                return;
            }

            Bitmap bitmap = getBitmapFromView(adContainer);
            TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
            Frame imageFrame = new Frame.Builder()
                    .setBitmap(bitmap)
                    .build();
            String imageText = "";
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                imageText = imageText + "" + textBlock.getValue();
            }
            Log.d("imageToText", "imageToText: " + imageText);

            if(isTestBanner!= true && imageText.contains(TEXT_ADS_EN)){
                isTestBanner = true;
                storeTestAd(context);
                Log.d("checkAds","textAdsBaner: " +imageText + ", Text common: " +TEXT_ADS_EN);
                Log.d("checkAds","textAdsBaner: "+isTestBanner.toString());

                (new Handler(context.getMainLooper())).postDelayed(new Runnable() {
                    public void run() {
                        callback.onCheckComplete();
                    }
                }, (long) timeDelay);
                return;

            }

            for(String textDefault: listTextAds){
                String[] contentHead = textDefault.split(SPACE);
                if(contentHead.length > 0){
                    if(contentHead[0].equals(getLocation())){
                        if(contentHead[1] != null){
                            String[] textcontentHead  = contentHead[1].split(":");
                            if(textcontentHead[0] !=null){
                                if(imageText.contains(textcontentHead[0].trim())){
                                    isTestBanner = true;
                                    storeTestAd(context);
                                    Log.d("checkAds","textAdsBaner: " + imageText + ", Text common: " +contentHead[1].trim());
                                    Log.d("checkAds","textAdsBaner: " + isTestBanner.toString());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if(countCheck <= 2){
                (new Handler(context.getMainLooper())).postDelayed(new Runnable() {
                    public void run() {
                        callback.onCheckComplete();
                    }
                }, (long) timeDelay);
            }
        }catch (Exception ex){
            (new Handler(context.getMainLooper())).postDelayed(new Runnable() {
                public void run() {
                    callback.onCheckComplete();
                }
            }, (long) timeDelay);
        }
    }

    public void checkBanner(Context context, final FrameLayout adContainer){
        try{
            if(!checkAd){
               return;
            }

            if(countCheck > 10){
                countCheck = 1;
            }

            countCheck += 1;
            Log.d("checkAds", "countCheck: " + countCheck);

            // Next when ads store
            if(getTestAd(context)){
                Log.d("checkAds", "Share: Skip check");
                return;
            }

            if(isTestBanner == true && countCheck > 1){
                Log.d("checkAds", "Skip check");
                return;
            }
            Log.d("checkAds", "Next check");

            if(countCheck > 5){
                Log.d("checkAds", "Stop check");
                return;
            }

            Bitmap bitmap = getBitmapFromView(adContainer);
            TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
            Frame imageFrame = new Frame.Builder()
                    .setBitmap(bitmap)
                    .build();
            String imageText = "";
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                imageText = imageText + "" + textBlock.getValue();
            }
            Log.d("imageToText", "imageToText: " + imageText);

            if(isTestBanner!= true && imageText.contains(TEXT_ADS_EN)){
                isTestBanner = true;
                storeTestAd(context);
                Log.d("checkAds","textAdsBaner: " +imageText + ", Text common: " +TEXT_ADS_EN);
                Log.d("checkAds","textAdsBaner: "+isTestBanner.toString());
                return;
            }

            for(String textDefault: listTextAds){
                String[] contentHead = textDefault.split(SPACE);
                if(contentHead.length > 0){
                    if(contentHead[0].equals(getLocation())){
                        if(contentHead[1] != null){
                            String[] textcontentHead  = contentHead[1].split(":");
                            if(textcontentHead[0] !=null){
                                if(imageText.contains(textcontentHead[0].trim())){
                                    isTestBanner = true;
                                    storeTestAd(context);
                                    Log.d("checkAds","textAdsBaner: " + imageText + ", Text common: " +contentHead[1].trim());
                                    Log.d("checkAds","textAdsBaner: " + isTestBanner.toString());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception ex){}
    }

    public static String getLocation(){
        try{
            //Location
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {
                locale = Resources.getSystem().getConfiguration().locale;
            }
            return locale.getLanguage();
        }catch (Exception ex){
            return "";
        }
    }

    public void storeTestAd(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE).edit();
        editor.putBoolean(testAd, true);
        editor.commit();
    }

    public Boolean getTestAd(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        return preferences.getBoolean(testAd, false);
    }
}
