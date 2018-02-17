package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.User;
import com.csye6225.spring2018.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;

@Controller
public class IndexController {

  @Autowired
  private UserRepository userRepository;

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    logger.info("Loading home page.");
    return "index";
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String getRegister() {
    logger.info("Loading registration page.");
    return "register";
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String registerUser() {
    logger.info("Loading registration page.");
    return "register";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    logger.info("Loading login page.");
    return "login";
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String postLogin() {
    logger.info("Loading login after registration.");
    return "login";
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String logoutPage(HttpServletRequest request){
    logger.info("Loading index page after logging out.");
    request.getSession().invalidate();
    return "index";
  }

  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public String postLogoutPage(HttpServletRequest request){
    logger.info("Loading index page after logging out.");
    request.getSession().invalidate();
    return "index";
  }

  @RequestMapping(value = "/loginSuccess", method = RequestMethod.POST)
  public String userSucceess(HttpServletRequest request, @RequestParam String email, @RequestParam String password){
    for (User u : userRepository.findAll()) {
      if (u.getEmail().equals(email) && BCrypt.checkpw(password, u.getPassword())) {
        logger.info("Login Success!");
        HttpSession session = request.getSession(true);
        session.setAttribute("firstname", u.getFirstname());
        session.setAttribute("lastname", u.getLastname());
        session.setAttribute("photo_location",u.getPhoto_location());
        session.setAttribute("about",u.getAbout());
        session.setAttribute("email",u.getEmail());
        return "UserHome";
      }
    }
    logger.info("User not found!");
    return "login";
  }

  @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
  public String userSuccessGet(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("photo_location") == null){
      logger.info("Redirecting to login as no session found");
      return "login";
    }
    logger.info("Loading user homepage since session is present");
    return "UserHome";
  }

  @RequestMapping(value = "/searchResults", method = RequestMethod.POST)
  public String searchResults(HttpServletRequest request, @RequestParam String search){
      ArrayList<String> searchResults = new ArrayList<>();
      for (User u : userRepository.findAll()){
          if (u.getLastname().contains(search) || u.getFirstname().contains(search)){
              String fullname = u.getFirstname() + " " + u.getLastname();
              searchResults.add(fullname);
          }
      }
      request.setAttribute("searchResults", searchResults);
      return "index";
  }

  @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String viewProfile(HttpServletRequest request, @RequestParam String username){
      logger.info("Loading profile for selected user");
      String[] names = username.split("\\s+");
      for (User u : userRepository.findAll()){
          if (u.getFirstname().equals(names[0]) && u.getLastname().equals(names[1])){
              HttpSession session = request.getSession(true);
              session.setAttribute("firstname",u.getFirstname());
              session.setAttribute("lastname", u.getLastname());
              session.setAttribute("about", u.getAbout());
              return "userProfile";
          }
      }
      return "error";
  }

  @RequestMapping(value = "/userProfile", method = RequestMethod.GET)
  public String getProfile(HttpServletRequest request) {

    if (request.getSession() == null) {
      logger.info("No such user found");
      return "index";
    }
    else {
      return "userProfile";
    }
  }

  @RequestMapping(value = "/editProfile", method = RequestMethod.GET)
  public String getEditProfile(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if (session==null){
      return "index";
    }
    return "editProfile";
  }

  @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
  public String uploadPhoto(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request, ModelMap modelMap) {

    HttpSession session = request.getSession(false);
    logger.info("Attempting to save file by using post method");
    try {

      logger.info("The file is being added");
      // Get the file and save it somewhere

      for (User u : userRepository.findAll()){
        if(u.getFirstname().equals(session.getAttribute("firstname")) && u.getLastname().equals(session.getAttribute("lastname"))){
          String destination = "/home/temp/" + u.getId() +multipartFile.getOriginalFilename();
          File file = new File(destination);
          if(file.exists()){
            file.delete();
          }
          multipartFile.transferTo(file);
          u.setPhoto_location(destination);
          session.setAttribute("photo_location", u.getPhoto_location());
          session.setAttribute("uploadMessage", "You successfully uploaded '" + multipartFile.getOriginalFilename() + "'");
          session.setAttribute("photo", file);
          modelMap.addAttribute("photo",file);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return "editProfile";
  }

  @RequestMapping(value = "/deleteImage" , method = RequestMethod.POST)
  public String deleteImagePost(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if(session == null){
      return "index";
    }
    String photo = String.valueOf(session.getAttribute("photo_location"));
    File file = new File(photo);
    file.delete();
    session.setAttribute("uploadMessage","");
    session.setAttribute("deleteMessage", "You have successfully deleted the photo");
    return "editProfile";
  }

  @RequestMapping("/photo")
  public ResponseEntity<byte[]> getImage(HttpServletRequest request) throws IOException {

    HttpSession session = request.getSession(false);
    String filename = String.valueOf(session.getAttribute("photo_location"));

    InputStream inputImage = new FileInputStream(filename);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[512];
    int l = inputImage.read(buffer);
    while(l >= 0) {
      outputStream.write(buffer, 0, l);
      l = inputImage.read(buffer);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "image/png");
    return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
  }

  @PostMapping("/saveChanges")
  public String updateProfile(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String about, HttpServletRequest request){

    HttpSession session = request.getSession(false);
    for (User newUser : userRepository.findAll()){
      if (newUser.getEmail().equals(String.valueOf(session.getAttribute("email")))){
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setAbout(about);
        userRepository.save(newUser);
      }
    }
    return "editProfile";
  }
}
