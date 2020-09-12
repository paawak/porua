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

$callableResolver = $app->getCallableResolver();
$responseFactory = $app->getResponseFactory();
$serverRequestCreator = ServerRequestCreatorFactory::create();
$request = $serverRequestCreator->createServerRequestFromGlobals();
$errorHandler = new ErrorHandler($callableResolver, $responseFactory, $logger);

$app->addRoutingMiddleware();
$app->addBodyParsingMiddleware();
$errorMiddleware = $app->addErrorMiddleware(true, true, true);
$errorMiddleware->setDefaultErrorHandler($errorHandler);

$app->get('/', [IndexController::class, 'get']);
$app->get('/train/book', [TrainingController::class, 'getAllBooks']);
$app->get('/train/book/{bookId}/page-count', [TrainingController::class, 'getPageCountInBook']);
$app->get('/train/page', [TrainingController::class, 'getPagesInBook']);
$app->put('/train/page/ignore/{pageImageId}', [TrainingController::class, 'markPageAsIgnored']);
$app->put('/train/page/complete/{pageImageId}', [TrainingController::class, 'markPageAsCompleted']);
$app->get('/train/word', [TrainingController::class, 'getWordsInPage']);
$app->get('/train/word/image', [TrainingController::class, 'getWordImage']);
$app->put('/train/word', [TrainingController::class, 'applyCorrectionToOcrWords']);
$app->put('/train/word/ignore', [TrainingController::class, 'markOcrWordsAsIgnored']);

$app->run();
?>
