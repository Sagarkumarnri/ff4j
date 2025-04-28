http://localhost:8080/calc/add?a=10&b=5


http://localhost:8080/h2-console



jdbc-url-> jdbc:h2:mem:ff4jdb




http://localhost:8080/ff4j-web-console/features




curl --location 'http://localhost:8080/calculator/operate' \
--header 'Content-Type: application/json' \
--data '{
    "canaryIds": ["canary1", "canary2", "canary3"],
    "num1": 10,
    "num2": 5,
    "operation": "ADD"
}'
