package io.github.chase22.telegram.pumpkinbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FilesConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesConfig.class);

    private final String keystorePath;
    private final String certificatePath;

    public FilesConfig(PumpkinConfig pumpkinConfig) throws IOException {
        this.keystorePath = copyRessourceToFile(pumpkinConfig.getKeystoreResource()).getAbsolutePath();
        this.certificatePath = copyRessourceToFile(pumpkinConfig.getCertificateResource()).getAbsolutePath();
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    private File copyRessourceToFile(String resource) throws IOException {
        Path tempFilePath = Files.createTempFile(resourceToPrefix(resource), null);
        LOGGER.info("Copying " + resource + " to temporary file " + tempFilePath.toString());
        Files.copy(FilesConfig.class.getResourceAsStream(resource), tempFilePath, REPLACE_EXISTING);
        return tempFilePath.toFile();
    }

    private String resourceToPrefix(final String resource) {
        return resource.replace('.', '_').substring(1);
    }
}
