package eu.scy.client.desktop.scydesktop.hacks;

public static def osName = FX.getProperty("javafx.os.name");
public static def IS_LINUX = osName.contains("inux");
public static def IS_MAC = osName.contains("mac");
public static def IS_WINDOWS = osName.contains("indows");
public static def IS_SOLARIS = osName.contains("olaris");

/**
 * Is the version of the running JDK at least major.minor.micro_update?
 * In 1.6.0_18 macro=1,minor=6,micro=0,update=18
 */
public function jdkAtLeast(macro: Integer, minor: Integer, micro: Integer, update: Integer): Boolean {
    def runtimeVersion = java.lang.System.getProperty("java.runtime.version");
    def pattern = java.util.regex.Pattern.compile("^(\\d)\\.(\\d)\\.(\\d)_(\\d+)-");
    def matcher = pattern.matcher(runtimeVersion);
    if (matcher.find()) {
        def currentMacro = Integer.valueOf(matcher.group(1));
        def currentMinor = Integer.valueOf(matcher.group(2));
        def currentMicro = Integer.valueOf(matcher.group(3));
        def currentUpdate = Integer.valueOf(matcher.group(4));
        if (currentMacro < macro or currentMinor < minor or currentMicro < micro or currentUpdate < update) {
            return false;
        }
    }
    true
}

public static function fixTransparency() {
    if (IS_LINUX and jdkAtLeast(1, 6, 0, 14)) {
        java.lang.System.setProperty("javafx.allowTransparentStage", "true");
    }
}
