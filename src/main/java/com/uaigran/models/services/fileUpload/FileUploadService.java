package com.uaigran.models.services.fileUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService implements IFileUploadService {

    @Autowired
    private AwsService _awsService;

    public String upload(MultipartFile file, String fileName) throws Exception {
        var fileUri = "";

        try {
            fileUri = _awsService.upload(file, fileName);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return fileUri;
    }
}