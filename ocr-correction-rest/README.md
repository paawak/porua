## Installing dependencies with Composer
    
    /kaaj/installs/php/composer/composer.phar install
    /kaaj/installs/php/composer/composer.phar update

## ORM Library Doctrine
Configuration: <https://www.doctrine-project.org/projects/doctrine-orm/en/2.7/reference/configuration.html>
<https://www.doctrine-project.org/projects/doctrine-orm/en/2.7/tutorials/getting-started.html#getting-started-with-doctrine>
Repository Pattern: <https://www.doctrine-project.org/projects/doctrine-orm/en/2.7/tutorials/getting-started.html#entity-repositories>

### Update Queries
<https://www.doctrine-project.org/projects/doctrine-orm/en/2.7/reference/dql-doctrine-query-language.html#update-queries>
<https://www.doctrine-project.org/projects/doctrine-orm/en/2.7/tutorials/getting-started.html#updating-entities>

## REST Library Slim
<http://www.slimframework.com/docs/v4/>
<http://www.slimframework.com/docs/v4/objects/request.html>

## Dependency Injection with PHP-DI
composer require php-di/slim-bridge
<https://php-di.org/doc/frameworks/slim.html>
<https://php-di.org/doc/container-configuration.html>
Demo: <https://github.com/PHP-DI/demo>

## Google Authentication
<https://developers.google.com/identity/sign-in/web/backend-auth>

## Sample URLs
### Fetch Pages for a Book

	curl -v -X GET "http://localhost:8000/train/page?bookId=1"

### Ignore words

        curl -v -X PUT "http://localhost:8000/train/word/ignore" -H  "accept: application/json" -H  "Content-Type: application/json" -d '[{"bookId":1,"pageImageId":47,"wordSequenceId":2}]'

### Misc

curl -v -X PUT "http://localhost:8000/train/word" -H  "accept: application/json" -H  "Content-Type: application/json" -d "[{\"correctedText\":\"বিলাপ\",\"ocrWordId\":{\"bookId\":1,\"pageImageId\":505,\"wordSequenceId\":24}},{\"correctedText\":\"গড়াগড়ি\",\"ocrWordId\":{\"bookId\":1,\"pageImageId\":505,\"wordSequenceId\":26}},{\"correctedText\":\"ভিক্ষুকী,\",\"ocrWordId\":{\"bookId\":1,\"pageImageId\":505,\"wordSequenceId\":39}},{\"correctedText\":\"করছিস।\",\"ocrWordId\":{\"bookId\":1,\"pageImageId\":505,\"wordSequenceId\":44}}]"


curl -v -X PUT "http://localhost:8000/train/word" -H  "accept: application/json" -H  "Content-Type: application/json" -d "[{\"correctedText\":\"বিলাপ\",\"ocrWordId\":{\"bookId\":1,\"pageImageId\":505,\"wordSequenceId\":24}}]"


curl -v -X PUT "http://localhost:8000/train/word/ignore" -H  "accept: application/json" -H  "Content-Type: application/json" -d "[{\"bookId\":1,\"pageImageId\":505,\"wordSequenceId\":24}]"


curl -v -X PUT "http://localhost:8000/train/word/ignore" -H  "accept: application/json" -H  "Content-Type: application/json" -d "[{\"bookId\":1,\"pageImageId\":505,\"wordSequenceId\":24}]"

curl -v -X PUT "http://localhost:8000/train/page/complete/40" 

curl -v -X PUT "http://localhost:8000/train/page/ignore/41" 

### URLs on deployed server

curl "http://ocrservice.paawak.me/train/book"

 curl "http://ocrservice.paawak.me/train/page?bookId=1"


