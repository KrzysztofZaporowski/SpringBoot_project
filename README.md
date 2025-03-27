# How it works
This file is a short description how it works and what it does.
## Adding HTML page connected to server
We need `MainController` and our **HTML** page name. Also if we want to take some action in case of for example erros we have to implements some interfaces. 
**Examples**
```java
package com.test.springboot_html; 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/")
    public String index() {
        return "index"; //our HTML file name
    }
}
```
This was first example with simple index.html page that is shown in our local host.

**Adding page that inform us that there was some error**
```java
package com.test.springboot_html;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController { //impelemnt error controller interface
    @GetMapping("/error") //looking for errors
    public String handleError() {
        return "error";  //return HTML file name that should be shown if error occurs
    }
}
```

## HTML, CSS and JAVA Script
In this section shortly HTML, CSS and JS files are going to be explained

### HTML
In this project there are two HTML files. First of all HTML files should be in  templates catalog - `src/main/resources/templates` 
