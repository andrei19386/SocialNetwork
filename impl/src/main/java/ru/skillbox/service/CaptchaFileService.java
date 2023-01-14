package ru.skillbox.service;

import com.github.cage.Cage;
import com.github.cage.image.Painter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.skillbox.common.CaptchaDto;
import ru.skillbox.config.CloudinaryConfig;
import ru.skillbox.model.CaptchaFile;
import ru.skillbox.repository.CaptchaFileRepository;
import ru.skillbox.response.CaptchaResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaptchaFileService {

    private final CaptchaFileRepository captchaFileRepository;
    private final CloudinaryConfig cloudinaryConfig;
    private static final Logger logger = LogManager.getLogger(CaptchaFileService.class);


    public Optional<CaptchaFile> getCaptchaFileByName(String name) {
        return captchaFileRepository.findByName(name);
    }

    public CaptchaResponse generateCaptchaResponse() {
        CaptchaResponse response = new CaptchaResponse();
        CaptchaFile captchaFile = makeCaptchaFile();
        response.setImage(captchaFile.getPath());
        response.setSecret(captchaFile.getName());
        return response;
    }

    private CaptchaFile makeCaptchaFile() {
        logger.info("uploading captcha file");
        CaptchaFile captchaFile = new CaptchaFile();
        CaptchaDto captcha = generateCaptcha();
        captchaFile.setPath(getCloudinaryUrl(captcha.getBytes()));
        captchaFile.setName(captcha.getCode());
        return captchaFileRepository.save(captchaFile);
    }

    public CaptchaDto generateCaptcha() {
        final Painter painter = new Painter(150, 70, null, null, null, null);
        Cage cage = new Cage(painter, null, null, null, null, null, null);
        String code = cage.getTokenGenerator().next().substring(0, 4);
        return new CaptchaDto(code, cage.draw(code));
    }

    private String getCloudinaryUrl(byte[] bytes) {
        String url = "";
        try {
            url = cloudinaryConfig
                    .getCloudinary()
                    .uploader()
                    .upload(bytes, new HashMap())
                    .get("url")
                    .toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
