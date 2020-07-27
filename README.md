## moviesite 

#TODO:
  * {
  
    SELECT SUM(mycount) 
    FROM  
        (SELECT COUNT(DISTINCT(movie.movie_id)) AS mycount
         FROM movie
        INNER JOIN movies_genres ON movies_genres.movie_id = movie.movie_id
        WHERE movies_genres.movie_genre_id IN (1,2)
        GROUP BY movie.movie_id
        HAVING COUNT(movies_genres.movie_genre_id) = 2
    ) as R; 
    
  }
  * Logger
  * Да може да се променя филтъра по година, актьор, режисьор
  * Бонус: Като отворя страница с филм, да ми излизат отдолу:
    - Подобни филми ( същия жанр)
    - Филми от същия режисьор
    - Филми със същите актьори
    - Бонус:  Хора, харесали този филм, харесват също

## Server-side technology
* Spring framework
* Spring boot
* Spring security
* JPA and Hibernate
* Lombok for Getters & Setters 
* JUnit and integrated tests
