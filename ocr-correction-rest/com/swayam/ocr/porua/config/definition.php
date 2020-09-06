<?php

use Doctrine\ORM\EntityManager;
use Doctrine\ORM\Tools\Setup;
use Monolog\Handler\StreamHandler;
use Monolog\Logger;
use Psr\Log\LoggerInterface;
use Monolog\Processor\PsrLogMessageProcessor;

return [
    LoggerInterface::class => function () {
        $logger = new Logger('ocr-correction-rest-logger');
        $file_handler = new StreamHandler(__DIR__ . '/../../../../../../logs/ocr-correction-rest.log');
        $logger->pushHandler($file_handler);
        $logger->pushProcessor(new PsrLogMessageProcessor);
        return $logger;
    },
    EntityManager::class => function () {
        $dbParams = array(
            'driver' => 'pdo_mysql',
            'user' => 'root',
            'password' => 'root123',
            'dbname' => 'porua',
            'host' => 'localhost',
            'charset' =>  'UTF8'
        );

        $isDevMode = true;
        $proxyDir = null;
        $cache = null;
        $useSimpleAnnotationReader = false;
        $config = Setup::createAnnotationMetadataConfiguration(array(__DIR__ . "/../model"), $isDevMode, $proxyDir, $cache, $useSimpleAnnotationReader);

        $entityManager = EntityManager::create($dbParams, $config);
        return $entityManager;
    }
];
