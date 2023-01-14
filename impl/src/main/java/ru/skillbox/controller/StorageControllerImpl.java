package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.common.PhotoDto;
import ru.skillbox.model.StorageController;
import ru.skillbox.response.FileUploadResponse;
import ru.skillbox.service.PostFileService;
import ru.skillbox.service.ProfileFileService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StorageControllerImpl implements StorageController {

    private final PostFileService postFileService;
    private final ProfileFileService profileFileService;

    @Override
    public ResponseEntity<PhotoDto> uploadPostFile(MultipartFile file) {
        return ResponseEntity.ok(postFileService.generatePostPhotoDto(file));
    }

    @Override
    public ResponseEntity<FileUploadResponse> uploadFile(MultipartFile file) {
        return ResponseEntity.ok(profileFileService
                .generateProfileFileUploadResponse(profileFileService.uploadProfileFile(file)));
    }
}
