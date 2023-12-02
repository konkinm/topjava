#### get Meals 100003
   ```
   curl --location 'http://localhost:8080/topjava/rest/profile/meals/100003'
   ```
#### get Meals not found
   ```
   curl --location 'http://localhost:8080/topjava/rest/profile/meals/100008'
   ```

#### delete Meals
   ```
   curl --location --request DELETE 'http://localhost:8080/topjava/rest/profile/meals/100003'
   ```

#### update Meals
   ```
   curl --location --request PUT 'http://localhost:8080/topjava/rest/profile/meals/100003' \
   --header 'Content-Type: application/json' \
   --data '{ \
       "id": 100003, \
       "dateTime": "2020-01-30T10:02:00", \
       "description": "Обновленный завтрак", \
       "calories": 200 \
   }'
   ```

#### create Meals
   ```
   curl --location 'http://localhost:8080/topjava/rest/profile/meals/' \
   --header 'Content-Type: application/json' \
   --data '{ \
       "dateTime": "2020-02-01T18:00:00", \
       "description": "Созданный ужин", \
       "calories": 300 \
   }'
   ```

#### get All Meals
   ```
   curl --location 'http://localhost:8080/topjava/rest/profile/meals/'
   ```

#### filter Meals
   ```
   curl --location 'http://localhost:8080/topjava/rest/profile/meals/between?startDate=2020-01-30&startTime=20%3A00&endDate=2020-01-31&endTime=23%3A00'
   ```