package com.ads.sapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.ads.sapp.R;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class CheckAds {

    private static final String SPACE = "_____";
    private static final String TEXT_ADS_EN = "Test Ad";

    //Check test
    public static Boolean isTest = false;
    public static Boolean isTestLanguage = false;
    public static final String NA = "NA";
    public static Boolean isTestIntro = false;
    public static final String IN = "IN";
    public static Boolean isTestPermission = false;
    public static final String PE = "PE";
    public static Boolean isTestOther = false;
    public static final String OT = "OT";

    //List drive
    private static ArrayList<String> listDriveID = new ArrayList<>();

    //List test common
    private static ArrayList<String> listTextAds = listTextTestAds();

    public static ArrayList<String> setDataDriveID(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a95848c5c33cda2b");
        return arrayList;
    }

    public static Boolean isDriveTest(Context context){
        Boolean isTestDrive = false;
        if(listDriveID != null){
            for(String s: listDriveID){
                if(s.equals(getDeviceIdTest(context))){
                    isTestDrive = true;
                    break;
                }
            }
            //Log.d("checkAds","isTestDrive: " +isTestDrive.toString());
        }
        return isTestDrive;
    }

    public static Boolean isShowAdsTest(Context context, String screenNameCheck){
        //Log.d("checkAds","isTestLanguage: " +isTestLanguage.toString());
        //Log.d("checkAds","isTestIntro: " +isTestIntro.toString());
        //Log.d("checkAds","isTestPermission: " +isTestPermission.toString());
        //Log.d("checkAds","isTestOther: " +isTestOther.toString());

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
        isTest = false;
        Boolean isTestAd = false;

        try {
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
                        String textAds =stringTexts[0].toString().trim();
                        Log.d("checkAds","locationCode: " + locationCode);
                        Log.d("checkAds","textAds0: " + textAds);
                        Log.d("checkAds","textAds1: " +stringTexts[1].toString().trim());
                        if(textAds.equals(TEXT_ADS_EN)){
                            isTestAd = true;
                        }else {
                            for(String textDefault: listTextAds){
                                String[] contentHead = textDefault.split(SPACE);
                                if(contentHead.length > 0){
                                    if(contentHead[0].equals(locationCode)){
                                        //Log.d("checkAds","contentHead0: " + contentHead[0].toString().trim());
                                        //Log.d("checkAds","contentHead1: " + contentHead[1].toString().trim());

                                        if(contentHead[1] != null){
                                            String[] textcontentHead  = contentHead[1].split(":");
                                            if(textcontentHead[0] !=null){
                                                if(textAds.equals(textcontentHead[0].trim())){
                                                    Log.d("checkAds","textAds: " +textAds + ", Text common: " +contentHead[1].trim());
                                                    isTestAd = true;
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
                Log.d("checkAds",isTestAd.toString());

            }
        }catch (Exception ex){
            Log.d("checkAds","Error");
        }
    }

    public static String getDeviceIdTest(Context context) {
        @SuppressLint("HardwareIds") String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("checkAds","getDeviceIdTest: " + id);

        return id;
    }

    public static void logNativeInfo(NativeAdView adView){
        appendOrCreateTextFileInDownload(getNativeInfo(adView));
    }

    public static ArrayList<String> listTextTestAds(){
        ArrayList<String> list = new ArrayList<>();
        list.add("de_____Testanzeige : Filestack: Transform Now");
        list.add("vi_____Quảng cáo thử nghiệm : Effortless File Handling");
        list.add("in_____Tes Iklan : Filestack File Uploader API");
        list.add("fr_____Annonce test : Filestack File Uploader API");
        list.add("bs_____Probni oglas : Filestack: Empower Your Data");
        list.add("ca_____Anunci de prova : Filestack: Transform Now");
        list.add("da_____Testannonce : Filestack: Empower Your Data");
        list.add("et_____Testreklaam : Powerful & Easy To Use API");
        list.add("es_____Anuncio de prueba : Powerful & Easy To Use API");
        list.add("et_____Testreklaam : Filestack File Uploader API");
        list.add("fil_____Pansubok na Ad : Filestack File Uploader API");
        list.add("gl_____Probar anuncio : Filestack: Empower Your Data");
        list.add("zh_____測試廣告 : Powerful & Easy To Use API");
        list.add("zh_____测试广告 : Filestack: Transform Now");
        list.add("km_____ពាណិជ្ជកម្ម​សាកល្បង : Filestack: Transform Now");
        list.add("lo_____ໂຄສະນາທົດສອບ : Filestack: Empower Your Data");
        list.add("th_____โฆษณาทดสอบ : Filestack: Transform Now");
        list.add("si_____පරීක්ෂණ වෙළඳ දැන්වීම : Effortless File Handling");
        list.add("ml_____പരസ്യം പരീക്ഷിക്കുക : Kutieskin");
        list.add("en_____પરીક્ષણની જાહેરાત : Kutieskin");
        list.add("bn_____পরীক্ষামূলক বিজ্ঞাপন : Kutieskin");
        list.add("ne_____परीक्षण विज्ञापन : Kutieskin");
        list.add("am_____የሙከራ ማስታወቂያ : Flash Sale 1 hộp chỉ còn 99k");
        list.add("fa_____آگهی آزمایشی : Flash Sale 1 hộp chỉ còn 99k");
        list.add("ar_____إعلان تجريبي : Kutieskin");
        list.add("ar_____إعلان تجريبي : Kutieskin");
        list.add("zh_____測試廣告 : exocad training");
        list.add("cs_____Zkušební reklama : Venta de Hotel, en Jacó");
        list.add("cs_____Zkušební reklama : Venta de Hotel, en Jacó");
        list.add("hr_____Testni oglas : Kutieskin");
        list.add("it_____Annuncio di testo : Kutieskin");
        list.add("sw_____Tangazo la Jaribio : Kutieskin");
        list.add("lt_____Bandomasis skelbimas : exocad training");
        list.add("vi_____Quảng cáo thử nghiệm : exocad training");
        list.add("da_____Testannonce : exocad training");
        list.add("nl_____Testadvertentie : Kutieskin");
        list.add("en_____Test Ad : Kutieskin");
        list.add("en_____Test Ad : Kutieskin");
        list.add("nb_____Testannonse : exocad training");
        list.add("nb_____Testannonse : Kutieskin");
        list.add("nb_____Testannonse : Găng Tay Gai Silicon Rửa Chén");
        list.add("uz_____Test reklama : Đầu vòi xịt tăng áp");
        list.add("pl_____Reklama testowa : exocad training");
        list.add("pt_____Anúncio de teste : Kutieskin");
        list.add("pt_____Anúncio de teste : Kutieskin");
        list.add("ro_____Anunț de probă : Filestack: Empower Your Data");
        list.add("ro_____Anunț de probă : Filestack: Transform Now");
        list.add("ro_____Anunț de probă : Powerful & Easy To Use API");
        list.add("en_____Test Ad : Filestack: Transform Now");
        list.add("en_____Test Ad : Filestack File Uploader API");
        list.add("sq_____Reklamë test : Effortless File Handling");
        list.add("sq_____Reklamë test : Filestack File Uploader API");
        list.add("el_____Δοκιμαστική διαφήμιση : Filestack File Uploader API");
        list.add("el_____Δοκιμαστική διαφήμιση : Filestack: Transform Now");
        list.add("sq_____Reklamë test : Effortless File Handling");
        list.add("sl_____Preizkusni oglas : Filestack: Empower Your Data");
        list.add("sr_____Probni oglas : Filestack File Uploader API");
        list.add("fi_____Testimainos : Effortless File Handling");
        list.add("sv_____Testannons : Filestack: Transform Now");
        list.add("tr_____[Quà tặng không bán] Túi đựng đồ...");
        list.add("tr_____Test Reklamı : Filestack: Empower Your Data");
        list.add("en_____Test Ad : Effortless File Handling");
        list.add("bg_____Тестова реклама : Effortless File Handling");
        list.add("mk_____Тестирај ја рекламата : Filestack: Empower Your Data");
        list.add("ru_____Тестовое объявление : Powerful & Easy To Use API");
        list.add("sr_____Пробни оглас : Filestack: Transform Now");
        list.add("sr_____Пробни оглас : Effortless File Handling");
        list.add("uk_____Тестове оголошення : Filestack: Transform Now");
        list.add("kk_____Сынақ жарнама : Filestack: Transform Now");
        list.add("iw_____מודעת בדיקה : Filestack: Empower Your Data");
        list.add("iw_____מודעת בדיקה : Effortless File Handling");
        list.add("ar_____מודעת בדיקה : Powerful & Easy To Use API");
        list.add("mr_____चाचणी जाहिरात : Filestack: Transform Now");
        list.add("as_____পৰীক্ষণ বিজ্ঞাপন : Filestack: Transform Now");
        list.add("or_____ପରୀକ୍ଷାମୂଳକ ବିଜ୍ଞାପନ : Filestack: Empower Your Data");
        list.add("ta_____சோதனை விளம்பரம் : Filestack: Transform Now");
        list.add("te_____ప్రకటనను పరీక్షించండి : Powerful & Easy To Use API");
        list.add("my_____စမ်းသပ်ကြော်ငြာ : Powerful & Easy To Use API");
        list.add("kn_____ಪರೀಕ್ಷಾ ಜಾಹೀರಾತು : Filestack: Empower Your Data");
        list.add("my_____စမ်းသပ်ကြော်ငြာ : Powerful & Easy To Use API");
        list.add("hi_____जाँच विज्ञापन : Filestack: Empower Your Data");
        list.add("ar_____إعلان تجريبي : Filestack File Uploader API");
        list.add("en_____Test Ad : Filestack File Uploader API");
        list.add("en_____Test Ad : Filestack: Transform Now");
        list.add("en_____Test Ad : Filestack File Uploader API");
        list.add("en_____Test Ad : Filestack: Transform Now");
        list.add("en_____Test Ad : Filestack File Uploader API");
        list.add("en_____Test Ad : Filestack File Uploader API");
        list.add("en_____Test Ad : Effortless File Handling");
        list.add("en_____Test Ad : Filestack: Empower Your Data");
        list.add("en_____Test Ad : Filestack File Uploader API");
        list.add("ar_____إعلان تجريبي : Filestack: Empower Your Data");
        list.add("ar_____إعلان تجريبي : Filestack: Empower Your Data");
        list.add("bs_____Probni oglas : Filestack: Empower Your Data");
        list.add("en_____Test Ad : Powerful & Easy To Use API");
        return list;
    }

    public static void appendOrCreateTextFileInDownload(String content) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        String log =  locale.getLanguage() + SPACE + content;

        String fileName = "CheckAdsAll";
        // Kiểm tra trạng thái lưu trữ ngoài
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //Toast.makeText(context, "External storage is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đường dẫn đến thư mục Download
        String downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        // Tạo tệp mới hoặc mở tệp trong thư mục Download
        File file = new File(downloadDirectory +"/BabyPhoto" , fileName + ".txt");

        FileOutputStream fileOutputStream = null;
        try {
            // Mở FileOutputStream ở chế độ append
            fileOutputStream = new FileOutputStream(file, true);

            // Thêm dòng mới nếu tệp đã tồn tại và không rỗng
            if (file.exists() && file.length() > 0) {
                fileOutputStream.write("\n".getBytes());
            }

            // Ghi nội dung vào tệp
            fileOutputStream.write(log.getBytes());
            fileOutputStream.flush();

            // Log value
            Log.d("CheckAds_" + "NativeAdAdmob", log);

            //Toast.makeText(context, "Content written to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(context, "Failed to write to file", Toast.LENGTH_SHORT).show();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getNativeInfo(NativeAdView adView){
        try{
            TextView headlineView = adView.findViewById(R.id.ad_headline);
            return headlineView.getText().toString();
        }catch (Exception e){
            return "";
        }
    }




    public Bitmap captureView1(View view) {
        ViewParent parent = view.getParent();
        ViewParent parent1 = parent.getParent();
        if (parent1 instanceof ViewGroup) {
            ViewGroup parentView = (ViewGroup) parent;

            // Tạo bitmap từ View cha
            Bitmap bitmap = Bitmap.createBitmap(parentView.getWidth(), parentView.getHeight(), Bitmap.Config.ARGB_8888);
            return bitmap;
        } else {
            //Toast.makeText(this, "No parent view available", Toast.LENGTH_SHORT).show();
        }
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
        return null;
    }

    public void saveBitmap1(Bitmap bitmap) {
        String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
        String downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(downloadDirectory + "/BabyPhoto", fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            //Toast.makeText(this, "Image saved to Download folder", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
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
}
