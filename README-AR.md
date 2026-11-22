# Mein Mathetrainer – Android TV

مشروع Android بسيط يحزم نسخة HTML داخل WebView محلي؛ لا يحتاج التطبيق إلى الإنترنت أثناء التشغيل.

## بناء APK بأبسط طريقة (من المتصفح، دون Android Studio)

1. أنشئ حساباً أو سجّل الدخول في https://github.com
2. أنشئ مستودعاً جديداً باسم `MeinMathetrainer` واجعله Private أو Public.
3. فك ضغط ملف المشروع ZIP على الكمبيوتر.
4. افتح المستودع في GitHub، ثم اختر **Add file → Upload files**.
5. ارفع جميع محتويات مجلد `MeinMathetrainer-AndroidTV` (بما فيها مجلد `.github` المخفي). تأكد أن `app` وملفات `build.gradle` و`.github` في جذر المستودع.
6. اضغط **Commit changes** وانتظر انتهاء عملية البناء.
7. افتح تبويب **Actions** ثم مهمة **Build Android TV APK**.
8. افتح آخر تشغيل ناجح، وانزل إلى **Artifacts**، ثم نزّل `MeinMathetrainer-AndroidTV-APK`.
9. فك ضغط ملف Artifact؛ ستجد `app-debug.apk`.

## التثبيت على Xiaomi TV Box S (2nd Gen)

1. انسخ `app-debug.apk` إلى USB مهيأ FAT32 أو exFAT.
2. صِل USB بجهاز Xiaomi.
3. افتح مدير ملفات، واختر APK، ثم وافق على السماح بالتثبيت من هذا المصدر عند الطلب.
4. اختر Install ثم Open.

## ملاحظات
- هذه نسخة تجريبية Debug وليست إصداراً منشوراً على Google Play.
- التطبيق يعمل محلياً، ويحفظ التقدم في تخزين WebView على الجهاز.
- التطبيق يعلن عن نفسه كتطبيق Android TV عبر فئة LEANBACK_LAUNCHER.
- لا تشارك مفاتيح توقيع أو كلمات مرور GitHub.
