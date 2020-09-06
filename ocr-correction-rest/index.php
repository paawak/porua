<?php

require __DIR__ . '/com/swayam/ocr/porua/rest/IndexController.php';
require __DIR__ . '/com/swayam/ocr/porua/rest/TrainingController.php';

use DI\Bridge\Slim\Bridge;
use Slim\Handlers\ErrorHandler;
use Slim\Factory\ServerRequestCreatorFactory;
use Psr\Log\LoggerInterface;

use com\swayam\ocr\porua\rest\IndexController;
use com\swayam\ocr\porua\rest\TrainingController;

$container = require __DIR__ . '/com/swayam/ocr/porua/config/bootstrap.php';

$app = Bridge::create($container);

$logger = $container->get(LoggerInterface::class);

$displayErrorDetails = true;
$callableResolver = $app->getCallableResolver();
$responseFactory = $app->getResponseFactory();
$serverRequestCreator = ServerRequestCreatorFactory::create();
$request = $serverRequestCreator->createServerRequestFromGlobals();
$errorHandler = new ErrorHandler($callableResolver, $responseFactory, $logger);

$app->addRoutingMiddleware();
$errorMiddleware = $app->addErrorMiddleware($displayErrorDetails, false, false);
$errorMiddleware->setDefaultErrorHandler($errorHandler);

$app->get('/', [IndexController::class, 'get']);
$app->get('/train/book', [TrainingController::class, 'getAll']);
$app->get('/train/book/{bookId}/page-count', [TrainingController::class, 'getOne']);

$app->run();
?>
