package com.auefly.spring.boot.security.service;

public interface TextTranslatorService {
    default String translateText(String text) {
        return translateText(text, "zh", "en");
    }

    String translateText(String text, String sourceLanguageCode, String targetLanguageCode);
}