package com.microservice.fileservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileTypeValidator implements ConstraintValidator<ValidateFileType, MultipartFile[]> {

    String[] allowedTypes;
    String message;
    @Override
    public void initialize(ValidateFileType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        allowedTypes = constraintAnnotation.allowedTypes();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {

        for(MultipartFile file : files) {
            if (file == null || file.getContentType() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
            for (String type : allowedTypes) {
                if (type.equals(file.getContentType())) {
                    try {
                        BufferedImage image = ImageIO.read(file.getInputStream());
                        if(image == null) {
                            context.disableDefaultConstraintViolation();
                            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                            return false;
                        }
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

}
