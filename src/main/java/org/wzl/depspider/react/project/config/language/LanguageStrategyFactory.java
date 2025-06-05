package org.wzl.depspider.react.project.config.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jsx所用语言策略工厂
 *
 * @author weizhilong
 */
public class LanguageStrategyFactory {

    private static final Map<Language, LanguageStrategy> LANGUAGE_STRATEGY_MAP = new HashMap<>();

    static {
        LANGUAGE_STRATEGY_MAP.put(Language.JS, new JSLanguageStrategy());
        LANGUAGE_STRATEGY_MAP.put(Language.TS, new TSLanguageStrategy());
    }

    /**
     * 获取语言策略
     * @param language  语言枚举
     * @return          策略类
     */
    public static LanguageStrategy getLanguageStrategy(Language language) {
        return LANGUAGE_STRATEGY_MAP.get(language);
    }

    public static List<LanguageStrategy> getLanguageStrategies(Set<Language> languages) {
        List<LanguageStrategy> strategies = new ArrayList<>();
        for (Language lang : languages) {
            LanguageStrategy strategy = LANGUAGE_STRATEGY_MAP.get(lang);
            if (strategy != null) {
                strategies.add(strategy);
            }
        }
        return strategies;
    }


}
