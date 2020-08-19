## moviesite back-end

## Comment 
* JDK is updated to 14.0.2, 
that's why if there is a problem, I must be notified

#TODO:
  * Donate system for an ads
  * Анг/бг менюта
  * Recent activity (bookmarks, upload movies, reviews)
  * Return reviews sorted by date and pagination (return the date too)
  commit new column for reviews Timestamp
  * Return genres by alphabetically
  ```
        /**
        * TODO: Another view model for User that is not me (another user)
        * @param username the username of the user
        * @return only relevant data about the user (id, image, name, maybe activity and authority)
        */
       @GetMapping("/user")
       public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
           User u = this.userService.getByUsername(username);
           if (u == null)
               return ResponseEntity.ok(Map.of("error", "No such user"));
           return ResponseEntity.ok(UserViewModel.toViewModel(u));
       }
```

## Server-side technology
* Spring Framework
* Spring Boot
* Spring Security
* Spring Data JPA
* Lombok for Getters & Setters 
* JUnit and integrated tests
