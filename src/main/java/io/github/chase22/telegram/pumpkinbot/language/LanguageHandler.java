package io.github.chase22.telegram.pumpkinbot.language;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
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

    public boolean containsPumpkin(String text) {
        return languageList.stream().filter(language -> text.contains(language.getWord())).peek(language ->
                LOGGER.info("Language {} with word {} matched {}", language.getLanguage(), language.getWord(), text)
        ).count() > 0;
    }

    public int getLanguageCount() {
        return languageList.size();
    }
}
