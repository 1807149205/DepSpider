package org.wzl.depspider.react.project.config.language;

import java.io.File;
import java.util.List;

public class CompositeLanguageStrategy implements LanguageStrategy {

    private final List<LanguageStrategy> strategies;

    public CompositeLanguageStrategy(List<LanguageStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public File createNewChildIndexFile(File folder) {
        for (LanguageStrategy strategy : strategies) {
            File newChildIndexFile = strategy.createNewChildIndexFile(folder);
            if (null == newChildIndexFile) {
                continue;
            }
            if (newChildIndexFile.isFile()) {
                return newChildIndexFile;
            }
        }
        return null;
    }

    @Override
    public File createNewChildWithPrefix(File folder, String fileName) {
        for (LanguageStrategy strategy : strategies) {
            File newChildIndexFile = strategy.createNewChildWithPrefix(folder, fileName);
            if (null == newChildIndexFile) {
                continue;
            }
            if (newChildIndexFile.isFile()) {
                return newChildIndexFile;
            }
        }
        return null;
    }
}
