package io.github.chase22.telegram.pumpkinbot.language;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class LanguageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageHandler.class);

    private final List<Language> languageList;

    public LanguageHandler() {
        List<Language> languageList;

        try {
            languageList = new ObjectMapper().readValue(LanguageHandler.class.getResource("/pumpkin.json"),
                    new TypeReference<List<Language>>(){});

            LOGGER.info("Loaded {} languages", languageList.size());

        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error loading languages. Using fallback", e);
            languageList = singletonList(new Language("en", "United Kingdom", "pumpkin"));
        }

        this.languageList = languageList;
    }


    public String getLanguages() {
        return languageList.stream().map(Language::getLanguage).collect(Collectors.joining(System.lineSeparator()));
    }

    public int countPumpkin(String text) {
        return languageList.stream().mapToInt(language -> countSubstring(language.getWord(), text)).sum();
    }

    public int countSubstring(String subStr, String str){
        return str.split(Pattern.quote(subStr), -1).length - 1;
    }

    public int getLanguageCount() {
        return languageList.size();
    }
}
