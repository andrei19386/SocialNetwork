package ru.skillbox.model;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.common.PhotoDto;
import ru.skillbox.response.FileUploadResponse;

public interface StorageController {

    @RequestMapping(value = "/post/storagePostPhoto", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<PhotoDto> uploadPostFile(@RequestParam("file") MultipartFile file);

    @RequestMapping(value = "/storage", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file);
}
