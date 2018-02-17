package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.AmazonClient;
import com.csye6225.spring2018.User;
import com.csye6225.spring2018.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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

    private AmazonClient amazonClient;

    @Autowired
    ImageController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @Autowired
    Environment env;

    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    public String uploadPhoto(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request, ModelMap modelMap) throws IOException {

        if(env.getProperty("app.profile.path").equals("aws")) {


            HttpSession session = request.getSession(false);
            if (session == null){
                return "index";
            }
            for (User u : userRepository.findAll()) {
                if (u.getFirstname().equals(session.getAttribute("firstname")) && u.getLastname().equals(session.getAttribute("lastname"))) {
                    String existPic = String.valueOf(session.getAttribute("fileUrl"));
                    if (existPic != null) {
                        this.amazonClient.deleteFileFromS3Bucket(existPic);
                    }
                    String fileUrl = this.amazonClient.uploadFile(multipartFile);
                    u.setPhoto_location(fileUrl);
                    session.setAttribute("fileUrl", fileUrl);
                    session.setAttribute("uploadMessage", "You successfully uploaded '" + multipartFile.getOriginalFilename() + "'");

                }
            }
                return "editProfile";

            }else {
            HttpSession session = request.getSession(false);
            logger.info("Attempting to save file by using post method");
            try {

                logger.info("The file is being added");
                // Get the file and save it somewhere

                for (User u : userRepository.findAll()) {
                    if (u.getFirstname().equals(session.getAttribute("firstname")) && u.getLastname().equals(session.getAttribute("lastname"))) {
                        String destination = "/home/temp/" + u.getId() + multipartFile.getOriginalFilename();
                        File file = new File(destination);
                        if (file.exists()) {
                            file.delete();
                        }
                        multipartFile.transferTo(file);
                        u.setPhoto_location(destination);
                        session.setAttribute("photo_location", u.getPhoto_location());
                        session.setAttribute("uploadMessage", "You successfully uploaded '" + multipartFile.getOriginalFilename() + "'");
                        session.setAttribute("photo", file);
                        modelMap.addAttribute("photo", file);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "editProfile";
        }
    }

    @RequestMapping(value = "/deletePhoto" , method = RequestMethod.POST)
    public String deleteImagePost(HttpServletRequest request){

        if (env.getProperty("app.profile.path").equals("aws")){
            HttpSession session = request.getSession(false);
            if(session == null){
                return "index";
            }
            String fileUrl = String.valueOf(session.getAttribute("fileUrl"));
            this.amazonClient.deleteFileFromS3Bucket(fileUrl);
            String email = String.valueOf(session.getAttribute("email"));
            for (User u : userRepository.findAll()){
                if (u.getEmail().equals(email)){
                    String destination = "/home/temp/google.png";
                    u.setPhoto_location(destination);
                    session.setAttribute("photo_location", u.getPhoto_location());
                }
            }

            session.setAttribute("uploadMessage","");
            session.setAttribute("deleteMessage", "You have successfully deleted the photo");
            return "editProfile";
        }else {
            HttpSession session = request.getSession(false);
            if (session == null) {
                return "index";
            }
            String photo = String.valueOf(session.getAttribute("photo_location"));
            File file = new File(photo);
            file.delete();
            String email = String.valueOf(session.getAttribute("email"));
            for (User u : userRepository.findAll()){
                if (u.getEmail().equals(email)){
                    String destination = "/home/temp/google.png";
                    u.setPhoto_location(destination);
                    session.setAttribute("photo_location", u.getPhoto_location());
                }
            }
            session.setAttribute("uploadMessage", "");
            session.setAttribute("deleteMessage", "You have successfully deleted the photo");
            return "editProfile";
        }
    }

}
