package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.config.CloudinaryConfig;
import ru.skillbox.common.PhotoDto;
import ru.skillbox.model.Person;
import ru.skillbox.model.PostFile;
import ru.skillbox.repository.PostFileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostFileService {

    private final PostFileRepository postFileRepository;
    private final PersonService personService;
    private final CloudinaryConfig cloudinaryConfig;
    private static final Logger logger = LogManager.getLogger(PostFileService.class);


    public Optional<PostFile> getPostFileByPath(String name) {
        return postFileRepository.findByPath(name);
    }


    public PostFile uploadPostFile(MultipartFile file) {
        logger.info("uploading post file " + file.getOriginalFilename());
        Person person = personService.getCurrentPerson();
        PostFile postFile = new PostFile();
        postFile.setPath(cloudinaryConfig.getCloudinaryUrl(file));
        postFile.setName(file.getOriginalFilename());
        person.setPhoto(postFile.getPath());
        personService.savePerson(person);
        return postFileRepository.save(postFile);
    }

    public PhotoDto generatePostPhotoDto(MultipartFile file) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setImagePath(uploadPostFile(file).getPath());
        return photoDto;
    }
}
