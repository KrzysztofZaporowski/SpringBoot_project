function appendValue(value) {
    document.getElementById('display').value += value;
}

function clearDisplay() {
    document.getElementById('display').value = '';
}

function deleteLastCharacter() {
    const display = document.getElementById('display');
    display.value = display.value.slice(0, -1);
}

function toggleSign() {
    const display = document.getElementById('display');
    const currentValue = display.value;

    if (currentValue.startsWith('-')) {
        display.value = currentValue.slice(1);
    } else {
        display.value = '-' + currentValue;
    }
}

function calculateAndSendToServer() {
    const display = document.getElementById('display');
    const expression = display.value;

    try {
        // Calculate the result
        const result = eval(expression);
        display.value = result;

        // Send the expression to the server
        fetch('/calculate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                expression: expression,
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

function sendEquation() {
    const equation = document.getElementById('equationInput').value;
    const resultElement = document.getElementById('result');

    fetch('/api/equation', {
        method: 'POST',
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
                const data = JSON.parse(text);

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