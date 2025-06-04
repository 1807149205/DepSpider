package org.wzl.depspider.react.project;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.react.dto.ProjectFileRelation;
import org.wzl.depspider.react.exception.ScanPathSetException;
import org.wzl.depspider.react.project.config.Language;
import org.wzl.depspider.react.project.config.ProjectConfiguration;
import org.wzl.depspider.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

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
        List<Language> languages = projectConfiguration.getLanguages();
        if (source.startsWith("@")) {
            String[] split = source.split("/");
            File currentFile = srcFileFolder;
            for (int i = 1 ; i < split.length; i++) {
                String folder = split[i];
                currentFile = new File(currentFile, folder);
            }

            if (languages.contains(Language.TS)) {
                relativeFile = new File(currentFile, "index.tsx");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
                relativeFile = new File(currentFile, "index.ts");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
            }
            if (languages.contains(Language.JS)) {
                relativeFile = new File(currentFile, "index.jsx");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
                relativeFile = new File(currentFile, "index.js");
                if (relativeFile.isFile()) {
                    return relativeFile;
                }
            }
        } else if (source.startsWith("../")) {
            File parentFile = curFile;
            parentFile = parentFile.getParentFile();
            String[] split = source.split("/");
            //导入有三种规则
            // 1、../components/CommonCard
            // 2、../components/CommonCard/index.jsx
            // 3、../components/CommonCard/index
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
                    File currentFile;
                    //当第3种情况时
                    if (languages.contains(Language.TS)) {
                        currentFile = new File(parentFile, str + ".tsx");
                        if (currentFile.isFile()) {
                            return currentFile;
                        }
                        currentFile = new File(parentFile, str + ".ts");
                        if (currentFile.isFile()) {
                            return currentFile;
                        }
                    }
                    if (languages.contains(Language.JS)) {
                        currentFile = new File(parentFile, str + ".jsx");
                        if (currentFile.isFile()) {
                            return currentFile;
                        }
                        currentFile = new File(parentFile, str + ".js");
                        if (currentFile.isFile()) {
                            return currentFile;
                        }
                    }
                    //第1种可能
                    parentFile = new File(parentFile, str);
                    if (languages.contains(Language.TS)) {
                        relativeFile = new File(parentFile, "index.tsx");
                        if (relativeFile.isFile()) {
                            return relativeFile;
                        }
                        relativeFile = new File(parentFile, "/index.ts");
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

    private boolean dfsFileHasProject(File file, int index, String[] split) {
        if (index >= split.length) {
            return true;
        }

        File[] children = file.listFiles();
        if (children == null) {
            return false;
        }

        for (File child : children) {
            if (child.isDirectory() && child.getName().equals(split[index])) {
                return dfsFileHasProject(child, index + 1, split);
            }
        }

        return false;
    }


}
