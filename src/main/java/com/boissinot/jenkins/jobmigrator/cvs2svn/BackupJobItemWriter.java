package com.boissinot.jenkins.jobmigrator.cvs2svn;

import org.apache.commons.io.IOUtils;
import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BackupJobItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> configFilePaths) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final String fileDateSuffix = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        for (String configFilePath : configFilePaths) {

            File initConfigFile = new File(configFilePath);

            File backupFile = new File(initConfigFile.getParentFile(), initConfigFile.getName() + ".backup-" + fileDateSuffix);
            FileWriter backupFileWriter = new FileWriter(backupFile);

            FileInputStream configFileInputStream = new FileInputStream(initConfigFile);
            IOUtils.copy(configFileInputStream, backupFileWriter);

            backupFileWriter.close();
            configFileInputStream.close();
        }

    }
}
