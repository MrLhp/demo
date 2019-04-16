/**
 *
 */
package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author meime
 */
@Slf4j
public class FileDataReader {

    public static Set<String> getFileContent(final String fileName) {
        final Set<String> res = new HashSet<String>();
        for (final String s : FileDataReader.getFileContentArrary(fileName)) {
            res.add(s);
        }
        return res;
    }

    public static List<String> getFileContentArrary(final String fileName) {
        final File file = new File(fileName);
        return FileDataReader.getFileContentArrary(file);
    }

    public static List<String> getFileContentArrary(final File file) {
        final FileReader fr = null;
        BufferedReader br = null;
        if (!file.exists() || file.isDirectory()) {
            FileDataReader.log.warn("getFileContent : file is not exits! ->" + file.getAbsolutePath());
            return null;
        }
        final List<String> data = new ArrayList<String>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            while (null != (line = br.readLine())) {
                if (StringUtils.isNotBlank(line)) {
                    data.add(line);
                }
            }
        } catch (final Exception e) {
            FileDataReader.log.debug("getFileContent: file ->' " + file.getAbsolutePath() + " ' format error!", e);
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
                if (null != fr) {
                    fr.close();
                }
            } catch (final IOException e2) {
                FileDataReader.log.debug("getFileContent: file close error!", e2);
            }
        }
        return data;
    }
}
