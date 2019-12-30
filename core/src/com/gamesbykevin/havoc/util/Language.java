package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.gamesbykevin.havoc.preferences.AppPreferences;

import java.util.Locale;

import static com.gamesbykevin.havoc.preferences.AppPreferences.getPreferenceValue;

public class Language {

    public static final String KEY_PROGRESS_STEP_1 = "progressStep1";
    public static final String KEY_PROGRESS_STEP_2 = "progressStep2";
    public static final String KEY_PROGRESS_STEP_3 = "progressStep3";
    public static final String KEY_PROGRESS_STEP_4 = "progressStep4";
    public static final String KEY_PROGRESS_STEP_5 = "progressStep5";
    public static final String KEY_PROGRESS_STEP_6 = "progressStep6";
    public static final String KEY_PROGRESS_STEP_7 = "progressStep7";

    public static final String KEY_OVERLAY_PAUSED = "overlayPaused";

    public static final String KEY_LEVEL_SELECT_TITLE = "levelSelectScreenTitleText";
    public static final String KEY_LEVEL_SELECT_SOLVED = "levelSelectScreenSolved";

    public static final String KEY_MENU_PLAY = "menuScreenPlay";
    public static final String KEY_MENU_OPTIONS = "menuScreenOptions";
    public static final String KEY_MENU_RATE = "menuScreenRate";
    public static final String KEY_MENU_MORE = "menuScreenMore";
    public static final String KEY_MENU_EXIT = "menuScreenExit";

    public static final String KEY_OPTIONS_BACK = "optionsScreenBack";
    public static final String KEY_OPTIONS_MUSIC = "optionsScreenMusic";
    public static final String KEY_OPTIONS_SOUND = "optionsScreenSound";
    public static final String KEY_OPTIONS_VIBRATE = "optionsScreenVibrate";

    public static final String KEY_NOTIFICATION_LEVEL_COMPLETE = "notificationMessageLevelComplete";
    public static final String KEY_NOTIFICATION_SMG = "notificationMessageSmg";
    public static final String KEY_NOTIFICATION_IMPACT = "notificationMessageImpact";
    public static final String KEY_NOTIFICATION_MAGNUM = "notificationMessageMagnum";
    public static final String KEY_NOTIFICATION_BUZZ = "notificationMessageBuzz";
    public static final String KEY_NOTIFICATION_SHOTGUN = "notificationMessageShotgun";
    public static final String KEY_NOTIFICATION_AMMO = "notificationMessageAmmo";
    public static final String KEY_NOTIFICATION_HEALTH_LARGE = "notificationMessageHealthLarge";
    public static final String KEY_NOTIFICATION_HEALTH_SMALL = "notificationMessageHealthSmall";
    public static final String KEY_NOTIFICATION_KEY = "notificationMessageKey";
    public static final String KEY_NOTIFICATION_LOCKED = "notificationMessageLocked";
    public static final String KEY_NOTIFICATION_SECRET = "notificationMessageSecret";
    public static final String KEY_NOTIFICATION_DEAD = "notificationMessageDead";
    public static final String KEY_NOTIFICATION_LOW_HEALTH = "notificationMessageLowHealth";
    public static final String KEY_STAT_TIME = "statTime";
    public static final String KEY_STAT_ENEMY = "statEnemy";
    public static final String KEY_STAT_SECRET = "statSecret";
    public static final String KEY_STAT_ITEM = "statItem";

    //object used for localization
    private static I18NBundle MY_BUNDLE;

    public static final String PATH_BUNDLE = "i18n/MyBundle";

    //languages commented out here we don't have the character set for
    public enum Languages {

        Afrikaans("Afrikaans", "af", ""),
        Albanian("Albanian", "sq", ""),
        //Amharic("Amharic", "am", ""),
        //Arabic("Arabic", "ar", ""),
        //Armenian("Armenian", "hy", ""),
        Azerbaijani("Azerbaijani", "az", ""),
        Basque("Basque", "eu", ""),
        //Belarusian("Belarusian", "be", ""),
        //Bengali("Bengali", "bn", ""),
        Bosnian("Bosnian", "bs", ""),
        //Bulgarian("Bulgarian", "bg", ""),
        Catalan("Catalan", "ca", ""),
        Cebuano("Cebuano", "ceb", ""),
        //ChineseSimplified("Chinese (Simplified)", "zh", "CN"),
        //ChineseTraditional("Chinese (Traditional)", "zh", "TW"),
        Corsican("Corsican", "co", ""),
        Croatian("Croatian", "hr", ""),
        Czech("Czech", "cs", ""),
        Danish("Danish", "da", ""),
        Dutch("Dutch", "nl", ""),
        English("English", "en", ""),
        Esperanto("Esperanto", "eo", ""),
        Estonian("Estonian", "et", ""),
        Finnish("Finnish", "fi", ""),
        French("French", "fr", ""),
        Frisian("Frisian", "fy", ""),
        Galician("Galician", "gl", ""),
        //Georgian("Georgian", "ka", ""),
        German("German", "de", ""),
        //Greek("Greek", "el", ""),
        //Gujarati("Gujarati", "gu", ""),
        Haitian("Haitian Creole", "ht", ""),
        Hausa("Hausa", "ha", ""),
        Hawaiian("Hawaiian", "haw", "639"),
        //Hebrew("Hebrew", "he", ""),
        //Hindi("Hindi", "hi", ""),
        Hmong("Hmong", "hmn", "639"),
        Hungarian("Hungarian", "hu", ""),
        Icelandic("Icelandic", "is", ""),
        Igbo("Igbo", "ig", ""),
        Indonesian("Indonesian", "id", ""),
        Irish("Irish", "ga", ""),
        Italian("Italian", "it", ""),
        //Japanese("Japanese", "ja", ""),
        Javanese("Javanese", "jw", ""),
        //Kannada("Kannada", "kn", ""),
        //Kazakh("Kazakh", "kk", ""),
        //Khmer("Khmer", "km", ""),
        //Korean("Korean", "ko", ""),
        Kurdish("Kurdish", "ku", ""),
        //Kyrgyz("Kyrgyz", "ky", ""),
        //Lao("Lao", "lo", ""),
        Latin("Latin", "la", ""),
        Latvian("Latvian", "lv", ""),
        Lithuanian("Lithuanian", "lt", ""),
        Luxembourgish("Luxembourgish", "lb", ""),
        //Macedonian("Macedonian", "mk", ""),
        Malagasy("Malagasy", "mg", ""),
        Malay("Malay", "ms", ""),
        //Malayalam("Malayalam", "ml", ""),
        Maltese("Maltese", "mt", ""),
        Maori("Maori", "mi", ""),
        //Marathi("Marathi", "mr", ""),
        //Mongolian("Mongolian", "mn", ""),
        //Myanmar("Myanmar (Burmese)", "my", ""),
        //Nepali("Nepali", "ne", ""),
        Norwegian("Norwegian", "no", ""),
        Nyanja("Nyanja (Chichewa)", "ny", ""),
        //Pashto("Pashto", "ps", ""),
        //Persian("Persian", "fa", ""),
        Polish("Polish", "pl", ""),
        Portuguese("Portuguese", "pt", ""),
        //Punjabi("Punjabi", "pa", ""),
        Romanian("Romanian", "ro", ""),
        //Russian("Russian", "ru", ""),
        Samoan("Samoan", "sm", ""),
        ScotsGaelic("Scots Gaelic", "gd", ""),
        //Serbian("Serbian", "sr", ""),
        Sesotho("Sesotho", "st", ""),
        Shona("Shona", "sn", ""),
        //Sindhi("Sindhi", "sd", ""),
        //Sinhala("Sinhala (Sinhalese)", "si", ""),
        Slovak("Slovak", "sk", ""),
        Slovenian("Slovenian", "sl", ""),
        Somali("Somali", "so", ""),
        Spanish("Spanish", "es", ""),
        Sundanese("Sundanese", "su", ""),
        Swahili("Swahili", "sw", ""),
        Swedish("Swedish", "sv", ""),
        Tagalog("Tagalog (Filipino)", "tl", ""),
        //Tajik("Tajik", "tg", ""),
        //Tamil("Tamil", "ta", ""),
        //Telugu("Telugu", "te", ""),
        //Thai("Thai", "th", ""),
        Turkish("Turkish", "tr", ""),
        //Ukrainian("Ukrainian", "uk", ""),
        //Urdu("Urdu", "ur", ""),
        Uzbek("Uzbek", "uz", ""),
        Vietnamese("Vietnamese", "vi", ""),
        Welsh("Welsh", "cy", ""),
        Xhosa("Xhosa", "xh", ""),
        //Yiddish("Yiddish", "yi", ""),
        Yoruba("Yoruba", "yo", ""),
        Zulu("Zulu", "zu", "");

        private final String desc;
        private final String languageCode;
        private final String countryCode;

        Languages(String desc, String languageCode, String countryCode) {
            this.desc = desc;
            this.languageCode = languageCode;
            this.countryCode = countryCode;
        }

        public String getDesc() {
            return this.desc;
        }

        public String getLanguageCode() {
            return this.languageCode;
        }

        public String getCountryCode() {
            return this.countryCode;
        }
    }

    public static void recycle() {
        MY_BUNDLE = null;
    }

    public static String getTranslatedText(String key) {
        return getMyBundle().get(key);
    }

    private static I18NBundle getMyBundle() {

        if (MY_BUNDLE == null) {

            //do we have a language setting
            final int index = getPreferenceValue(AppPreferences.PREF_LANGUAGE);

            //if we selected a language set it
            if (index >= 0) {
                changeMyBundle(index);
            } else {
                MY_BUNDLE = I18NBundle.createBundle(Gdx.files.internal(PATH_BUNDLE));
            }
        }

        return MY_BUNDLE;
    }

    public static void changeMyBundle(int index) {

        //list of languages
        Language.Languages[] languages = Language.Languages.values();

        //language settings
        final String countryCode = languages[index].getCountryCode();
        final String languageCode = languages[index].getLanguageCode();

        Locale locale;

        //create the locale
        if (countryCode != null && countryCode.length() > 0) {
            locale = new Locale(languageCode, countryCode);
        } else {
            locale = new Locale(languageCode);
        }

        //create new bundle with the specified language and country code
        MY_BUNDLE = I18NBundle.createBundle(Gdx.files.internal(PATH_BUNDLE), locale);
    }
}