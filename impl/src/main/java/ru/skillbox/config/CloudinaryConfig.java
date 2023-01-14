package ru.skillbox.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
@Component
@PropertySource("classpath:application.properties")
public class CloudinaryConfig {

    @Value("${cloud.name}")
    private String cloudName;
    @Value("${api.key}")
    private String apiKey;
    @Value("${api.secret}")
    private String apiSecret;


    public Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }

    public String getCloudinaryUrl(MultipartFile file) {
        try {
            return (String) getCloudinary().uploader().upload(file
                    .getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}