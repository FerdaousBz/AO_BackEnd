package com.example.AIGen.services;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
	
	private final Cloudinary cloudinary;
	
	 @Autowired
	    public CloudinaryService(Cloudinary cloudinary) {
	        this.cloudinary = cloudinary;
	    }

	    public String uploadImage(byte[] imageData, String publicId) throws IOException {
	        Map uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.asMap("public_id", publicId));
	        return (String) uploadResult.get("secure_url");
	    }

	    public String getImageUrl(String publicId) {
	        return cloudinary.url().secure(true).generate(publicId);
	    }

}
