package org.wzl.depspider.react.project;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.react.dto.ProjectFileRelation;
import org.wzl.depspider.react.exception.ScanPathSetException;
import org.wzl.depspider.react.project.config.language.CompositeLanguageStrategy;
import org.wzl.depspider.react.project.config.language.Language;
import org.wzl.depspider.react.project.config.ProjectConfiguration;
import org.wzl.depspider.react.project.config.language.LanguageStrategy;
import org.wzl.depspider.react.project.config.language.LanguageStrategyFactory;
import org.wzl.depspider.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * React 项目操作类
 *
 * @author weizhilong
 */
@Slf4j
public class ReactProjectOperator {

    /**
     * 项目根目录
     */
    private final String projectPath;

    /**
     * 项目根目录
     */
    private final File projectFileFolder;

    /**
     * 项目配置
     */
    private final ProjectConfiguration projectConfiguration;

    /**
     * src目录
     */
    private final File srcFileFolder;

    /**
     * src下属的所有目录
     */
    private final List<File> srcFolderChildren;

    /**
     * 扫描路径
     * ProjectConfiguration#scanPath 可配置
     */
    private File scanPath;

    /**
     * 语言策略类
     */
    private LanguageStrategy languageStrategy;

    /**
     * 构造函数
     * @param projectPath 项目根目录
     * @param projectConfiguration 项目配置
     */
    public ReactProjectOperator(String projectPath,
                                ProjectConfiguration projectConfiguration) {
        this.projectPath = projectPath;
        this.projectFileFolder = new File(projectPath);
        this.projectConfiguration = projectConfiguration;
        this.srcFileFolder = new File(projectPath, "src");
        this.srcFolderChildren = new ArrayList<>();
        for (File file : Objects.requireNonNull(this.srcFileFolder.listFiles())) {
            if (file.isDirectory()) {
                this.srcFolderChildren.add(file);
            }
        }
        log.info("JSXProjectOperator initialized with project path: {}", projectPath);
        initProject();
    }

    private void initProject() {
        setScanPath();
        setLanguageStrategy();
    }

    private void setLanguageStrategy() {
        Set<Language> languages = projectConfiguration.getLanguages();
        List<LanguageStrategy> languageStrategies = new ArrayList<>();
        for (Language language : languages) {
            LanguageStrategy languageStrategy1 = LanguageStrategyFactory.getLanguageStrategy(language);
            if (null != languageStrategy1) {
                languageStrategies.add(languageStrategy1);
            }
        }
        if (languageStrategies.size() > 1) {
            languageStrategy = new CompositeLanguageStrategy(languageStrategies);
        } else {
            languageStrategy = languageStrategies.get(0);
        }
    }

    private void setScanPath() {
        List<String> projectConfigurationScanPath = projectConfiguration.getScanPath();
        if (null != projectConfigurationScanPath && !projectConfigurationScanPath.isEmpty()) {
            this.scanPath = FileUtil.resolvePath(this.projectFileFolder, projectConfigurationScanPath);
            if (!this.scanPath.isDirectory()) {
                throw new ScanPathSetException("Scan path is not a directory: " + projectConfigurationScanPath);
            }
        } else {
            // 默认从src开始扫描
            this.scanPath = this.srcFileFolder;
        }
    }

    /**
     * 获取项目文件关系
     * 通过一个文件的import来判断
     * @return  项目文件关系列表
     */
    public List<ProjectFileRelation> projectFileRelation() {
        File file;
        if (null == this.scanPath) {
            file = new File(this.srcFileFolder.getPath());
        } else {
            file = new File(this.scanPath.getPath());
        }
        List<ProjectFileRelation> projectFileRelations = new ArrayList<>();
        this._projectFileRelation(file, projectFileRelations);
        return projectFileRelations;
    }

    private void _projectFileRelation(File file, List<ProjectFileRelation> projectFileRelations) {
        if (file.isDirectory()) {
            for (File childFile : Objects.requireNonNull(file.listFiles())) {
                _projectFileRelation(childFile, projectFileRelations);
            }
        } else if (file.isFile()){
            ProjectFileRelation projectFileRelation = new ProjectFileRelation();
            projectFileRelation.setTargetFilePath(file);
            List<File> relationFiles = new ArrayList<>();
            if (file.getPath().endsWith(".jsx")) {
                JSXFileOperation jsxFileOperation = new JSXFileOperation(
                        file.getAbsolutePath()
                );
                List<JSXFileOperation.ImportInfo> importInfos = jsxFileOperation.importInfo();
                for (JSXFileOperation.ImportInfo importInfo : importInfos) {
                    String source = importInfo.getSource();
                    boolean projectImport = isProjectImport(source);
                    if (projectImport) {
                        File relativeFile = findFileBySource(source, file);
                        if (null != relativeFile) {
                            relationFiles.add(relativeFile);
                        }
                    }
                }
            }
            projectFileRelation.setRelationFilePaths(relationFiles);
            projectFileRelations.add(projectFileRelation);
        }
    }

    /**
     * 根据模块路径， 找到对应的文件
     */
    private File findFileBySource(String source, File curFile) {
        File relativeFile;
        Set<Language> languages = projectConfiguration.getLanguages();
        //导入有三种规则
        // 1、../components/CommonCard
        // 2、../components/CommonCard/index.jsx
        // 3、../components/CommonCard/index
        if (source.startsWith("@")) {
            String[] split = source.split("/");
            File currentFile = srcFileFolder;
            for (int i = 1 ; i < split.length; i++) {
                String folder = split[i];

                if (i == split.length - 1) {
                    //当导入规则为2时，直接返回
                    if (folder.contains(".")) {
                        relativeFile = new File(currentFile, folder);
                        return relativeFile;
                    }
                    //当第3种情况时
                    File currentFile1 = languageStrategy.createNewChildWithPrefix(currentFile, folder);
                    if (null != currentFile1) {
                        return currentFile1;
                    }

                    //第1种可能
                    currentFile = new File(currentFile, folder);
                    currentFile1 = languageStrategy.createNewChildIndexFile(currentFile);
                    if (null != currentFile1) {
                        return currentFile1;
                    }
                } else {
                    currentFile = new File(currentFile, folder);
                }
            }

            return languageStrategy.createNewChildIndexFile(currentFile);
        } else if (source.startsWith("../")) {
            File parentFile = curFile;
            parentFile = parentFile.getParentFile();
            String[] split = source.split("/");

            for (int i = 0 ; i < split.length ; i++) {
                String str = split[i];

                if (str.equals("..")) {
                    parentFile = parentFile.getParentFile();
                    continue;
                }

                if (i == split.length - 1) {
                    //当导入规则为2时，直接返回
                    if (str.contains(".")) {
                        relativeFile = new File(parentFile, str);
                        return relativeFile;
                    }

                    //当第3种情况时
                    File currentFile = languageStrategy.createNewChildIndexFile(parentFile);
                    if (null != currentFile) {
                        return currentFile;
                    }

                    //第1种可能
                    parentFile = new File(parentFile, str);
                    currentFile = languageStrategy.createNewChildIndexFile(parentFile);
                    if (null != currentFile) {
                        return currentFile;
                    }
                } else {
                    parentFile = new File(parentFile, str);
                }
            }
        } else if (source.startsWith("./")) {
            File currentFile = curFile;
            File parentFile = currentFile.getParentFile();
            String[] split = source.split("/");
            for (int i = 1 ; i < split.length ; i++) {
                parentFile = new File(parentFile, split[i]);
            }
            if (languages.contains(Language.TS)) {
                relativeFile = new File(parentFile, "index.tsx");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
                relativeFile = new File(parentFile, "index.ts");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
            }
            if (languages.contains(Language.JS)) {
                relativeFile = new File(parentFile, "index.jsx");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
                relativeFile = new File(parentFile, "index.js");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
            }
        }
        return null;
    }

    /**
     * 判断一条import语句中， 是否为项目文件
     * @param importPath    导入 的模块路径
     */
    private boolean isProjectImport(String importPath) {
        //如果是@开头的，有可能导入node_modules中的文件，也有可能导入项目中的那文件
        if (importPath.startsWith("@")) {
            String[] split = importPath.split("/");
            split[0] = split[0].substring(1); // 去掉@符号
            return dfsFileHasProject(srcFileFolder, 1, split);
        }
        return importPath.startsWith("./")
                || importPath.startsWith("../")
                || importPath.startsWith("/")
                || importPath.startsWith("src/")
                || importPath.endsWith(".css")
                || importPath.endsWith(".less")
                || importPath.endsWith(".scss");
    }

    /**
     * 通过dfs方式寻找是否在根目录下，是否存在路径符合split路径的文件。
     * eg: file: 是一个项目的根目录
     * folders: ["src", "components", "CommonCard"]
     * 那么他会寻找 这个项目根目录开始，是否存在 /src/components/CommonCard/ 这个路径。
     * @param file          文件寻找的根目录
     * @param index         folders的下标
     * @param folders       导入的路径
     * @return              是否存在符合路径的文件
     */
    private boolean dfsFileHasProject(File file, int index, String[] folders) {
        if (index >= folders.length) {
            return true;
        }

        File[] children = file.listFiles();
        if (children == null) {
            return false;
        }

        if (index == folders.length - 1) {
            for (File child : children) {
                if (child.isFile()) {
                    String fileName = child.getName().split("\\.")[0];
                    if (fileName.equals(folders[index])) {
                        return true;
                    }
                } else if (child.getName().equals(folders[index])) {
                    File newChildIndexFile = languageStrategy.createNewChildIndexFile(file);
                    if (null == newChildIndexFile) {
                        continue;
                    }
                    return newChildIndexFile.isFile();
                }
            }
        } else {
            for (File child : children) {
                if (child.isDirectory() && child.getName().equals(folders[index])) {
                    return dfsFileHasProject(child, index + 1, folders);
                }
            }
        }

        return false;
    }




}
