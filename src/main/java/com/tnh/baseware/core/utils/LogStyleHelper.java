package com.tnh.baseware.core.utils;

public class LogStyleHelper {

    private LogStyleHelper() {
    }

    public static String info(String message) {
        return "✅ " + message;
    }

    public static String warn(String message) {
        return "⚠️  " + message;
    }

    public static String error(String message) {
        return "❌ " + message;
    }

    public static String debug(String message) {
        return "🐞 " + message;
    }

    public static String trace(String message) {
        return "👣 " + message;
    }

    public static String auth(String message) {
        return "🔐 " + message;
    }

    public static String syncing(String message) {
        return "🔄 " + message;
    }

    public static String sending(String message) {
        return "📤 " + message;
    }

    public static String receiving(String message) {
        return "📥 " + message;
    }

    public static String success(String message) {
        return "✨ " + message;
    }

    public static String crash(String message) {
        return "🔥 " + message;
    }

    public static String unauthorized(String message) {
        return "🔒 " + message;
    }

    public static String route(String message) {
        return "🔍 " + message;
    }

    public static String outbound(String message) {
        return "📡 " + message;
    }

    public static String stop(String message) {
        return "🛑 " + message;
    }
}
