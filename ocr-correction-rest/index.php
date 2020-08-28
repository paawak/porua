<?php

require_once __DIR__ . '/vendor/autoload.php';

require_once __DIR__ . '/com/swayam/ocr/porua/model/Book.php';

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;
use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;
use Monolog\Logger;
use Monolog\Handler\StreamHandler;
use Pimple\Container;

$app = AppFactory::create();

$container = new Container();

$container['logger'] = function($c) {
    $logger = new Logger('ocr-correction-rest-logger');
    $file_handler = new StreamHandler('../logs/ocr-correction-rest.log');
    $logger->pushHandler($file_handler);
    return $logger;
};

$container['entityManager'] = function($c) {
    $dbParams = array(
        'driver' => 'pdo_mysql',
        'user' => 'root',
        'password' => 'root123',
        'dbname' => 'porua',
    );

    $isDevMode = true;
    $proxyDir = null;
    $cache = null;
    $useSimpleAnnotationReader = false;
    $config = Setup::createAnnotationMetadataConfiguration(array(__DIR__ . "/com/swayam/ocr/porua/model"), $isDevMode, $proxyDir, $cache, $useSimpleAnnotationReader);

    $entityManager = EntityManager::create($dbParams, $config);
    return $entityManager;
};

$app->get('/', function (Request $request, Response $response) {
    $container['logger']->addInfo("Welcome!!");
    $book = $container['entityManager']->find('com\swayam\ocr\porua\model\Book', 1);
    $response->getBody()->write("Book: " + $book->getName());
    return $response;
});

$app->run();
?>
