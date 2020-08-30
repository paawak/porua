<?php

require_once __DIR__ . '/vendor/autoload.php';
require_once __DIR__ . '/com/swayam/ocr/porua/rest/IndexController.php';

use DI\Bridge\Slim\Bridge;
use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;
use Monolog\Logger;
use Monolog\Handler\StreamHandler;
use DI\ContainerBuilder;
use com\swayam\ocr\porua\rest\IndexController;

$builder = new ContainerBuilder();
$container = $builder->build();

$app = Bridge::create($container);

$container->set('logger', \DI\value(function() {
            $logger = new Logger('ocr-correction-rest-logger');
            $file_handler = new StreamHandler('../logs/ocr-correction-rest.log');
            $logger->pushHandler($file_handler);
            return $logger;
        }));

$container->set('entityManager', \DI\value(function() {
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
        }));

$app->get('/', [IndexController::class, 'get']);

$app->run();
?>
