1. get
   ```
   curl --location 'http://localhost:8080/topjava/rest/meals/100003'
   ```

2. delete
   ```
   curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100003'
   ```
   
3. update
   ```
   curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100003' \
   --header 'Content-Type: application/json' \
   --data '{ \
       "id": 100003, \
       "dateTime": "2020-01-30T10:02:00", \
       "description": "Обновленный завтрак", \
       "calories": 200 \
   }'
   ```

4. create
   ```
   curl --location 'http://localhost:8080/topjava/rest/meals/' \
   --header 'Content-Type: application/json' \
   --data '{ \
       "dateTime": "2020-02-01T18:00:00", \
       "description": "Созданный ужин", \
       "calories": 300 \
   }'
   ```
   
5. getAll
   ```
   curl --location 'http://localhost:8080/topjava/rest/meals/'
   ```
   
6. getBetween
   ```
   curl --location 'http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-30&startTime=20%3A00&endDate=2020-01-31&endTime=23%3A00'
   ```