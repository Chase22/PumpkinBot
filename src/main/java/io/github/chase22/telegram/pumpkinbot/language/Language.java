package io.github.chase22.telegram.pumpkinbot.language;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class Language {
    private final String locale;
    private final String language;
    private final String word;

    @JsonCreator
    Language(
            @JsonProperty("locale") final String locale,
            @JsonProperty("country") final String language,
            @JsonProperty("string") final String word
    ) {
        this.locale = locale;
        this.language = language;
        this.word = word;
    }

    public String getLocale() {
        return locale;
    }

    public String getLanguage() {
        return language;
    }

    public String getWord() {
        return word;
    }
}
