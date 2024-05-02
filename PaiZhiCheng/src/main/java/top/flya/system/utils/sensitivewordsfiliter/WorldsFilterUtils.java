package top.flya.system.utils.sensitivewordsfiliter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User:azu<azu@eventslack.com>
 * Date: 2021/5/19
 * Time:16:29
 */
@Slf4j
public class WorldsFilterUtils {


    private static final List<String> blackWordsList = new ArrayList<>();


    public static void initBlackWordsList() {
        try {
            Resource resource = new ClassPathResource("wordsfilter/sensi_words2.txt");
            if (!resource.exists()) {
                log.error("未找到黑词文件库");
                return;
            }
            File file = FileUtil.writeFromStream(resource.getInputStream(), FileUtil.createTempFile());
            List<String> keyList = FileUtil.readUtf8Lines(file);
            blackWordsList.addAll(keyList);
            log.info("加载黑词库行数 => {}", keyList.size());
        } catch (Exception e) {
            log.error("黑词库加载失败", e);
        }
    }




    private static boolean wordsCheck(List<String> worlds, String content) {
        WordsSearch iwords = new WordsSearch();
        iwords.SetKeywords(worlds);
        return iwords.ContainsAny(content);
    }


    public static boolean checkBySystemWords(String content) {
        loadSystemKeywords();
        return wordsCheck(blackWordsList, content);
    }


    public synchronized static void loadSystemKeywords() {
        if (!blackWordsList.isEmpty()) {
            return;
        }
        initBlackWordsList();
    }

    public synchronized static void forceLoadSystemKeywords() {
        blackWordsList.clear();
        initBlackWordsList();
    }

}
