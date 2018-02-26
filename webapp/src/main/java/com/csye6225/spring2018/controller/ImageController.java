package com.csye6225.spring2018.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.csye6225.spring2018.AmazonClient;
import com.csye6225.spring2018.User;
import com.csye6225.spring2018.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class ImageController {

    @Autowired
    private UserRepository userRepository;

    private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private AmazonClient amazonClient;

    @Autowired
    ImageController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @Autowired
    Environment env;

    @PostMapping("/uploadPhoto")
    public String uploadPhoto(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null){
            return "index";
        }
        String existPic = String.valueOf(session.getAttribute("fileUrl"));
        logger.info("Attempting to save file by using post method");

            for (User u : userRepository.findAll()) {
                if (u.getEmail().equals(session.getAttribute("email"))) {
                    if(env.getProperty("app.profile.path").equals("aws")) {
                        if (existPic != null) {
                            this.amazonClient.deleteFileFromS3Bucket(existPic);
                        }
                        String fileUrl = this.amazonClient.uploadFile(multipartFile);
                        u.setPhoto_location(fileUrl);
                        session.setAttribute("fileUrl", fileUrl);
                        session.setAttribute("uploadMessage", "You successfully uploaded '" + multipartFile.getOriginalFilename() + "'");
                    }

                    else {
                        try {
                            File file = new File(existPic);
                            if (!existPic.equalsIgnoreCase("/home/temp/google.png")) {
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                            String fileUrl = "/home/temp/" + u.getId() + multipartFile.getOriginalFilename();
                            multipartFile.transferTo(file);
                            u.setPhoto_location(fileUrl);
                            session.setAttribute("fileUrl", fileUrl);
                            session.setAttribute("uploadMessage", "You successfully uploaded '" + multipartFile.getOriginalFilename() + "'");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return "editProfile";
    }

    @PostMapping("/deletePhoto")
    public String deleteImagePost(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if(session == null){
            return "index";
        }
        String email = String.valueOf(session.getAttribute("email"));
        String fileUrl = String.valueOf(session.getAttribute("fileUrl"));
        String destination = "/home/temp/google.png";
        for (User u : userRepository.findAll()) {
            if (u.getEmail().equals(email)) {
                if (env.getProperty("app.profile.path").equals("aws")) {
                    String deleteMsg = this.amazonClient.deleteFileFromS3Bucket(fileUrl);
                    u.setPhoto_location(destination);
                    session.setAttribute("fileUrl", destination);
                    session.setAttribute("deleteMessage", deleteMsg);
                } else {
                    File file = new File(fileUrl);
                    if (!fileUrl.equalsIgnoreCase("/home/temp/google.png")) {
                        file.delete();
                    }
                    u.setPhoto_location(destination);
                    session.setAttribute("fileUrl", destination);
                    session.setAttribute("deleteMessage", "You have successfully deleted the photo");
                }
                session.setAttribute("uploadMessage", "");
            }

        }
        return "editProfile";
    }

}
