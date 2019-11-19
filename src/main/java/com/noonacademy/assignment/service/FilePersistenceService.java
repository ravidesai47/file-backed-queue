package com.noonacademy.assignment.service;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class FilePersistenceService {

    public void saveEntryToFile(String filePath, String entity) throws IOException {
        FileUtils.write(new File(filePath), entity, Charsets.UTF_8);
    }

    public String readEntryAndDeleteFile(String filePath) throws IOException {
        String entry = FileUtils.readFileToString(new File(filePath), Charsets.UTF_8);
        FileUtils.forceDelete(new File(filePath));
        return entry;
    }

}
