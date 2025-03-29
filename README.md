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

No examples in this section because it's very simple `index.html` page and even simplier `error.html` file.
### CSS
Two `.css` files for main and error page. CSS files should be in static catalog - `src/main/resources/static`
### JAVA Script
As CSS files JS files should be in static catalog - `src/main/resources/static`
Let's explain crucial calculator's method and functions that send equation to server.

**Calculate and send to server**
```js
function calculateAndSendToServer() {
    const display = document.getElementById('display'); //get data from display
    const expression = display.value;

    try {
        // Calculate the result
        const result = eval(expression); //eval is not safe, it is not reccomended to use it in comercial projects
        display.value = result;

        // Send the expression to the server
        fetch('/calculate', {
            method: 'POST',    /using POST method
            headers: {
                'Content-Type': 'application/json',    //type -> JSON
            },
            body: JSON.stringify({
                expression: expression,    //convert to json format
                result: result
            })
        })
            .then(response => response.json())
            .then(data => {
                console.log('Server response:', data);
                // You can handle the server response here
            })
            .catch(error => {
                console.error('Error:', error);
            });
    } catch (error) {
        display.value = 'Error';
    }
}
```

**Send equation**
```js
function sendEquation() {
    const equation = document.getElementById('equationInput').value; //get data from input
    const resultElement = document.getElementById('result');

    fetch('/api/equation', {
        method: 'POST',    //POST method
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ equation: equation })
    })
        .then(response => {
            console.log('Response status:', response.status);
            return response.text();
        })
        .then(text => {
            console.log('Response text:', text);

            try {
                const data = JSON.parse(text);    /JSON to text

                if (data.result !== undefined) {
                    resultElement.textContent = `Wynik: ${data.result}`;
                    resultElement.style.color = 'lightgreen';
                } else if (data.error) {
                    resultElement.textContent = `Błąd: ${data.error}`;
                    resultElement.style.color = 'red';
                } else {
                    resultElement.textContent = 'Nieznany format odpowiedzi';
                    resultElement.style.color = 'red';
                }
            } catch (jsonError) {
                resultElement.textContent = `Błąd parsowania: ${jsonError.message}`;
                resultElement.style.color = 'red';
            }
        })
        .catch(error => {
            resultElement.textContent = `Błąd: ${error.message}`;
            resultElement.style.color = 'red';
            console.error('Błąd:', error);
        });
}
```

## JAVA file to controll opertaions
### Equation controller
```java
package com.test.springboot_html;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.objecthunter.exp4j.Expression;        //import to perform equations
import net.objecthunter.exp4j.ExpressionBuilder; //import to perform equations

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EquationController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @PostMapping("/equation")
    public ResponseEntity<String> solveEquation(@RequestBody Map<String, String> request) {
        String equation = request.get("equation");
        Map<String, Object> response = new HashMap<>();
        System.out.println("Otrzymano równanie: " + equation);
        try {
            Expression expression = new ExpressionBuilder(equation).build();
            double result = expression.evaluate();

            response.put("equation", equation);
            response.put("result", result);
            return ResponseEntity.ok(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            response.put("error", "Błędne równanie");
            try {
                return ResponseEntity.badRequest()
                        .body(objectMapper.writeValueAsString(response));
            } catch (Exception jsonError) {
                return ResponseEntity.badRequest().body("{\"error\": \"Błąd parsowania\"}");
            }
        }
    }
}
```
# Summary
This is my first project in SpringBoot so there could be som begginnig mistakes, for example history for simple calc is not preety printed, but I want to make new better project soon
