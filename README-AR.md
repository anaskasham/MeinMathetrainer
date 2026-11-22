# Mein Mathetrainer – Android TV (الإصدار 1.1)

نسخة Android TV مبنية على WebView محلي، وتعمل دون اتصال بالإنترنت بعد التثبيت.

## ما الذي تحسّن في الإصدار 1.1؟
- أصوات نجاح وخطأ حقيقية عبر Android ToneGenerator، مع بقاء خيار إيقاف الصوت.
- دعم أوضح لأسهم جهاز التحكم وEnter/OK، مع إطار تركيز مرئي.
- تخطيط متجاوب لشاشات التلفاز العريضة 16:9 والهواتف واللوحيات.
- إضافة أيقونة رياضيات/بومة للتطبيق.
- رفع إصدار التطبيق إلى 1.1.

## تحديث مستودع GitHub الحالي
لا تنشئ مستودعاً جديداً. حدّث الملفات التالية في المستودع الموجود:
1. استبدل `app/src/main/assets/index.html` بالنسخة الموجودة في هذا المشروع.
2. استبدل `app/src/main/java/de/meinmathetrainer/tv/MainActivity.java` بالنسخة الجديدة.
3. استبدل `app/src/main/AndroidManifest.xml` بالنسخة الجديدة.
4. أنشئ الملف `app/src/main/res/drawable/ic_launcher_foreground_legacy.xml` والصق محتوى الملف الموجود هنا.
5. استبدل `app/build.gradle` بالنسخة الجديدة.
6. حدّث ملف `.github/workflows/build-apk.yml` إلى النسخة الموجودة هنا (اختياري، لتحديث setup-java إلى v5).
ثم اضغط Commit changes. سيبدأ GitHub Actions بناء الإصدار الجديد تلقائياً.

## تنزيل APK
من المستودع افتح Actions، ثم آخر تشغيل ناجح، ثم نزّل Artifact باسم `MeinMathetrainer-AndroidTV-APK` وفك ضغطه. ستجد `app-debug.apk`.

## التثبيت
انسخ APK إلى USB، وصله بجهاز Xiaomi TV Box S (2nd Gen)، وافتحه بمدير الملفات ثم وافق على التثبيت من هذا المصدر عند الطلب.

ملاحظة: هذه نسخة Debug للاختبار وليست إصداراً منشوراً على Google Play.
