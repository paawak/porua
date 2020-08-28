<?php

require_once __DIR__ . '/vendor/autoload.php';

require_once __DIR__ . '/com/swayam/ocr/porua/model/Book.php';

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;
use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

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

$app = AppFactory::create();

$book = $entityManager->find('com\swayam\ocr\porua\model\Book', 1);

echo $book->getName();

$app->get('/', function (Request $request, Response $response, $args) {
    $response->getBody()->write("Book: ");
    return $response;
});

$app->run();
?>
