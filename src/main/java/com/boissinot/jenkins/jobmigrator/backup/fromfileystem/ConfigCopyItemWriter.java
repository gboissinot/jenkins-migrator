package com.boissinot.jenkins.jobmigrator.backup.fromfileystem;

import org.apache.commons.io.IOUtils;
import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class ConfigCopyItemWriter implements ItemWriter<File> {

    private String outputConfigsDirPath;

    public ConfigCopyItemWriter(String outputConfigsDirPath) {
        this.outputConfigsDirPath = outputConfigsDirPath;
    }

    @Override
    public void write(List<? extends File> files) throws Exception {

        File outputDir = new File(outputConfigsDirPath);
        outputDir.mkdirs();


        for (File file : files) {

            FileInputStream fileInputStream = new FileInputStream(file);

            File specificDirFile = new File(outputDir, file.getParentFile().getName());
            specificDirFile.mkdir();
            File outputFile = new File(specificDirFile, file.getName());

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            IOUtils.copy(fileInputStream, fileOutputStream);

            fileInputStream.close();
            fileOutputStream.close();

        }
    }
}
