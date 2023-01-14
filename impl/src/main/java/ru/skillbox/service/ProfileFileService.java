package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.config.CloudinaryConfig;
import ru.skillbox.common.FileUploadDto;
import ru.skillbox.model.Person;
import ru.skillbox.model.ProfileFile;
import ru.skillbox.repository.ProfileFileRepository;
import ru.skillbox.response.FileUploadResponse;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProfileFileService {

    private final ProfileFileRepository profileFileRepository;
    private final PersonService personService;
    private final CloudinaryConfig cloudinaryConfig;
    private static final Logger logger = LogManager.getLogger(ProfileFileService.class);


    public ProfileFile uploadProfileFile(MultipartFile file) {
        logger.info("uploading profile file " + file.getOriginalFilename());
        Person person = personService.getCurrentPerson();
        ProfileFile profileFile = new ProfileFile();
        profileFile.setPath(cloudinaryConfig.getCloudinaryUrl(file));
        profileFile.setName(file.getOriginalFilename());
        person.setPhoto(profileFile.getPath());
        personService.savePerson(person);
        return profileFileRepository.save(profileFile);
    }

    public FileUploadResponse generateProfileFileUploadResponse(ProfileFile profileFile) {
        FileUploadResponse response = new FileUploadResponse();
        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setPhotoName(profileFile.getPath());
        fileUploadDto.setPhotoId(String.valueOf(profileFile.getId()));
        response.setData(fileUploadDto);
        if (response.getData() == null) {
            response.setError("Неверный запрос");
            response.setErrorDescription("Неверный код авторизации");
            response.setTimestamp(new Date().getTime());
        }
        return response;
    }


}
