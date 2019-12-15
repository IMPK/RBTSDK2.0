# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

######### START COMMON ############
# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions
# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes java.lang.annotation.*
-keepattributes javax.annotations.*
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-dontwarn sun.misc.**
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
########## END COMMON ##########


########## START AOSP ##########
# Preference objects are inflated via reflection
-keep public class android.support.v7.preference.Preference {
  public <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.support.v7.preference.Preference {
  public <init>(android.content.Context, android.util.AttributeSet);
}
########## END AOSP ##########


########## START EVENTBUS ##########
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}
########## END EVENTBUS ##########

########## START GSON ##########
# Gson specific classes
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
########## END GSON ##########


########## START RETROFIT2:RETROFIT ############
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions
#
#-keepclasseswithmembers class * {
#    @retrofit2.com.onmobile.rbt.baseline.http.* <methods>;
#}

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
# keep initializing and calling class sequesnce
-keepattributes InnerClasses
-printmapping out.map
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated

# Add project specific ProGuard rules here.
-assumenosideeffects class android.util.Log {
   public static boolean isLoggable(java.lang.String, int);
   public static int v(...);
   public static int i(...);
   public static int w(...);
   public static int d(...);
   public static int e(...);
}

-dontwarn org.ini4j.** # Ignore warning for missing classes in ini4j
-dontwarn javax.**
-keep class android.support.v7.widget.SearchView { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclasseswithmembers class * {
   @retrofit2.com.onmobile.rbt.baseline.http.* <methods>;
}
##---------------End: proguard configuration for Gson  ----------
########## END RETROFIT2:RETROFIT ###########


######### START OKHTTP3 ############
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**
######### START OKHTTP3 ############


######### START KOTLIN ############
-dontwarn kotlin.reflect.jvm.internal.impl.descriptors.CallableDescriptor
-dontwarn kotlin.reflect.jvm.internal.impl.descriptors.ClassDescriptor
-dontwarn kotlin.reflect.jvm.internal.impl.descriptors.ClassifierDescriptorWithTypeParameters
-dontwarn kotlin.reflect.jvm.internal.impl.descriptors.annotations.AnnotationDescriptor
-dontwarn kotlin.reflect.jvm.internal.impl.descriptors.impl.PropertyDescriptorImpl
-dontwarn kotlin.reflect.jvm.internal.impl.load.java.JavaClassFinder
-dontwarn kotlin.reflect.jvm.internal.impl.resolve.OverridingUtil
-dontwarn kotlin.reflect.jvm.internal.impl.types.DescriptorSubstitutor
-dontwarn kotlin.reflect.jvm.internal.impl.types.DescriptorSubstitutor
-dontwarn kotlin.reflect.jvm.internal.impl.types.TypeConstructor
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
######### END KOTLIN ############


######### START GLIDE ############
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
 **[] $VALUES;
 public *;
}
#-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
######### END GLIDE ############


######### START CRASHLYTICS ############
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
######### END CRASHLYTICS ############


######### START HOCKEYAPP ############
-keepclassmembers class net.hockeyapp.android.UpdateFragment {
  *;
}
######### END HOCKEYAPP ############


######### START ONESIGNAL ############
-dontwarn com.onesignal.**
# These 2 methods are called with reflection.
-keep class com.google.android.gms.common.api.GoogleApiClient {
    void connect();
    void disconnect();
}
-keep public interface android.app.OnActivityPausedListener {*;}
-keep class com.onesignal.ActivityLifecycleListenerCompat** {*;}
# Observer backcall methods are called with reflection
-keep class com.onesignal.OSSubscriptionState {
    void changed(com.onesignal.OSPermissionState);
}
-keep class com.onesignal.OSPermissionChangedInternalObserver {
    void changed(com.onesignal.OSPermissionState);
}
-keep class com.onesignal.OSSubscriptionChangedInternalObserver {
    void changed(com.onesignal.OSSubscriptionState);
}
-keep class ** implements com.onesignal.OSPermissionObserver {
    void onOSPermissionChanged(com.onesignal.OSPermissionStateChanges);
}
-keep class ** implements com.onesignal.OSSubscriptionObserver {
    void onOSSubscriptionChanged(com.onesignal.OSSubscriptionStateChanges);
}
-keep class com.onesignal.shortcutbadger.impl.AdwHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.ApexHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.AsusHomeLauncher { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.DefaultBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.EverythingMeHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.HuaweiHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.LGHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.NewHtcHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.NovaHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.OPPOHomeBader { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.SamsungHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.SonyHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.VivoHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.XiaomiHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.ZukHomeBadger { <init>(...); }
-dontwarn com.amazon.**
# Proguard ends up removing this class even if it is used in AndroidManifest.xml so force keeping it.
-keep public class com.onesignal.ADMMessageHandler {*;}
-keep class com.onesignal.JobIntentService$* {*;}
-keep class com.onesignal.UpgradeReceiver* {*;}
######### END ONESIGNAL ############


######### START BLURVIEW ############
-dontnote eightbitlab.com.blurview.**
######### END BLURVIEW ############


######### START YOUTUBE PLAYER ############
-dontnote com.pierfrancescosoffritti.androidyoutubeplayer.**
######### END YOUTUBE PLAYER ############


######### START GUAVA ############
-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**
-dontwarn com.google.errorprone.**
-dontwarn sun.misc.Unsafe
-dontwarn java.lang.ClassValue
######### END GUAVA ############


######### START PLAY SERVICE GMS ############
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}
######### END PLAY SERVICE GMS ############


######### START JOBQUEUE ############
-keep interface com.birbit.android.jobqueue.** { *; }
######### END JOBQUEUE ############


#### START FIREBASE ####
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}
-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}
-keep class com.google.firebase.provider.** { *; }
-keep class com.google.firebase.messaging.**  { *; }
#### END FIREBASE ####


######### START BASELINE ############

#Removing classes from build
#-keep class !com.onmobile.rbt.baseline.BuildConfig { *; }

# Application classes that will be serialized/deserialized
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.Configuration { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.dtos.** { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.catalogapis.ChartQueryParameters { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.catalogapis.DynamicChartQueryParameters { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.catalogapis.RecommnedQueryParameters { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.catalogapis.SearchAPIRequestParameters { *; }

-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.CreateUserDefinedPlaylistQueryParams { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.AddContentToUDPQueryParameters { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.DigitalStarQueryParams { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.FeedBackRequestParameters { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.UserHistoryQueryParameters { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.UserSubscriptionQueryParams { *; }

-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.BatchItemResponseDTO { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.BatchRequestItemDTO { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.ListOfRequestBatchItemsDTO { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.ListOfResponseBatchItemsDTO { *; }

-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.CallingParty { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiAssetDto { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiBillingInfoDto { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboAPIExtraInfoDto { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiPlayRuleDto { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiPurchaseDto { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiSubscriptionDto { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.Discount { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequestDTO { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequestParameters { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO { *; }
-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ScheduleDTO { *; }


#-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.errormodule.** { *; }
#-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.AddContentToUDPQueryParameters { *; }
#-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.FeedBackRequestParameters { *; }
#-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.** { *; }
#-keep class com.onmobile.baseline.com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.** { *; }
# Applcation serializable/deserializable
-keep class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# Some application classes
-keep class com.onmobile.rbt.baseline.widget.cardswiper.DefaultAnimation { *; }
-keep class com.onmobile.rbt.baseline.widget.cardswiper.DefaultLayout { *; }
-keep class com.onmobile.rbt.baseline.widget.cardswiper.StackAnimation { *; }
-keep class com.onmobile.rbt.baseline.widget.cardswiper.StackLayout { *; }
-keep class com.onmobile.rbt.baseline.util.BottomNavigationViewHelper { *; }
-keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
    boolean mShiftingMode;
}
######### END BASELINE ############

######### START CleverTap ############
-dontwarn com.clevertap.android.sdk.**
######### END CleverTap ############