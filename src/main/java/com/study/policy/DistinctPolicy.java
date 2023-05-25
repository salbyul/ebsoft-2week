package com.study.policy;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class DistinctPolicy implements FileRenamePolicy {

    @Override
    public File rename(File file) {

        String name = file.getName();
        String body;
        String ext;
        int dot = name.lastIndexOf(".");
        if (dot != -1) {
            body = name.substring(0, dot);
            ext = name.substring(dot);
        } else {
            body = name;
            ext = "";
        }

        String newName;
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        newName = uuid.toString() + now + "[" + body + "]" + ext;
        return new File(file.getParent(), newName);
    }
}
